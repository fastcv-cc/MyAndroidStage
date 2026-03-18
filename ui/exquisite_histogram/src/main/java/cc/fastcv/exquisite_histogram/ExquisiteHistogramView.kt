package cc.fastcv.exquisite_histogram

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import java.util.Locale

class ExquisiteHistogramView : FrameLayout, HistogramCoordinatorCallback {
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

    private lateinit var histogramView: ExquisiteHistogramViewExt

    companion object {
        private const val DEFAULT_EQUAL_PARTS_SIZE = 3
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
        inflate(context, R.layout.ui_histogram_view_ext_layout, this)
        initHistogramView()
    }

    private fun initHistogramView() {
        histogramView = findViewById(R.id.ui_histogram)
        histogramView.setHistogramSelectCallback(this)
        sidewaysBarController = SidewaysBarController(this)
        bottomIndicatorBarController = BottomIndicatorBarController(this)
        selectViewController = SelectViewController(this)
        tipViewController = TipViewController(this)
    }

    override fun onHistogramSelect(position: Int, info: HistogramInfo?) {
        selectViewController.moveTo(position)
        tipViewController.moveTo(position)
    }

    override fun onHistogramCalc() {
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

    fun setData(list: List<Float>, map: List<Pair<Int, String>>) {
        histogramView.setEqualPartsSize(equalPartsSize)
        histogramView.setData(list)
        updateSidewaysBar()
        bottomIndicatorBarController.setIndicatorList(map)
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
        val maxValue = histogramView.getMaxValue()
        val equalPartsList = mutableListOf<String>()
        for (i in equalPartsSize downTo 0) {
            equalPartsList.add(fixRangeText(maxValue * i / equalPartsSize))
        }
        sidewaysBarController.setSideWayBarTextList(equalPartsList)
    }

    private fun fixRangeText(value: Int): String {
        return if (value >= 1000) {
            String.format(Locale.ENGLISH, "%.1fk", value / 1000f)
        } else {
            "$value"
        }
    }

    fun setHistogramWidth(width: Float) {
        histogramView.setHistogramWidth(width)
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

    fun getTipAdapter(): TipAdapter {
        return adapter
    }

    fun getDataInfoByIndex(index: Int): HistogramInfo? {
        return histogramView.getDataInfoByIndex(index)
    }
}