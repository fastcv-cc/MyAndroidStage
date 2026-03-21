package cc.fastcv.exquisite_linechart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withSave
import kotlin.math.min
import kotlin.math.sqrt

internal class InnerLineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //绘图可用高度
    private var availableHeight = 0f

    //绘图可用宽度
    private var availableWidth = 0f

    //画笔
    private val paint = Paint().apply {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }


    //平均线高度
    private var averageLineHeight = 0f

    private var lineChartPath = Path()

    private var shaderPath = Path()

    /**
     * 折线图信息
     */
    private val lineChartInfoList = mutableListOf<LineChartInfo>()

    /**
     * 选中项
     */
    private var lastSelectIndex = -1

    private lateinit var coordinator: InnerLineChartViewCoordinator


    private lateinit var params: ExquisiteLineChartParams

    internal fun bindView(
        params: ExquisiteLineChartParams,
        coordinator: InnerLineChartViewCoordinator
    ) {
        this.params = params
        this.coordinator = coordinator
    }

    internal fun refreshView() {
        calc()
        invalidate()
    }

    private fun loadDrawLinePaintConfig() {
        paint.strokeWidth = params.chartLineWidth
        paint.color = params.chartLineColor
        paint.style = Paint.Style.STROKE
        paint.shader = null
    }

    private fun loadDrawCentralLinePaintConfig() {
        paint.strokeWidth = params.centralLineWidth
        paint.color = params.centralLineColor
        paint.style = Paint.Style.FILL
        paint.shader = null
    }

    /**
     * 原始数据点坐标（未经圆滑处理），用于构建贝塞尔曲线
     */
    private val rawPoints = mutableListOf<PointF>()

    private fun calc() {
        if (params.points.isEmpty()) return
        availableHeight = height - params.chartLineWidth
        availableWidth = width - params.chartLineWidth
        rawPoints.clear()
        //x是从0开始的
        var averageXDistance = availableWidth * 1.0f / params.points.size
        //y是预留了从0开始的逻辑
        val averageYDistance =
            (availableHeight - params.bottomSpaceHeight) * 1.0f / (params.yAxisMaxValue - params.yAxisMinValue)

        if (params.autoLineWidth) {
            //点很密集的情况
            if ((averageXDistance - params.chartLineWidth) <1.0f) {
                while (true) {
                    params.chartLineWidth -= 0.2f
                    availableWidth = width - params.chartLineWidth
                    averageXDistance = availableWidth * 1.0f / params.points.size
                    if ((averageXDistance - params.chartLineWidth) >= 1.0f) {
                        break
                    }
                }
            }
        }

        if (lastSelectIndex >= (params.points.size + rawPoints.size)) {
            lastSelectIndex = -1
        }
        calcRawPoints(averageXDistance, averageYDistance)
        calcAverageLineHeight(averageYDistance)
        calcPathAndLineChartInfoList()
        calcShaderPathIfNeed()
        coordinator.onLineChartInfoSelected(lastSelectIndex, null)
    }


    private fun calcRawPoints(averageXDistance: Float, averageYDistance: Float) {
        if (params.addZeroPoint) {
            rawPoints.add(PointF(0f, 0f))
        }

        for (i in 0 until params.points.size) {
            val pointX = (i + 1) * averageXDistance
            val pointY = if (params.yAxisIncrement) {
                params.bottomSpaceHeight + (params.points[i].y - params.yAxisMinValue) * averageYDistance
            } else {
                availableHeight - (params.points[i].y - params.yAxisMinValue) * averageYDistance
            }

            rawPoints.add(PointF(pointX, pointY))
            if (lastSelectIndex == -1) {
                lastSelectIndex = i
            }
        }
    }

    private fun calcAverageLineHeight(averageYDistance: Float) {
        averageLineHeight = availableHeight - (params.yAxisMaxValue - (params.points.map { it.y }
            .sum() / params.points.size)) * averageYDistance
    }

    private fun calcPathAndLineChartInfoList() {
        lineChartInfoList.clear()
        val firstLineChartInfo = if (params.addZeroPoint) {
            LineChartInfo(
                rawPoints[0],
                params.xAxisTouchRange,
                PointF(0f, 0f)
            )
        } else {
            LineChartInfo(
                rawPoints[0],
                params.xAxisTouchRange,
                params.points[0]
            )
        }
        lineChartInfoList.add(firstLineChartInfo)

        lineChartPath.reset()
        lineChartPath.moveTo(rawPoints[0].x, rawPoints[0].y)
        val dataOffset = if (params.addZeroPoint) {
            -1
        } else {
            0
        }

        for (i in 1 until rawPoints.size) {
            val curr = rawPoints[i]

            if (i < rawPoints.size - 1) {
                // 中间点：用二次贝塞尔曲线圆滑处理
                val prev = rawPoints[i - 1]
                val next = rawPoints[i + 1]

                // 计算 prev->curr 方向的单位向量
                val dx1 = curr.x - prev.x
                val dy1 = curr.y - prev.y
                val len1 = sqrt(dx1 * dx1 + dy1 * dy1)

                // 计算 curr->next 方向的单位向量
                val dx2 = next.x - curr.x
                val dy2 = next.y - curr.y
                val len2 = sqrt(dx2 * dx2 + dy2 * dy2)

                // 圆滑半径不能超过相邻线段长度的一半
                val radius = min(params.lineChartRadius, min(len1 / 2f, len2 / 2f))

                // 切入点：从curr沿prev方向回退radius距离
                val enterX = curr.x - (dx1 / len1) * radius
                val enterY = curr.y - (dy1 / len1) * radius

                // 切出点：从curr沿next方向前进radius距离
                val exitX = curr.x + (dx2 / len2) * radius
                val exitY = curr.y + (dy2 / len2) * radius

                // 画直线到切入点
                lineChartPath.lineTo(enterX, enterY)
                // 用二次贝塞尔曲线从切入点到切出点，控制点为原始拐角点
                lineChartPath.quadTo(curr.x, curr.y, exitX, exitY)

                // 贝塞尔曲线 t=0.5 处的点即为圆滑后的实际坐标
                // Q(0.5) = 0.25*P0 + 0.5*P1 + 0.25*P2
                val smoothX = 0.25f * enterX + 0.5f * curr.x + 0.25f * exitX
                val smoothY = 0.25f * enterY + 0.5f * curr.y + 0.25f * exitY

                lineChartInfoList.add(
                    LineChartInfo(
                        PointF(smoothX, smoothY),
                        params.xAxisTouchRange,
                        params.points[i + dataOffset]
                    )
                )
            } else {
                // 最后一个点：直接连线，不做圆滑
                lineChartPath.lineTo(curr.x, curr.y)
                lineChartInfoList.add(
                    LineChartInfo(
                        PointF(curr.x, curr.y),
                        params.xAxisTouchRange,
                        params.points[i + dataOffset]
                    )
                )
            }
        }
    }

    private fun calcShaderPathIfNeed() {
        if (params.shader != null) {
            shaderPath = Path(lineChartPath)
            shaderPath.lineTo(
                width.toFloat(),
                lineChartInfoList[lineChartInfoList.size - 1].pointF.y
            )
            shaderPath.lineTo(
                width.toFloat(),
                0f
            )
            shaderPath.lineTo(
                0f,
                0f
            )
            shaderPath.close()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (params.points.isEmpty()) return
        //绘制分割线
        canvas.let {
            it.withSave {
                val isLtr = layoutDirection == LAYOUT_DIRECTION_LTR
                if (isLtr) {
                    canvas.translate(0f, height.toFloat())
                    canvas.scale(1f, -1f)
                } else {
                    canvas.translate(width.toFloat(), height.toFloat())
                    canvas.scale(-1f, -1f)
                }
                canvas.translate(params.chartLineWidth / 2.0f, params.chartLineWidth / 2.0f)
                if (params.shader != null) {
                    loadDrawLinePaintConfig()
                    paint.style = Paint.Style.FILL
                    paint.shader = params.shader
                    drawPath(shaderPath, paint)
                }
                loadDrawLinePaintConfig()
                drawPath(lineChartPath, paint)
                loadDrawCentralLinePaintConfig()
                //画分割线
                drawLine(
                    0f,
                    averageLineHeight + params.bottomSpaceHeight,
                    width.toFloat(),
                    averageLineHeight + params.bottomSpaceHeight,
                    paint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (params.points.isEmpty()) return true
        event?.let {
            val x = event.x
            val position = getPositionByX(x)
            if (position != -1 && lastSelectIndex != position) {
                lastSelectIndex = position
                invalidate()
                coordinator.onLineChartInfoSelected(
                    lastSelectIndex,
                    lineChartInfoList[lastSelectIndex]
                )
            }
        }
        return true
    }

    private fun getPositionByX(x: Float): Int {
        val xValue = if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            availableWidth - x
        } else {
            x
        }
        lineChartInfoList.forEachIndexed { index, lineChartInfo ->
            if (lineChartInfo.inArea(xValue)) {
                return index
            }
        }
        return -1
    }


    internal fun getXbyPosition(index: Int): Float {
        return lineChartInfoList[index].pointF.x + params.chartLineWidth / 2
    }

    internal fun getYbyPosition(index: Int): Float {
        return lineChartInfoList[index].pointF.y + params.chartLineWidth / 2
    }

    internal fun getDataInfoByIndex(index: Int): LineChartInfo? {
        return if (index < lineChartInfoList.size) {
            lineChartInfoList[index]
        } else {
            null
        }
    }

    internal interface InnerLineChartViewCoordinator {
        fun onLineChartInfoSelected(position: Int, info: LineChartInfo?)
    }
}