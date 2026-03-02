package cc.fastcv.date_switch_view

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

internal class CardLinearSnapHelper : LinearSnapHelper() {

    var mNoNeedToScroll : Boolean = false

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        return if (mNoNeedToScroll) {
            intArrayOf(0, 0)
        } else {
            super.calculateDistanceToFinalSnap(layoutManager, targetView)
        }
    }
}