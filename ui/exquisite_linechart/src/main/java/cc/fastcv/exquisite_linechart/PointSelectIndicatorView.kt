package cc.fastcv.exquisite_linechart

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isGone
import kotlin.math.roundToInt

internal class PointSelectIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.layou_point_select_indicator, this)
    }

    private lateinit var coordinator: PointSelectIndicatorViewCoordinator
    private lateinit var params: ExquisiteLineChartParams

    private var lastIndex = -1

    internal fun bindView(
        params: ExquisiteLineChartParams,
        coordinator: PointSelectIndicatorViewCoordinator
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

        val relativeX = coordinator.getRelativeX(index)
        val relativeHeight =
            coordinator.getRelativeHeight(index).roundToInt()
        val marginStart = relativeX - width / 2
        val layoutParams = layoutParams as LayoutParams
        layoutParams.marginStart = marginStart.toInt()
        layoutParams.height = relativeHeight
        this.layoutParams = layoutParams
    }

    interface PointSelectIndicatorViewCoordinator {
        fun getRelativeX(index: Int): Float
        fun getRelativeHeight(index: Int): Float
    }

}