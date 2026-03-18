package cc.fastcv.exquisite_linechart

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt

internal class SidewaysBarController(private val extView: ExquisiteLineChartView) {

    private var llSideWayBar: LinearLayout = extView.findViewById(R.id.ll_side_way_bar)
    private var sideWayBarWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        60f,
        extView.context.resources.displayMetrics
    )

    /**
     * 侧边栏显示的文字集合
     */
    private var sideWayBarTextList = mutableListOf<String>()

    fun setBottomSpaceHeight(value: Float) {
        val bottomSpaceHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            extView.context.resources.displayMetrics
        )
        llSideWayBar.setPadding(0, 0, 0, bottomSpaceHeight.toInt())
    }

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
        val fontTextView = TextView(extView.context).apply {
            textSize = 10f
            setTextColor("#6F756B".toColorInt())
        }
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        fontTextView.text = text
        fontTextView.gravity = Gravity.CENTER
        llSideWayBar.addView(fontTextView, layoutParams)
    }

    fun setSideBarWith(width: Int) {
        sideWayBarWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            width * 1.0f,
            llSideWayBar.context.resources.displayMetrics
        )
        val layoutParams = llSideWayBar.layoutParams
        layoutParams.width = sideWayBarWidth.toInt()
        llSideWayBar.layoutParams = layoutParams
    }

    fun getSideBarWith() = sideWayBarWidth
}