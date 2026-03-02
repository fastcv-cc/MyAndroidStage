package cc.fastcv.date_switch_view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

internal class DateSwitchExtRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        return false
    }
}