package cc.fastcv.exquisite_histogram

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isGone

internal class SelectViewController(private val extView: ExquisiteHistogramView) {
    private var llSelect: LinearLayout = extView.findViewById(R.id.ll_select)
    private var llSelectWidth = dp2px(extView.context,3)

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
        val marginStart = positionX - llSelectWidth/2
        val layoutParams = llSelect.layoutParams as FrameLayout.LayoutParams
        layoutParams.marginStart = marginStart.toInt()
        llSelect.layoutParams = layoutParams
    }

    fun rePosition() {
        if (lastIndex == -1) {
            return
        }
        moveTo(lastIndex)
    }

    private fun dp2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}