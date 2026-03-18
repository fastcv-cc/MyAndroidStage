package cc.fastcv.exquisite_linechart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withSave
import kotlin.math.sqrt

internal class ExquisiteLineChartViewExt : View {
    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    //绘图可用高度
    private var availableHeight = 0f

    //绘图可用宽度
    private var availableWidth = 0f

    //底部预留高度
    private var bottomSpaceHeight = 0f

    //画笔
    private val paint = Paint()

    //转角pathEffect
    private var pathEffect = 0f

    //平均线高度
    private var averageLineHeight = 0f

    private var lineChartPath = Path()
    private var shaderPath = Path()
    private var lineWidth = 0f

    /**
     * 折线图信息
     */
    private val lineChartInfoList = mutableListOf<LineChartInfo>()

    /**
     * 数据源
     */
    private var data = mutableListOf<Pair<Int, Int>>()

    /**
     * 选中项
     */
    private var lastSelectIndex = -1

    /**
     * 选择回调
     */
    private var callback: LineChartCoordinatorCallback? = null

    /**
     * 等分值
     */
    private var equalPartsSize = 0

    private var shader = LinearGradient(
        0f, 0f,
        0f, height.toFloat(),
        "#330000FF".toColorInt(),
        Color.TRANSPARENT,
        Shader.TileMode.CLAMP
    )


    private fun init(context: Context?) {
        context?.let {
            lineWidth = dp2px(context, 5f)
            bottomSpaceHeight = dp2px(context, 10f)
            pathEffect = dp2px(context, 10f)
        }
    }

    fun loadDrawLinePaintConfig() {
        paint.strokeWidth = lineWidth
        paint.color = "#026543".toColorInt()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.pathEffect = CornerPathEffect(pathEffect)
        paint.shader = null
    }

    fun loadDrawCentralLinePaintConfig() {
        paint.strokeWidth = dp2px(context, 1f)
        paint.color = "#C9CDC6".toColorInt()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.pathEffect = null
        paint.shader = null
    }

    fun loadDrawShaderPaintConfig() {
        paint.strokeWidth = lineWidth
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.pathEffect = CornerPathEffect(pathEffect)
        paint.shader = shader
    }

    fun setLineChartSelectCallback(callback: LineChartCoordinatorCallback) {
        this.callback = callback
    }

    fun setData(list: List<Pair<Int, Int>>) {
        data.clear()
        data.addAll(list)
        lastSelectIndex = -1
        if (width > 0) {
            calc()
            invalidate()
        }
    }

    fun refreshView() {
        calc()
        invalidate()
    }

    fun setLineChartWidth(value: Float) {
        lineWidth = dp2px(context, value)
        availableHeight = height - lineWidth
        availableWidth = width - lineWidth
        calc()
        invalidate()
    }

    fun setPathEffect(effect: Float) {
        pathEffect = dp2px(context, effect)
        invalidate()
    }

    fun getPathEffect() = pathEffect

    fun setBottomSpaceHeight(height: Float) {
        bottomSpaceHeight = dp2px(context, height)
        calc()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        availableHeight = h - lineWidth
        availableWidth = w - lineWidth
        shader = LinearGradient(
            0f, 0f,
            0f, height.toFloat(),
            "#330000FF".toColorInt(),
            Color.TRANSPARENT,
            Shader.TileMode.CLAMP
        )
        calc()
    }

    private fun calc() {
        if (data.isEmpty()) return
        lineChartInfoList.clear()
        lineChartInfoList.add(LineChartInfo(PointF(0f, 0f), 20f, 10f, Pair(0, 0)))
        val averageXDistance = availableWidth * 1.0f / data.size
        val averageYDistance =
            (availableHeight - bottomSpaceHeight) * 1.0f / (getMaxFirstValue() - getMinFirstValue())
        var totalValue = 0
        for (i in 0 until data.size) {
            val pointX = (i + 1) * averageXDistance
            val pointY = availableHeight - (getMaxFirstValue() - data[i].first) * averageYDistance
            val lineChartInfo = LineChartInfo(
                PointF(pointX, pointY),
                20f,
                10f,
                data[i]
            )
            lineChartInfoList.add(lineChartInfo)

            totalValue += data[i].first

            if (lastSelectIndex == -1) {
                lastSelectIndex = i
            }
        }
        averageLineHeight =
            availableHeight - (getMaxFirstValue() - (totalValue / data.size)) * averageYDistance
        lineChartPath.reset()
        shaderPath.reset()
        lineChartPath.moveTo(
            lineChartInfoList[0].pointF.x,
            lineChartInfoList[0].pointF.y
        )
        shaderPath.moveTo(0f, 0f)
        for (i in 1 until lineChartInfoList.size) {
            lineChartPath.lineTo(
                lineChartInfoList[i].pointF.x,
                lineChartInfoList[i].pointF.y
            )
            shaderPath.lineTo(
                lineChartInfoList[i].pointF.x,
                lineChartInfoList[i].pointF.y
            )
        }
        shaderPath.lineTo(
            width.toFloat(),
            lineChartInfoList[lineChartInfoList.size - 1].pointF.y
        )
        shaderPath.lineTo(
            width.toFloat(),
            -lineWidth / 2.0f
        )
        shaderPath.lineTo(
            0f,
            -lineWidth / 2.0f
        )
        shaderPath.close()
        callback?.onLineChartSelect(lastSelectIndex, null)
        callback?.onLineChartCalc()
    }

    internal fun getMaxSecondValue(): Int {
        return data.map {
            it.second
        }.maxOf { it }
    }

    internal fun getMinSecondValue(): Int {
        return data.map {
            it.second
        }.minOf { it }
    }

    internal fun getMaxFirstValue(): Int {
        return data.map {
            it.first
        }.maxOf { it }
    }

    internal fun getMinFirstValue(): Int {
        return data.map {
            it.first
        }.minOf { it }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isEmpty()) return
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
                canvas.translate(lineWidth / 2.0f, lineWidth / 2.0f)
                loadDrawLinePaintConfig()
                drawPath(lineChartPath, paint)
                loadDrawShaderPaintConfig()
                drawPath(shaderPath, paint)
                loadDrawLinePaintConfig()
                loadDrawCentralLinePaintConfig()
                //画分割线
                drawLine(
                    0f,
                    averageLineHeight + bottomSpaceHeight,
                    width.toFloat(),
                    averageLineHeight + bottomSpaceHeight,
                    paint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (data.isEmpty()) return true
        event?.let {
            val x = event.x
            val position = getPositionByX(x)
            if (position != -1 && lastSelectIndex != position) {
                lastSelectIndex = position
                invalidate()
                callback?.onLineChartSelect(lastSelectIndex, lineChartInfoList[lastSelectIndex])
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

    private fun dp2px(context: Context, dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        )
    }

    fun setEqualPartsSize(size: Int) {
        equalPartsSize = size
    }

    fun getXbyPosition(index: Int): Float {
        return lineChartInfoList[index].pointF.x
    }

    fun getYbyPosition(index: Int): Float {
        return lineChartInfoList[index].pointF.y
    }

    fun getDataInfoByIndex(index: Int): LineChartInfo? {
        return if (index < lineChartInfoList.size) {
            lineChartInfoList[index]
        } else {
            null
        }

    }

}