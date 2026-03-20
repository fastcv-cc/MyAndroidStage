package cc.fastcv.exquisite_linechart

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class SidewaysBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var params: ExquisiteLineChartParams

    internal fun bindView(
        params: ExquisiteLineChartParams
    ) {
        this.params = params
    }

    internal fun refreshView() {
        buildSlideWayBar()
        invalidate()
    }

    private fun buildSlideWayBar() {
        updateSideBarWith()
        setPadding(0, 0, 0, params.bottomSpaceHeight.toInt())
        val totalSize = params.divideEquallyYAxisLabelList.size
        val spaceViewSize = totalSize - 1

        //清空所有视图
        removeAllViews()
        for (i in 0 until totalSize) {
            addTextView(params.divideEquallyYAxisLabelList[i])
            if (i < spaceViewSize) {
                addSpaceView()
            }
        }
    }

    private fun addSpaceView() {
        val space = View(context)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0)
        layoutParams.weight = 1f
        addView(space, layoutParams)
    }

    private fun addTextView(text: String) {
        val fontTextView = TextView(context).apply {
            textSize = params.yAxisLabelTextSize
            setTextColor(params.yAxisLabelTextColor)
        }
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        fontTextView.text = text
        fontTextView.gravity = Gravity.CENTER
        addView(fontTextView, layoutParams)
    }

    private fun updateSideBarWith() {
        val layoutParams = layoutParams
        layoutParams.width = params.sideWayBarWidth.toInt()
        this.layoutParams = layoutParams
    }
}