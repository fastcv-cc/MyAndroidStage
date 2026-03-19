package cc.fastcv.exquisite_linechart

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import cc.fastcv.exquisite_linechart.adapter.TipAdapter

class ExquisiteLineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val lineChartParams = ExquisiteLineChartParams(context)

    //底部指示栏(x轴)
    private var bottomIndicatorBarView: BottomIndicatorBarView

    //折线图View
    private var lineChartView: InnerLineChartView

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
            override fun getInnerLineChartViewWidth(): Float {
                return this@ExquisiteLineChartView.lineChartView.width.toFloat()
            }

        }

    private val innerLineChartViewCoordinator =
        object : InnerLineChartView.InnerLineChartViewCoordinator {
            override fun onLineChartInfoSelected(
                position: Int,
                info: LineChartInfo?
            ) {
                pointSelectIndicatorView.moveTo(position)
                tipView.moveTo(position)
            }
        }

    private val pointSelectIndicatorViewCoordinator =
        object : PointSelectIndicatorView.PointSelectIndicatorViewCoordinator {
            override fun getRelativeX(index: Int): Float {
                return this@ExquisiteLineChartView.getRelativeX(index)
            }

            override fun getRelativeHeight(index: Int): Float {
                return this@ExquisiteLineChartView.getRelativeHeight(index)
            }
        }

    private val tipViewCoordinator = object : TipView.TipViewCoordinator {
        override fun getDataInfoByIndex(index: Int): LineChartInfo? {
            return lineChartView.getDataInfoByIndex(index)
        }

        override fun getExquisiteLineChartViewWidth(): Float {
            return this@ExquisiteLineChartView.width.toFloat()
        }

        override fun getRelativeX(index: Int): Float {
            return this@ExquisiteLineChartView.getRelativeX(index)
        }
    }

    init {
        inflate(context, R.layout.layout_exquisite_line_chart_view, this)
        bottomIndicatorBarView = findViewById(R.id.bottomIndicator)
        bottomIndicatorBarView.bindView(lineChartParams, bottomIndicatorBarViewCoordinator)

        lineChartView = findViewById(R.id.lineChartView)
        lineChartView.bindView(lineChartParams, innerLineChartViewCoordinator)

        pointSelectIndicatorView = findViewById(R.id.pointSelectIndicatorView)
        pointSelectIndicatorView.bindView(lineChartParams, pointSelectIndicatorViewCoordinator)

        sidewaysBarView = findViewById(R.id.sidewaysBarView)
        sidewaysBarView.bindView(lineChartParams)

        tipView = findViewById(R.id.tipView)
        tipView.bindView(lineChartParams, tipViewCoordinator)
    }


    //设置数据源
    fun setPoints(points: List<PointF>) {
        lineChartParams.points.clear()
        lineChartParams.points.addAll(points)
        refreshView()
    }

    fun setXAxisParams(labels: List<String>, min: Float, max: Float) {
        lineChartParams.divideEquallyXAxisLabelList.clear()
        lineChartParams.divideEquallyXAxisLabelList.addAll(labels)
        lineChartParams.xAxisMaxValue = max
        lineChartParams.xAxisMinValue = min
        refreshView()
    }

    fun setYAxisParams(labels: List<String>, min: Float, max: Float) {
        lineChartParams.divideEquallyYAxisLabelList.clear()
        lineChartParams.divideEquallyYAxisLabelList.addAll(labels)
        lineChartParams.yAxisMaxValue = max
        lineChartParams.yAxisMinValue = min
        refreshView()
    }

    fun setTipAdapter(adapter: TipAdapter) {
        lineChartParams.adapter = adapter
        tipView.refreshView()
    }

    fun setLineChartRadius(radius: Float) {
        lineChartParams.lineChartRadius = lineChartParams.dp2px(radius)
        refreshView()
    }

    fun setBottomSpaceHeight(height: Float) {
        lineChartParams.bottomSpaceHeight = lineChartParams.dp2px(height)
        refreshView()
    }

    fun setLineChartWidth(value: Float) {
        lineChartParams.chartLineWidth = lineChartParams.dp2px(value)
        refreshView()
    }

    fun setSideBarWith(value: Float) {
        lineChartParams.sideWayBarWidth = lineChartParams.dp2px(value)
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
            lineChartView.refreshView()
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
        val xbyPosition = lineChartView.getXbyPosition(index)
        return xbyPosition + lineChartParams.sideWayBarWidth
    }

    fun getRelativeHeight(index: Int): Float {
        val ybyPosition = lineChartView.getYbyPosition(index)
        return lineChartView.height - ybyPosition + TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            25f,
            context.resources.displayMetrics
        ) - 0.5f
    }
}