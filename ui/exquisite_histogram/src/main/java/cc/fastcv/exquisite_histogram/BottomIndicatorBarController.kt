package cc.fastcv.exquisite_histogram

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

internal class BottomIndicatorBarController internal constructor(private val extView: ExquisiteHistogramView) {

    private var flBottomIndicator: FrameLayout = extView.findViewById(R.id.fl_bottom_indicator)

    /**
     * 底部指示栏显示的文字集合
     */
    private var bottomIndicatorList = mutableListOf<Pair<Int,String>>()

    fun setIndicatorList(list: List<Pair<Int,String>>) {
        bottomIndicatorList.clear()
        bottomIndicatorList.addAll(list)
        buildBottomIndicatorBar()
    }

    fun buildBottomIndicatorBar() {
        //清空所有视图
        flBottomIndicator.removeAllViews()
        for (mutableEntry in bottomIndicatorList) {
            val index = mutableEntry.first
            val positionX = extView.getRelativeX(index)

            addIndicatorView(positionX, mutableEntry.second)
        }
    }

    private fun addIndicatorView(marginStart: Float, text: String) {
        val ll = LinearLayout(extView.context)
        ll.orientation = LinearLayout.VERTICAL
        ll.gravity = Gravity.CENTER_HORIZONTAL
        val line = LineView(extView.context)
        val lineLayoutParams = LinearLayout.LayoutParams(
            dp2px(extView.context, 5f),
            0
        )
        lineLayoutParams.weight = 1f
        ll.addView(line, lineLayoutParams)


        val textView = TextView(extView.context)
        textView.text = text
        textView.gravity = Gravity.CENTER
        val textLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            dp2px(extView.context, 40f)
        )
        ll.addView(textView, textLayoutParams)
        val measureText = textView.paint.measureText(text)

        val llLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        llLayoutParams.marginStart = (marginStart - measureText/2).toInt()
        flBottomIndicator.addView(ll, llLayoutParams)

    }

    private fun dp2px(context: Context, value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value,
            context.resources.displayMetrics
        ).toInt()
    }

}