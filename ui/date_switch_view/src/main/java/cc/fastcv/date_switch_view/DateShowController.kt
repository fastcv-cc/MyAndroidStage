package cc.fastcv.date_switch_view

import android.widget.TextView

internal class DateShowController(private val dateSwitchView: DateSwitchView) {

    private var tvDate: TextView = dateSwitchView.findViewById(R.id.tv_date)

    fun updateDateText(dayInfo: DateInfo.DayInfoDetail) {
        //更新日期文本
        tvDate.text = if (dayInfo.day != -1) {
            "${dateSwitchView.params.monthStrArrays[dayInfo.month - 1]} · ${dayInfo.year}"
        } else {
            "${dayInfo.year}"
        }
    }

}