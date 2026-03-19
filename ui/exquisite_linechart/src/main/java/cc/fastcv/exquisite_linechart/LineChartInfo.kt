package cc.fastcv.exquisite_linechart

import android.graphics.PointF

/**
 * 以左下标为原点时的坐标值
 */
class LineChartInfo(
    val pointF: PointF,
    val touchRange: Float,
    val value: PointF
) {
    fun inArea(x: Float): Boolean {
        return x in (pointF.x - touchRange)..(pointF.x + touchRange)
    }

    override fun toString(): String {
        return "pointF:$pointF -- xAxisTouchRange:$touchRange  -- value:$value"
    }
}