package cc.fastcv.exquisite_histogram

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

/**
 * 以左下标为原点时的坐标值
 */
class HistogramInfo(
    private val startPointF: PointF,
    private val endPointF: PointF,
    val height:Float,
    val value:Float
) {

    var isInvalid: Boolean = false

    fun drawLine(canvas: Canvas, paint: Paint, isLtr :Boolean) {
        if (isInvalid) return
        val faction = if (isLtr) {
            1
        } else {
            -1
        }
        canvas.drawLine(startPointF.x*faction, startPointF.y, endPointF.x*faction, endPointF.y, paint)
    }

    fun inArea(x: Float, range: Float): Boolean {
        if (isInvalid) {
            return false
        }
        if (height < 0f) {
            return false
        }
        return  x in (startPointF.x - range) .. (startPointF.x + range)
    }

    override fun toString(): String {
        return "startPointF:$startPointF   --  endPointF:$endPointF  -- height:$height  -- isInvalid:$isInvalid"
    }
}