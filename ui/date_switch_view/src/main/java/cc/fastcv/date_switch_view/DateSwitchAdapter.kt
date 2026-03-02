package cc.fastcv.date_switch_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class DateSwitchAdapter(private val list: MutableList<DateInfo>) : RecyclerView.Adapter<DateSwitchVH>() {
    /**
     * 周文本/日期文本间距
     */
    private var weekTextMargin = -1

    private var lastDayInfo: DateInfo.DayInfoDetail? = null

    private var itemClickListener: OnDateItemClickListener<DateInfo.DayInfoDetail>? = null

    fun setWeekTextMargin(margin: Int) {
        weekTextMargin = margin
        notifyDataSetChanged()
    }

    fun updateDataList(list: MutableList<DateInfo>) {
        lastDayInfo = null
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnDateItemClickListener<DateInfo.DayInfoDetail>) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateSwitchVH {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.ui_data_switch_item, parent, false)
        return DateSwitchVH(inflate)
    }

    override fun onBindViewHolder(holder: DateSwitchVH, position: Int) {
        holder.setMarginIfNeed(weekTextMargin)
        list[position].weekContent.forEachIndexed { index, dayInfo ->
            val text = if (dayInfo.day != -1) {
                dayInfo.day.toString()
            } else {
                dayInfo.month.toString()
            }
            val textView =
                holder.updateTextByState(index, text, dayInfo.isSelected, dayInfo.isInvalid, dayInfo.isToday)


            if (dayInfo.isSelected && lastDayInfo == null) {
                lastDayInfo = dayInfo
                itemClickListener!!.onDateItemClick(dayInfo)
            }

            //无效状态
            if (!dayInfo.isInvalid) {
                textView.setOnClickListener {
                    callbackIfNeed(dayInfo)
                }
            } else {
                textView.setOnClickListener(null)
            }
        }
    }

    private fun callbackIfNeed(dayInfo: DateInfo.DayInfoDetail) {
        if (itemClickListener != null) {
            //取消上次的点击位置
            if (lastDayInfo != null) {
                lastDayInfo!!.isSelected = false
            }
            dayInfo.isSelected = true
            lastDayInfo = dayInfo
            itemClickListener!!.onDateItemClick(dayInfo)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
