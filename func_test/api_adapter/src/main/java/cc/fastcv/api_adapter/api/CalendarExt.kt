package cc.fastcv.api_adapter.api

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Calendar.getYear(): Int {
    return this[Calendar.YEAR]
}

fun Calendar.getMonth(): Int {
    return this[Calendar.MONTH]
}

fun Calendar.getDay(): Int {
    return this[Calendar.DAY_OF_MONTH]
}

fun Calendar.getHour(): Int {
    return this[Calendar.HOUR_OF_DAY]
}

fun Calendar.getMinute(): Int {
    return this[Calendar.MINUTE] + 1
}

fun Calendar.getSecond(): Int {
    return this[Calendar.SECOND]
}

fun Calendar.isAM(): Boolean {
    return this[Calendar.AM_PM] == Calendar.AM
}

/**
 * 1-7 对应周一到周日
 */
fun Calendar.getWeekIndex(): Int {
    val index = this[Calendar.DAY_OF_WEEK] - 1
    return if (index < 7) {
        index
    } else {
        7
    }
}

fun Calendar.format(formatStr: String): String {
    val format = SimpleDateFormat(formatStr, Locale.US).format(this.time)
    return format.format(Date())
}

/**
 * 是否是同一天
 */
fun Calendar.isSameDay(target: Calendar): Boolean {
    return this.getYear() == target.getYear()
            && this.getMonth() == target.getMonth()
            && this.getDay() == target.getDay()
}