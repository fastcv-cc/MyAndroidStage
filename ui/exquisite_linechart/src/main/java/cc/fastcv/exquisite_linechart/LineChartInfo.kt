package cc.fastcv.exquisite_linechart

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

/**
 * 以左下标为原点时的坐标值
 */
class LineChartInfo(
    val pointF: PointF,
    val radius: Float,
    val radiusSpace: Float,
    val value: Pair<Int, Int>
) {

    fun drawPoint(canvas: Canvas, paint: Paint, isLtr: Boolean) {
        val faction = if (isLtr) {
            1
        } else {
            -1
        }
        canvas.drawCircle(pointF.x * faction, pointF.y, radius + radiusSpace, paint)
        canvas.drawCircle(pointF.x * faction, pointF.y, radius, paint)
    }

    fun inArea(x: Float): Boolean {
        return x in (pointF.x - (radius + radiusSpace))..(pointF.x + (radius + radiusSpace))
    }

    override fun toString(): String {
        return "pointF:$pointF -- radius:$radius  -- radiusSpace:$radiusSpace  -- value:$value"
    }
}