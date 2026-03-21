package cc.fastcv.exquisite_segdiagram

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import cc.fastcv.exquisite_segdiagram.adapter.TipAdapter

class ExquisiteSegDiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val seqDiagramParams = ExquisiteSegDiagramParams(context)

    //底部指示栏(x轴)
    private var bottomIndicatorBarView: BottomIndicatorBarView

    //折线图View
    private var innerSeqDiagramView: InnerSeqDiagramView

    //选中效果View
    private var pointSelectIndicatorView: PointSelectIndicatorView

    //侧边指示栏(y轴)
    private var sidewaysBarView: SidewaysBarView

    //提示View
    private var tipView: TipView

    //是否已经触发了刷新
    private var isRefreshing = false

    private val bottomIndicatorBarViewCoordinator =
        object : BottomIndicatorBarView.BottomIndicatorBarViewCoordinator {
            override fun getInnerSegDiagramViewWidth(): Float {
                return this@ExquisiteSegDiagramView.innerSeqDiagramView.width.toFloat()
            }

        }

    private val innerSeqDiagramViewCoordinator =
        object : InnerSeqDiagramView.InnerSeqDiagramViewCoordinator {
            override fun onSeqDiagramViewInfoSelected(
                position: Int,
                info: SegDiagramInfo?
            ) {
                pointSelectIndicatorView.moveTo(position)
                tipView.moveTo(position)
            }
        }

    private val pointSelectIndicatorViewCoordinator =
        object : PointSelectIndicatorView.PointSelectIndicatorViewCoordinator {
            override fun getRelativeX(index: Int): Float {
                return this@ExquisiteSegDiagramView.getRelativeX(index)
            }

            override fun getRelativeHeight(index: Int): Float {
                return this@ExquisiteSegDiagramView.getRelativeHeight(index)
            }
        }

    private val tipViewCoordinator = object : TipView.TipViewCoordinator {
        override fun getDataInfoByIndex(index: Int): SegDiagramInfo? {
            return innerSeqDiagramView.getDataInfoByIndex(index)
        }

        override fun getExquisiteSeqDiagramViewWidth(): Float {
            return this@ExquisiteSegDiagramView.width.toFloat()
        }

        override fun getRelativeX(index: Int): Float {
            return this@ExquisiteSegDiagramView.getRelativeX(index)
        }
    }

    init {
        inflate(context, R.layout.exquisite_seg_diagram_layout_view, this)
        bottomIndicatorBarView = findViewById(R.id.bottomIndicator)
        bottomIndicatorBarView.bindView(seqDiagramParams, bottomIndicatorBarViewCoordinator)

        innerSeqDiagramView = findViewById(R.id.innerSeqDiagramView)
        innerSeqDiagramView.bindView(seqDiagramParams, innerSeqDiagramViewCoordinator)

        pointSelectIndicatorView = findViewById(R.id.pointSelectIndicatorView)
        pointSelectIndicatorView.bindView(seqDiagramParams, pointSelectIndicatorViewCoordinator)

        sidewaysBarView = findViewById(R.id.sidewaysBarView)
        sidewaysBarView.bindView(seqDiagramParams)

        tipView = findViewById(R.id.tipView)
        tipView.bindView(seqDiagramParams, tipViewCoordinator)
    }


    //设置数据源
    fun setPoints(data: List<SegDiagramData>) {
        seqDiagramParams.sourceData.clear()
        seqDiagramParams.sourceData.addAll(data)
        refreshView()
    }

    fun setXAxisParams(labels: List<String>, min: Float, max: Float) {
        seqDiagramParams.divideEquallyXAxisLabelList.clear()
        seqDiagramParams.divideEquallyXAxisLabelList.addAll(labels)
        seqDiagramParams.xAxisMaxValue = max
        seqDiagramParams.xAxisMinValue = min
        refreshView()
    }

    fun setYAxisParams(labels: List<String>, min: Float, max: Float) {
        seqDiagramParams.divideEquallyYAxisLabelList.clear()
        seqDiagramParams.divideEquallyYAxisLabelList.addAll(labels)
        seqDiagramParams.yAxisMaxValue = max
        seqDiagramParams.yAxisMinValue = min
        refreshView()
    }

    fun setTipAdapter(adapter: TipAdapter) {
        seqDiagramParams.adapter = adapter
        tipView.refreshView()
    }

    fun setBottomSpaceHeight(height: Float) {
        seqDiagramParams.bottomSpaceHeight = seqDiagramParams.dp2px(height)
        refreshView()
    }

    fun setDiagramLineWidth(value: Float) {
        seqDiagramParams.segDiagramLineWidth = seqDiagramParams.dp2px(value)
        refreshView()
    }

    fun setSideBarWith(value: Float) {
        seqDiagramParams.sideWayBarWidth = seqDiagramParams.dp2px(value)
        refreshView()
    }

    fun setChartLineColor(color: Int) {
        seqDiagramParams.segDiagramLineColor = color
        refreshView()
    }

    fun setCentralLineWidth(value: Float) {
        seqDiagramParams.centralLineWidth = seqDiagramParams.dp2px(value)
        refreshView()
    }

    fun setCentralLineColor(color: Int) {
        seqDiagramParams.centralLineColor = color
        refreshView()
    }

    fun setXAxisTouchRange(range: Float) {
        seqDiagramParams.xAxisTouchRange = range
        refreshView()
    }

    fun setXAxisLabelTextColor(color: Int) {
        seqDiagramParams.xAxisLabelTextColor = color
        refreshView()
    }

    fun setXAxisLabelTextSize(size: Float) {
        seqDiagramParams.xAxisLabelTextSize = size
        refreshView()
    }

    fun setYAxisLabelTextColor(color: Int) {
        seqDiagramParams.yAxisLabelTextColor = color
        refreshView()
    }

    fun setYAxisLabelTextSize(size: Float) {
        seqDiagramParams.yAxisLabelTextSize = size
        refreshView()
    }

    fun setAutoLineWidth(autoLineWidth: Boolean) {
        seqDiagramParams.autoLineWidth = autoLineWidth
        refreshView()
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        super.setLayoutDirection(layoutDirection)
        refreshView()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        refreshView()
    }


    private fun refreshView() {
        if (isRefreshing) {
            return
        }
        isRefreshing = true
        post {
            isRefreshing = false
            innerSeqDiagramView.refreshView()
            bottomIndicatorBarView.refreshView()
            pointSelectIndicatorView.refreshView()
            sidewaysBarView.refreshView()
            tipView.refreshView()
        }
    }

    /**
     * 根据柱状图属性计算底部指示器相对于整体View的X轴偏移量
     */
    internal fun getRelativeX(index: Int): Float {
        val xbyPosition = innerSeqDiagramView.getXbyPosition(index)
        return xbyPosition + seqDiagramParams.sideWayBarWidth
    }

    fun getRelativeHeight(index: Int): Float {
        val ybyPosition = innerSeqDiagramView.getYbyPosition(index)
        return innerSeqDiagramView.height - ybyPosition + seqDiagramParams.bottomSpaceHeight + seqDiagramParams.dp2px(19f)
    }
}