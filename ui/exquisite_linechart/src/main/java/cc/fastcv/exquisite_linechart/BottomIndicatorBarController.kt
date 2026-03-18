package cc.fastcv.exquisite_linechart

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import java.util.Locale
import kotlin.math.max

internal class BottomIndicatorBarController internal constructor(private val extView: ExquisiteLineChartView) {

    private var flBottomIndicator: FrameLayout = extView.findViewById(R.id.fl_bottom_indicator)

    /**
     * 底部指示栏显示的文字集合
     */
    private var bottomIndicatorList = mutableListOf<Int>()

    fun setIndicatorList(list: List<Int>) {
        bottomIndicatorList.clear()
        bottomIndicatorList.addAll(list)
//        buildBottomIndicatorBar()
    }

    fun buildBottomIndicatorBar() {
        flBottomIndicator.post {
            //清空所有视图
            flBottomIndicator.removeAllViews()
            for ((index, value) in bottomIndicatorList.withIndex()) {
                val minute = value / 60
                val second = value % 60
                val textWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    42f,
                    extView.context.resources.displayMetrics
                ).toInt()
                val space = max(
                    0f,
                    (extView.getHistogramViewWith() - textWidth * 5 * 1.0f) / 5.0f
                )
                val marginStart =
                    extView.getSideBarWith() + index * textWidth - textWidth / 2.0f + index * space
                addIndicatorView(
                    marginStart,
                    textWidth,
                    String.format(Locale.ENGLISH, "%d:%02d", minute, second)
                )
            }
        }
    }

    private fun addIndicatorView(marginStart: Float, textWidth: Int, text: String) {
        val textView = TextView(extView.context).apply {
            textSize = 10f
            setTextColor("#6F756B".toColorInt())
        }
        textView.text = text
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val textLayoutParams = FrameLayout.LayoutParams(
            textWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT

        )
        textLayoutParams.gravity = Gravity.CENTER_VERTICAL or Gravity.START
        textView.layoutParams = textLayoutParams
        textLayoutParams.marginStart = marginStart.toInt()
        flBottomIndicator.addView(textView, textLayoutParams)
    }
}