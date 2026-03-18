package cc.fastcv.exquisite_linechart

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isGone
import kotlin.math.roundToInt

internal class SelectViewController(private val extView: ExquisiteLineChartView) {
    private var llSelect: LinearLayout = extView.findViewById(R.id.ll_select)
    private var llSelectWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        3f,
        extView.context.resources.displayMetrics
    ).toInt()

    private var lastIndex = -1

    fun moveTo(index:Int) {
        lastIndex = index
        if (index == -1) {
            lastIndex = -1
            llSelect.visibility = View.GONE
            return
        }

        if (llSelect.isGone) {
            llSelect.visibility = View.VISIBLE
        }

        val positionX = extView.getRelativeX(index)
        val relativeHeight = extView.getRelativeHeight(index).roundToInt()
        val marginStart = positionX - llSelectWidth/2
        val layoutParams = llSelect.layoutParams as FrameLayout.LayoutParams
        layoutParams.marginStart = marginStart.toInt()
        layoutParams.height = relativeHeight
        llSelect.layoutParams = layoutParams
    }

    fun rePosition() {
        if (lastIndex == -1) {
            return
        }
        moveTo(lastIndex)
    }
}