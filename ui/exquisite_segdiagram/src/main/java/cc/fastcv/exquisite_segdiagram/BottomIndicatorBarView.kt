package cc.fastcv.exquisite_segdiagram

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.math.max

internal class BottomIndicatorBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var params: ExquisiteSegDiagramParams
    private lateinit var coordinator: BottomIndicatorBarViewCoordinator

    internal fun bindView(
        params: ExquisiteSegDiagramParams,
        coordinator: BottomIndicatorBarViewCoordinator
    ) {
        this.params = params
        this.coordinator = coordinator
    }

    internal fun refreshView() {
        buildBottomIndicatorBar()
        invalidate()
    }

    private fun buildBottomIndicatorBar() {
        //清空所有视图
        removeAllViews()
        for ((index, label) in params.divideEquallyXAxisLabelList.withIndex()) {
            val textWidth = params.dp2px(42f).toInt()
            val space = max(
                0f,
                (coordinator.getInnerSegDiagramViewWidth() - textWidth * 5 * 1.0f) / (params.divideEquallyXAxisLabelList.size - 1)
            )
            val marginStart =
                params.sideWayBarWidth + index * textWidth - textWidth / 2.0f + index * space
            addIndicatorView(
                marginStart,
                textWidth,
                label
            )
        }
    }

    private fun addIndicatorView(marginStart: Float, textWidth: Int, text: String) {
        val textView = TextView(context).apply {
            textSize = params.xAxisLabelTextSize
            setTextColor(params.xAxisLabelTextColor)
        }
        textView.text = text
        textView.textAlignment = TEXT_ALIGNMENT_CENTER
        val textLayoutParams = LayoutParams(
            textWidth,
            LayoutParams.WRAP_CONTENT

        )
        textLayoutParams.gravity = Gravity.CENTER_VERTICAL or Gravity.START
        textView.layoutParams = textLayoutParams
        textLayoutParams.marginStart = marginStart.toInt()
        addView(textView, textLayoutParams)
    }

    internal interface BottomIndicatorBarViewCoordinator {
        fun getInnerSegDiagramViewWidth(): Float
    }
}