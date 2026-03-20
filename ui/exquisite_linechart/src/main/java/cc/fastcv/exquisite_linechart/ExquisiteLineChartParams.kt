package cc.fastcv.exquisite_linechart

import android.content.Context
import android.graphics.PointF
import android.graphics.Shader
import android.util.TypedValue
import androidx.core.graphics.toColorInt
import cc.fastcv.exquisite_linechart.adapter.DefaultTipAdapter
import cc.fastcv.exquisite_linechart.adapter.TipAdapter

internal class ExquisiteLineChartParams(val context: Context) {

    //数据源 x,y
    internal val points = mutableListOf<PointF>()

    /**
     * x轴所需参数
     */
    //等分x轴标签集合
    internal val divideEquallyXAxisLabelList = mutableListOf<String>()

    //最大最小值用于绘图
    internal var xAxisMaxValue = 0f

    internal var xAxisMinValue = 0f

    internal var xAxisLabelTextColor = "#6F756B".toColorInt()

    internal var xAxisLabelTextSize = 10f

    /**
     * y轴所需参数
     */
    //等分y轴标签集合
    internal val divideEquallyYAxisLabelList = mutableListOf<String>()

    //最大最小值用于绘图
    internal var yAxisMaxValue = 0f

    internal var yAxisMinValue = 0f

    internal var yAxisLabelTextColor = "#6F756B".toColorInt()

    internal var yAxisLabelTextSize = 10f

    /**
     * 折线图所需参数
     */
    //折线图圆滑化圆角半径
    internal var lineChartRadius = dp2px(10f)

    //底部预留高度
    internal var bottomSpaceHeight = dp2px(10f)

    //折线图线宽
    internal var chartLineWidth = dp2px(5f)

    //折线颜色
    internal var chartLineColor = "#026543".toColorInt()

    //中间线线宽
    internal var centralLineWidth = dp2px(1f)

    //中间线颜色
    internal var centralLineColor = "#C9CDC6".toColorInt()

    //选择点x轴扩充范围
    internal var xAxisTouchRange = 30f

    //折线图内容渐变
    internal var shader: Shader? = null

    //侧边栏宽度
    internal var sideWayBarWidth = dp2px(60f)

    internal var adapter: TipAdapter = DefaultTipAdapter()

    internal var addZeroPoint = false

    internal var yAxisIncrement = false


    internal fun dp2px(dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        )
    }

}