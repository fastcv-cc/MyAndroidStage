package cc.fastcv.date_switch_view

import java.util.*

class DateInfo {

    var weekContent = Array(7) {
        DayInfoDetail()
    }


    class DayInfoDetail {
        var isSelected = false
        var isInvalid = false
        var year = 0
        var month = 0
        var day = 0
        var isToday = false
        override fun toString(): String {
            return "DayInfoDetail(isSelected=$isSelected, isToday=$isToday, isInvalid=$isInvalid, year=$year, month=$month, day=$day)"
        }

        fun getDateYmd(): String {
            return "$year-${String.format(Locale.ENGLISH,"%02d", month)}-${String.format(Locale.ENGLISH,"%02d", day)}"
        }

    }

}