package cc.fastcv.exquisite_segdiagram

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withSave

internal class InnerSeqDiagramView @JvmOverloads constructor(
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

    /**
     * 折线图信息
     */
    private val seqDiagramInfoList = mutableListOf<SegDiagramInfo>()

    /**
     * 选中项
     */
    private var lastSelectIndex = -1

    private lateinit var coordinator: InnerSeqDiagramViewCoordinator


    private lateinit var params: ExquisiteSegDiagramParams

    internal fun bindView(
        params: ExquisiteSegDiagramParams,
        coordinator: InnerSeqDiagramViewCoordinator
    ) {
        this.params = params
        this.coordinator = coordinator
    }

    internal fun refreshView() {
        calc()
        invalidate()
    }

    private fun loadDrawLinePaintConfig() {
        paint.strokeWidth = params.segDiagramLineWidth
        paint.color = params.segDiagramLineColor
        paint.style = Paint.Style.STROKE
        paint.shader = null
    }

    private fun loadDrawCentralLinePaintConfig() {
        paint.strokeWidth = params.centralLineWidth
        paint.color = params.centralLineColor
        paint.style = Paint.Style.FILL
        paint.shader = null
    }

    private fun calc() {
        if (params.sourceData.isEmpty()) return
        availableHeight = height - params.segDiagramLineWidth
        availableWidth = width - params.segDiagramLineWidth

        //x是从0开始的
        var averageXSpaceDistance =
            (availableWidth * 1.0f - params.sourceData.size * params.segDiagramLineWidth) / (params.sourceData.size - 1)

        if (averageXSpaceDistance <= 0f) {
            //点过于密集 自动适配
            while (true) {
                params.segDiagramLineWidth -= 0.2f
                averageXSpaceDistance =
                    (availableWidth * 1.0f - params.sourceData.size * params.segDiagramLineWidth) / (params.sourceData.size - 1)

                if (averageXSpaceDistance > 2.0f) {
                    break
                }
            }
        }

        //y是预留了从0开始的逻辑
        val averageYDistance =
            (availableHeight - params.bottomSpaceHeight) * 1.0f / (params.yAxisMaxValue - params.yAxisMinValue)
        if (lastSelectIndex >= params.sourceData.size) {
            lastSelectIndex = -1
        }
        calcAverageLineHeight(averageYDistance)
        calcSeqDiagramInfoList(averageXSpaceDistance, averageYDistance)
        coordinator.onSeqDiagramViewInfoSelected(lastSelectIndex, null)
    }

    private fun calcAverageLineHeight(averageYDistance: Float) {
        val avgValue = (params.sourceData.map { (it.max + it.min) / 2.0f }
            .sum() / params.sourceData.size)
        averageLineHeight = calcY(avgValue, averageYDistance)
    }

    private fun calcY(value: Float, averageYDistance: Float): Float {
        return params.bottomSpaceHeight + (value - params.yAxisMinValue) * averageYDistance
    }

    private fun calcSeqDiagramInfoList(
        averageXSpaceDistance: Float,
        averageYDistance: Float
    ) {
        seqDiagramInfoList.clear()
        for (i in 0 until params.sourceData.size) {
            val pointX = i * (averageXSpaceDistance + params.segDiagramLineWidth)
            val startPointY =
                calcY(params.sourceData[i].min, averageYDistance)
            val endPointY =
                calcY(params.sourceData[i].max, averageYDistance)

            seqDiagramInfoList.add(
                SegDiagramInfo(
                    PointF(pointX, startPointY),
                    PointF(pointX, endPointY),
                    params.xAxisTouchRange,
                    params.sourceData[i]
                )
            )
            if (lastSelectIndex == -1) {
                lastSelectIndex = i
            }
        }
        for (info in seqDiagramInfoList) {
            Log.d("xcl_debug", "calcSeqDiagramInfoList: info = $info")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (params.sourceData.isEmpty()) return
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
                canvas.translate(
                    params.segDiagramLineWidth / 2.0f,
                    params.segDiagramLineWidth / 2.0f
                )
                loadDrawLinePaintConfig()
                seqDiagramInfoList.forEach {
                    it.drawSegDiagram(canvas, paint)
                }
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
        if (params.sourceData.isEmpty()) return true
        event?.let {
            val x = event.x
            val position = getPositionByX(x)
            if (position != -1 && lastSelectIndex != position) {
                lastSelectIndex = position
                invalidate()
                coordinator.onSeqDiagramViewInfoSelected(
                    lastSelectIndex,
                    seqDiagramInfoList[lastSelectIndex]
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
        seqDiagramInfoList.forEachIndexed { index, seqDiagramInfo ->
            if (seqDiagramInfo.inArea(xValue)) {
                return index
            }
        }
        return -1
    }


    /**
     * 因为我们绘制原点是 （params.segDiagramLineWidth / 2，params.segDiagramLineWidth / 2），
     * 所以相对视图左下角/右下角来算的话，就要加上这个params.segDiagramLineWidth / 2
     */
    internal fun getXbyPosition(index: Int): Float {
        return seqDiagramInfoList[index].startPointF.x + params.segDiagramLineWidth / 2
    }

    /**
     * 因为我们绘制原点是 （params.segDiagramLineWidth / 2，params.segDiagramLineWidth / 2），
     * 所以相对视图左下角/右下角来算的话，就要加上这个params.segDiagramLineWidth / 2
     */
    internal fun getYbyPosition(index: Int): Float {
        return seqDiagramInfoList[index].endPointF.y + params.segDiagramLineWidth / 2
    }

    internal fun getDataInfoByIndex(index: Int): SegDiagramInfo? {
        return if (index < seqDiagramInfoList.size) {
            seqDiagramInfoList[index]
        } else {
            null
        }
    }

    internal interface InnerSeqDiagramViewCoordinator {
        fun onSeqDiagramViewInfoSelected(position: Int, info: SegDiagramInfo?)
    }
}