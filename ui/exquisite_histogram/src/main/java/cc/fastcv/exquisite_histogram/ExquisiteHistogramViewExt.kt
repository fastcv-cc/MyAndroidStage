package cc.fastcv.exquisite_histogram

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt
import androidx.core.graphics.toColorInt

internal class ExquisiteHistogramViewExt : View {
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

    companion object Companion {
        private const val DEFAULT_HISTOGRAM_WIDTH = 5.0f
    }


    /**
     * 视图总宽度
     */
    private var viewHeight = 0

    /**
     * 试图
     */
    private var viewWidth = 0

    /**
     * 每条柱子的宽度
     */
    private var histogramWidth = 10f

    /**
     * 柱状图画笔
     */
    private val histogramPaint = Paint()

    /**
     * 柱状图颜色
     */
    private var histogramColor = "#026543".toColorInt()

    /**
     * 柱状图颜色
     */
    private var histogramSelectColor = "#A3B352".toColorInt()

    /**
     * 柱状图间距
     */
    private var histogramMargin = 0f

    /**
     * 柱状图信息
     */
    private val histogramInfoList = mutableListOf<HistogramInfo>()

    /**
     * 数据源
     */
    private var data = mutableListOf<Float>()

    /**
     * 选中项
     */
    private var lastSelectIndex = -1

    /**
     * 选择回调
     */
    private var callback: HistogramCoordinatorCallback? = null

    /**
     * 等分值
     */
    private var equalPartsSize = 0


    private fun init(context: Context?) {
        context?.let {
            histogramWidth = dp2px(it, DEFAULT_HISTOGRAM_WIDTH)
        }
        histogramPaint.strokeWidth = histogramWidth
        histogramPaint.color = histogramColor
        histogramPaint.strokeCap = Paint.Cap.ROUND
    }

    fun setHistogramSelectCallback(callback: HistogramCoordinatorCallback) {
        this.callback = callback
    }

    fun setData(list: List<Float>) {
        data.clear()
        data.addAll(list)
        lastSelectIndex = -1
        calc()
        invalidate()
    }

    fun refreshView() {
        calc()
        invalidate()
    }

    fun setHistogramWidth(width: Float) {
        histogramWidth = dp2px(context, width)
        histogramPaint.strokeWidth = histogramWidth
        calc()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
        calc()
    }

    private fun calc() {
        if (data.isEmpty()) return
        val maxValue = getMaxValue()
        histogramInfoList.clear()
        histogramMargin = (viewWidth - data.size * histogramWidth) / (data.size - 1)
        val heightPercentage = (viewHeight - histogramWidth) * 1f / maxValue
        for (i in 0 until data.size) {
            if (data[i] <= 0) {
                histogramInfoList.add(HistogramInfo(PointF(0f, 0f), PointF(0f, 0f), -1f, data[i]).apply {
                    isInvalid = true
                })
                continue
            }
            val startX = histogramWidth / 2.0f + (histogramWidth + histogramMargin) * i
            val startY = -histogramWidth / 2.0f
            val endX = startX
            val endY = startY + (-heightPercentage * data[i])
            val histogramInfo = HistogramInfo(
                PointF(startX, startY),
                PointF(endX, endY),
                heightPercentage * data[i],
                data[i]
            )
            histogramInfoList.add(histogramInfo)

            if (lastSelectIndex == -1) {
                lastSelectIndex = i
            }
        }
        callback?.onHistogramSelect(lastSelectIndex,null)
        callback?.onHistogramCalc()
    }

    internal fun getMaxValue(): Int {
        //获取数据源中的最大值
        val max = data.maxOrNull()!!
        //根据数据最大值除以三 拿到等分值
        val temp = (max / equalPartsSize + 0.5f).roundToInt()

        return (temp * equalPartsSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isEmpty()) return
        //绘制分割线
        canvas.let {
            it.save()
            //将原点移到左下角

            val isLtr = layoutDirection == LAYOUT_DIRECTION_LTR
            if (isLtr) {
                it.translate(0F, viewHeight.toFloat())
            } else {
                it.translate(viewWidth.toFloat(), viewHeight.toFloat())
            }
            for (i in 0 until histogramInfoList.size) {
                if (histogramInfoList[i].isInvalid) continue
                histogramPaint.color = if (i == lastSelectIndex) {
                    histogramSelectColor
                } else {
                    histogramColor
                }
                histogramInfoList[i].drawLine(it, histogramPaint,isLtr)
            }
            it.restore()
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
                callback?.onHistogramSelect(lastSelectIndex,histogramInfoList[lastSelectIndex])
            }
        }
        return true
    }

    private fun getPositionByX(x: Float): Int {
        val xValue = if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            viewWidth - x
        } else {
            x
        }
        histogramInfoList.forEachIndexed { index, histogramInfo ->
            if (histogramInfo.inArea(xValue, histogramWidth / 2)) {
                return index
            }
        }
        return -1
    }

    private fun dp2px(context: Context, dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue,
            context.resources.displayMetrics
        )
    }

    fun setEqualPartsSize(size: Int) {
        equalPartsSize = size
    }

    fun getXbyPosition(index: Int) : Float {
        return histogramWidth / 2.0f + (histogramWidth + histogramMargin) * index
    }

    fun getMargin() : Float {
        return histogramMargin
    }

    fun getDataInfoByIndex(index: Int) : HistogramInfo? {
        return if (index < histogramInfoList.size) {
            histogramInfoList[index]
        } else {
            null
        }

    }

}