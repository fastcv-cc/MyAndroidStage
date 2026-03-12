package cc.fastcv.lib_utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Calendar.getYearExt(): Int {
    return this[Calendar.YEAR]
}

fun Calendar.getMonthExt(): Int {
    return this[Calendar.MONTH]
}

fun Calendar.getDayExt(): Int {
    return this[Calendar.DAY_OF_MONTH]
}

fun Calendar.getHourExt(): Int {
    return this[Calendar.HOUR_OF_DAY]
}

fun Calendar.getMinuteExt(): Int {
    return this[Calendar.MINUTE] + 1
}

fun Calendar.getSecondExt(): Int {
    return this[Calendar.SECOND]
}

fun Calendar.isAMExt(): Boolean {
    return this[Calendar.AM_PM] == Calendar.AM
}

/**
 * 1-7 对应周一到周日
 */
fun Calendar.getWeekIndexExt(): Int {
    val index = this[Calendar.DAY_OF_WEEK] - 1
    return if (index < 7) {
        index
    } else {
        7
    }
}

fun Calendar.formatExt(formatStr: String): String {
    val format = SimpleDateFormat(formatStr, Locale.US).format(this.time)
    return format.format(Date())
}

/**
 * 是否是同一天
 */
fun Calendar.isSameDayExt(target: Calendar): Boolean {
    return this.getYearExt() == target.getYearExt()
            && this.getMonthExt() == target.getMonthExt()
            && this.getDayExt() == target.getDayExt()
}