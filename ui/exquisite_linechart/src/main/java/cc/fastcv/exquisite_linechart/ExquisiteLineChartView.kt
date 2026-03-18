package cc.fastcv.exquisite_linechart

import android.content.Context
import android.graphics.CornerPathEffect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.FrameLayout
import java.util.Locale

class ExquisiteLineChartView : FrameLayout, LineChartCoordinatorCallback {
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs)
    }

    private lateinit var histogramView: ExquisiteLineChartViewExt

    companion object {
        private const val DEFAULT_EQUAL_PARTS_SIZE = 4
    }

    /**
     * 等分值
     */
    private var equalPartsSize = DEFAULT_EQUAL_PARTS_SIZE

    private lateinit var sidewaysBarController: SidewaysBarController
    private lateinit var bottomIndicatorBarController: BottomIndicatorBarController
    private lateinit var selectViewController: SelectViewController
    private lateinit var tipViewController: TipViewController
    private var adapter: TipAdapter = DefaultTipAdapter()

    private fun initView(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.ui_line_chart_view_ext_layout, this)
        initHistogramView()
    }

    private fun initHistogramView() {
        histogramView = findViewById(R.id.ui_histogram)
        histogramView.setLineChartSelectCallback(this)
        sidewaysBarController = SidewaysBarController(this)
        bottomIndicatorBarController = BottomIndicatorBarController(this)
        selectViewController = SelectViewController(this)
        tipViewController = TipViewController(this)
        setBottomSpaceHeight(10f)
    }

    override fun onLineChartSelect(position: Int, info: LineChartInfo?) {
        selectViewController.moveTo(position)
        tipViewController.moveTo(position)
    }

    override fun onLineChartCalc() {
        bottomIndicatorBarController.buildBottomIndicatorBar()
        selectViewController.rePosition()
        tipViewController.rePosition()
    }

    fun setEqualPartsSize(size: Int) {
        equalPartsSize = size
        histogramView.setEqualPartsSize(equalPartsSize)
        histogramView.refreshView()
        updateSidewaysBar()
    }

    fun setData(data: List<Pair<Int, Int>>) {
        Log.d("xcl_debug", "setData: $data")
        histogramView.setEqualPartsSize(equalPartsSize)
        histogramView.setData(data)
        updateSidewaysBar()
        val bottomIndicatorList = mutableListOf<Int>()
        for (i in 0..5) {
            bottomIndicatorList.add(histogramView.getMaxSecondValue() * i / 5)
        }
        bottomIndicatorBarController.setIndicatorList(bottomIndicatorList)
    }

    fun setTipAdapter(adapter: TipAdapter) {
        this.adapter = adapter
        tipViewController.rePosition()
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        super.setLayoutDirection(layoutDirection)
        histogramView.refreshView()
    }

    private fun updateSidewaysBar() {
        //获取计算之后的最大值
        val minValue = histogramView.getMinFirstValue()
        val maxValue = histogramView.getMaxFirstValue()
        val equalPartsList = mutableListOf<String>()
        for (i in 0..equalPartsSize) {
            equalPartsList.add(fixRangeText(minValue + (maxValue - minValue) * i / equalPartsSize))
        }
        sidewaysBarController.setSideWayBarTextList(equalPartsList)
    }

    private fun fixRangeText(value: Int): String {
        val minute = value / 60
        val second = value % 60
        return String.format(Locale.ENGLISH, "%d:%02d", minute, second)
    }

    fun setLineChartWidth(width: Float) {
        histogramView.setLineChartWidth(width)
    }

    fun setPathEffect(effect: Float) {
        histogramView.setPathEffect(effect)
    }


    fun setBottomSpaceHeight(height: Float) {
        histogramView.setBottomSpaceHeight(height)
        sidewaysBarController.setBottomSpaceHeight(height)
    }

    fun setSideBarWith(width: Int) {
        sidewaysBarController.setSideBarWith(width)
    }

    /**
     * 根据柱状图属性计算底部指示器的X轴偏移量
     */
    fun getRelativeX(index: Int): Float {
        val xbyPosition = histogramView.getXbyPosition(index)
        val sideBarWith = sidewaysBarController.getSideBarWith()
        return xbyPosition + sideBarWith
    }

    fun getRelativeHeight(index: Int): Float {
        val ybyPosition = histogramView.getYbyPosition(index)
        return histogramView.height - ybyPosition + TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            context.resources.displayMetrics
        )
    }

    fun getTipAdapter(): TipAdapter {
        return adapter
    }

    fun getDataInfoByIndex(index: Int): LineChartInfo? {
        return histogramView.getDataInfoByIndex(index)
    }

    internal fun getSideBarWith() = sidewaysBarController.getSideBarWith()

    internal fun getHistogramViewWith() = histogramView.width
}