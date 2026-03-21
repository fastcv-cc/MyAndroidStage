package cc.fastcv.exquisite_segdiagram

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

/**
 * 以左下标为原点时的坐标值
 */
class SegDiagramInfo(
    val startPointF: PointF,
    val endPointF: PointF,
    val touchRange: Float,
    val value: SegDiagramData
) {

    fun drawSegDiagram(canvas: Canvas, paint: Paint) {
        canvas.drawLine(startPointF.x, startPointF.y, endPointF.x, endPointF.y, paint)
    }

    fun inArea(x: Float): Boolean {
        return x in (startPointF.x - touchRange)..(startPointF.x + touchRange)
    }

    override fun toString(): String {
        return "startPointF:$startPointF   --  endPointF:$endPointF  -- xAxisTouchRange:$touchRange  -- value:$value"
    }
}