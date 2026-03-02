package cc.fastcv.exquisite_histogram

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

internal class SidewaysBarController(private val extView: ExquisiteHistogramView) {

    private var llSideWayBar: LinearLayout = extView.findViewById(R.id.ll_side_way_bar)
    private var sideWayBarWidth = dp2px(extView.context,60)

    /**
     * 侧边栏显示的文字集合
     */
    private var sideWayBarTextList = mutableListOf<String>()

    fun setSideWayBarTextList(list: List<String>) {
        sideWayBarTextList.clear()
        sideWayBarTextList.addAll(list)
        buildSlideWayBar()
    }

    private fun buildSlideWayBar() {
        val total = sideWayBarTextList.size
        val spaceViewSize = total - 1

        //清空所有视图
        llSideWayBar.removeAllViews()

        for (i in 0 until total) {
            addTextView(sideWayBarTextList[i], llSideWayBar)
            if (i < spaceViewSize) {
                addSpaceView(llSideWayBar)
            }
        }
    }

    private fun addSpaceView(llSideWayBar: LinearLayout) {
        val space = View(extView.context)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        layoutParams.weight = 1f
        llSideWayBar.addView(space, layoutParams)
    }

    private fun addTextView(text: String, llSideWayBar: LinearLayout) {
        val fontTextView = TextView(extView.context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        fontTextView.text = text
        fontTextView.setTextColor(Color.parseColor("#343434"))
        fontTextView.textSize = 12f
        fontTextView.gravity = Gravity.CENTER
        llSideWayBar.addView(fontTextView, layoutParams)
    }

    fun setSideBarWith(width: Int) {
        sideWayBarWidth = dp2px(llSideWayBar.context,width)
        val layoutParams = llSideWayBar.layoutParams
        layoutParams.width = sideWayBarWidth
        llSideWayBar.layoutParams = layoutParams
    }

    fun getSideBarWith() = sideWayBarWidth

    private fun dp2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

}