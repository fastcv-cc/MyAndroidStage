package cc.fastcv.api_adapter.api

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.math.abs

object ZonedDateTimeUtil {

    /**
     * 获取指定日期之前/之后amount天的日期
     * 如 amount = 2 currentDate_yMd = "2022-06-30"
     * 得到的结果就是  “2022-07-02”
     */
    fun getOffsetDateByDay(amount: Int, currentDateStr: String, format: SimpleDateFormat): String {
        val currentDate = format.parse(currentDateStr)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate!!
        calendar.add(Calendar.DAY_OF_MONTH, amount)
        return format.format(calendar.time)
    }


    /**
     * 转换时间格式
     * 如：dateTime  20220624145912    parseDateFormat  yyyyMMddHHmmss   formatDateFormat yyyy-MM-dd HH:mm:ss
     * 得到的结果为：2022-06-24 14：59：12
     * 如果传入的格式解析失败  则会返回 “”字符
     */
    fun convertDateTimeFormat(
        dateTime: String,
        parseDateFormat: SimpleDateFormat,
        formatDateFormat: SimpleDateFormat
    ): String {
        val parseDate = parseDateFormat.parse(dateTime)
        return if (parseDate != null) {
            return formatDateFormat.format(parseDate)
        } else {
            ""
        }
    }

    /**
     * 转换 日期时间字符 为 时间戳
     */
    fun convertDateTimeStrToTimestamp(
        dateTimeStr: String,
        simpleDateFormat: SimpleDateFormat
    ): Long {
        val dateTime = simpleDateFormat.parse(dateTimeStr)
        return dateTime?.time ?: 0L
    }


    /**
     * 转换 时间戳 为 日期时间字符
     */
    fun convertTimestampToDateTimeStr(timestamp: Long, simpleDateFormat: SimpleDateFormat): String =
        try {
            simpleDateFormat.format(Date(timestamp))
        } catch (e: ParseException) {
            ""
        }

    /**
     * 获取两个日期之间相差的天数
     */
    fun getDateDifferenceDay(startDate: Date, endDate: Date): Int {
        // 获取相差的天数
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val startTimeInMillis = calendar.timeInMillis

        calendar.time = endDate
        val endTimeInMillis = calendar.timeInMillis
        val betweenDays = (endTimeInMillis - startTimeInMillis) / (1000L * 3600L * 24L)
        return betweenDays.toInt()
    }

    /**
     * 获取指定日期之前/之后amount月的日期
     * 如 amount = 2 currentDate_yMd = "2022-06-30"
     * 得到的结果就是  “2022-08-30”
     */
    fun getOffsetDateByMonth(
        amount: Int,
        date: Date,
    ): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, amount)
        return calendar.time
    }

    /**
     * 获取两个日期之间相差的月
     */
    fun getDateDifferenceMonth(fromDate: Date, toDate: Date): Int {
        val from = Calendar.getInstance()
        from.time = fromDate
        val to = Calendar.getInstance()
        to.time = toDate
        //只要年月
        val fromYear = from[Calendar.YEAR]
        val fromMonth = from[Calendar.MONTH]
        val toYear = to[Calendar.YEAR]
        val toMonth = to[Calendar.MONTH]
        return toYear * 12 + toMonth - (fromYear * 12 + fromMonth)
    }

    /**
     * 根据年月得到当前月份最大多好号
     */
    fun getMaxDayByYearMonth(year: Int, month: Int): Int {
        var maxDay = 0
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            maxDay = 31
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            maxDay = 30
        }
        if (month == 2) {
            maxDay = if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                29
            } else {
                28
            }
        }
        return maxDay
    }


    /**
     * 可简化计算过程 为了方便理解 没有简化
     */
    fun getCurrentTimeZone(): Float {
        val now = Date()
        val zone = TimeZone.getDefault()
        val id = zone.id
        val timeZone1 = TimeZone.getTimeZone(id)
        val timeZone2 = TimeZone.getTimeZone("UTC")

        val currentOffsetFromUTC = if (timeZone1.inDaylightTime(now)) {
            timeZone1.dstSavings + timeZone1.rawOffset
        } else {
            0 + timeZone1.rawOffset
        }

        val serverOffsetFromUTC = if (timeZone2.inDaylightTime(now)) {
            timeZone2.dstSavings + timeZone2.rawOffset
        } else {
            0 + timeZone2.rawOffset
        }

        val isNegativeNumber = currentOffsetFromUTC < 0;

        val secondOffset = abs(abs(serverOffsetFromUTC) - currentOffsetFromUTC) / 1000
        val hour = secondOffset / 3600
        val minute = secondOffset % 3600 / 60
        return if (isNegativeNumber) {
            -1.0f * (hour + minute / 60.0f)
        } else {
            hour + minute / 60.0f
        }
    }

}