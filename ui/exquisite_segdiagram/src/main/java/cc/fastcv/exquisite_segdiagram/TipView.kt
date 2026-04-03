package cc.fastcv.exquisite_segdiagram

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isGone
import kotlin.math.max

internal class TipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private lateinit var params: ExquisiteSegDiagramParams
    private lateinit var coordinator: TipViewCoordinator

    private var tvTop: TextView
    private var tvBottom: TextView

    private var lastIndex = -1

    init {
        inflate(context, R.layout.exquisite_seg_diagram_layout_tip_view, this)
        tvTop = findViewById(R.id.tv_top)
        tvBottom = findViewById(R.id.tv_bottom)
    }


    internal fun bindView(
        params: ExquisiteSegDiagramParams,
        coordinator: TipViewCoordinator
    ) {
        this.params = params
        this.coordinator = coordinator
    }

    internal fun refreshView() {
        post {
            moveTo(lastIndex)
        }
    }

    internal fun moveTo(index: Int) {
        lastIndex = index

        if (index == -1) {
            lastIndex = -1
            visibility = GONE
            return
        }

        if (isGone) {
            visibility = VISIBLE
        }

        val maxMargin = coordinator.getExquisiteSeqDiagramViewWidth() * 1f - width

        val marginStart = if (coordinator.getRelativeX(index) > maxMargin + width / 2) {
            maxMargin
        } else {
            max(coordinator.getRelativeX(index) - width / 2f, 0f)
        }

        val layoutParams = layoutParams as LayoutParams
        layoutParams.marginStart = marginStart.toInt()
        this.layoutParams = layoutParams
        showTip()
    }

    private fun showTip() {
        if (lastIndex == -1) {
            return
        }

        val info = coordinator.getDataInfoByIndex(lastIndex)
        info?.let {
            val adapter = params.adapter
            tvTop.text = adapter.getTopText(lastIndex, it)
            tvBottom.text = adapter.getBottomText(lastIndex, it)
            val bgColor = adapter.getTipBackgroundColor(lastIndex, it)
            if (bgColor != 0) {
                val bg = background
                if (bg is GradientDrawable) {
                    bg.setColor(bgColor)
                }
            }
        }
    }

    internal interface TipViewCoordinator {
        fun getDataInfoByIndex(index: Int): SegDiagramInfo?

        fun getExquisiteSeqDiagramViewWidth(): Float

        fun getRelativeX(index: Int): Float
    }

}