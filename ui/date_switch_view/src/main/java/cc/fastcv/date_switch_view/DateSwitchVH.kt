package cc.fastcv.date_switch_view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

internal class DateSwitchVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tv1: TextView = itemView.findViewById(R.id.tv_1)
    private var tv2: TextView = itemView.findViewById(R.id.tv_2)
    private var tv3: TextView = itemView.findViewById(R.id.tv_3)
    private var tv4: TextView = itemView.findViewById(R.id.tv_4)
    private var tv5: TextView = itemView.findViewById(R.id.tv_5)
    private var tv6: TextView = itemView.findViewById(R.id.tv_6)
    private var tv7: TextView = itemView.findViewById(R.id.tv_7)

    fun updateTextByState(
        index: Int,
        text: String,
        isSelect: Boolean,
        isInvalid: Boolean,
        isToday: Boolean
    ): TextView {
        val textView = getTextRef(index)
        textView.text = text

        if (isSelect) {
            textView.setTextColor("#F9F9F9".toColorInt())
            textView.setBackgroundResource(R.drawable.ui_shape_date_switch_selected_bg)
            return textView
        }

        if (isToday) {
            textView.setTextColor("#F9F9F9".toColorInt())
            textView.setBackgroundResource(R.drawable.ui_shape_date_switch_today_bg)
            return textView
        }

        if (isInvalid) {
            textView.setTextColor("#99003324".toColorInt())
            textView.background = null
            return textView
        }

        textView.setTextColor("#003324".toColorInt())
        textView.background = null
        return textView
    }

    private fun getTextRef(index: Int): TextView {
        return when (index) {
            0 -> tv1
            1 -> tv2
            2 -> tv3
            3 -> tv4
            4 -> tv5
            5 -> tv6
            else -> tv7
        }
    }

    fun setMarginIfNeed(weekTextMargin: Int) {
        if (weekTextMargin != -1) {
            updateLayoutParams(tv1, weekTextMargin)
            updateLayoutParams(tv2, weekTextMargin)
            updateLayoutParams(tv3, weekTextMargin)
            updateLayoutParams(tv4, weekTextMargin)
            updateLayoutParams(tv5, weekTextMargin)
            updateLayoutParams(tv6, weekTextMargin)
        }
    }

    private fun updateLayoutParams(view: View, marginEnd: Int) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = marginEnd
        view.layoutParams = layoutParams
    }
}