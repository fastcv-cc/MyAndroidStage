package cc.fastcv.date_switch_view

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

class DateBuildFactory {

    private val dateUtil = DateUtil()

    private var startDate: String = ""
    private var endDate: String = ""

    fun setDateRange(start: String, end: String) {
        startDate = start
        endDate = end
    }

    fun buildMonthList(): MutableList<DateInfo> {
        val bindDateStr = startDate

        val endDateStr = endDate
        //获取两个时间节点相差的月数
        val difMonthNumber = dateUtil.getDateDifferenceMonth(
            dateUtil.parseDateTimeStrToDate(
                bindDateStr,
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            ),
            dateUtil.parseDateTimeStrToDate(
                endDateStr,
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            )
        )

        val toBeAdded = 7 - difMonthNumber % 7
        val startDateStr = dateUtil.getOffsetDateByMonth(
            -toBeAdded, bindDateStr,
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        )
        val listTotal: Int = (difMonthNumber + toBeAdded) / 7
        //所有的数据列表
        val list = mutableListOf<DateInfo>()

        //绑定设备时的时间戳
        val bindDeviceTimestamp =
            dateUtil.convertDateTimeStrToTimestamp(
                bindDateStr,
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            )
        //获取绑定时间的calendar对象 方便做对比操作
        val bindCalendar = Calendar.getInstance()
        bindCalendar.timeInMillis = bindDeviceTimestamp

        val startCalendar = Calendar.getInstance()
        startCalendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(startDateStr)!!

        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(endDate)!!

        for (i in 1..listTotal) {
            val weekInfo = DateInfo()
            for (j in 0..6) {
                startCalendar.add(Calendar.MONTH, 1)
                val dayInfo = buildDayInfo(startCalendar, bindCalendar, todayCalendar, 1)
                weekInfo.weekContent[j] = dayInfo
            }
            list.add(weekInfo)
        }
        return list
    }


    fun buildDayList(weekStart: Int): MutableList<DateInfo> {
        val bindDateStr = startDate
        //获取列表开始时间 可以理解为生成数据时真正的开始日期
        val startDateStr = getListStartDate(bindDateStr, weekStart)
        //获取列表结束时间 可以理解为生成数据时真正的结束日期
        val endDateStr = getListEndDate(endDate, weekStart)

        //获取两个时间节点相差的天数
        val difDayNumber = dateUtil.getDateDifferenceDay(startDateStr, endDateStr) + 1

        //除以7得到总共显示的周数
        val listTotal: Int = difDayNumber / 7
        //所有的数据列表
        val list = mutableListOf<DateInfo>()

        //绑定设备时的时间戳
        val bindDeviceTimestamp =
            dateUtil.convertDateTimeStrToTimestamp(
                bindDateStr,
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            )
        //获取绑定时间的calendar对象 方便做对比操作
        val bindCalendar = Calendar.getInstance()
        bindCalendar.timeInMillis = bindDeviceTimestamp

        //获取开始日期的calendar对象 方便做日期自加的操作
        val startCalendar = Calendar.getInstance()
        startCalendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(startDateStr)!!
        //往开始日期前移动一天，方便后面的计算
        startCalendar.add(Calendar.DAY_OF_MONTH, -1)

        //获取结束日期的calendar对象
        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(endDate)!!

        for (i in 1..listTotal) {
            val weekInfo = DateInfo()
            for (j in 0..6) {
                startCalendar.add(Calendar.DAY_OF_MONTH, 1)
                val dayInfo = buildDayInfo(startCalendar, bindCalendar, todayCalendar, 0)
                weekInfo.weekContent[j] = dayInfo
            }
            list.add(weekInfo)
        }
        return list
    }


    private fun buildDayInfo(
        startCalendar: Calendar,
        bindCalendar: Calendar,
        todayCalendar: Calendar,
        type: Int
    ): DateInfo.DayInfoDetail {
        val dayInfo = DateInfo.DayInfoDetail()
        dayInfo.year = startCalendar[Calendar.YEAR]
        dayInfo.month = startCalendar[Calendar.MONTH] + 1
        dayInfo.day = startCalendar[Calendar.DAY_OF_MONTH]
        dayInfo.isInvalid =
            if (type == 0) {
                (bindCalendar.timeInMillis > startCalendar.timeInMillis || todayCalendar.timeInMillis < startCalendar.timeInMillis)
            } else {
                (bindCalendar[Calendar.YEAR] > startCalendar[Calendar.YEAR]
                        || (bindCalendar[Calendar.YEAR] == startCalendar[Calendar.YEAR] && bindCalendar[Calendar.MONTH] > startCalendar[Calendar.MONTH]))
                        || (todayCalendar[Calendar.YEAR] < startCalendar[Calendar.YEAR]
                        || (todayCalendar[Calendar.YEAR] == startCalendar[Calendar.YEAR] && todayCalendar[Calendar.MONTH] < startCalendar[Calendar.MONTH]))
            }

        dayInfo.isToday = if (type == 0) {
            dateUtil.isSameDay(todayCalendar, startCalendar)
        } else {
            todayCalendar[Calendar.YEAR] == startCalendar[Calendar.YEAR] && todayCalendar[Calendar.MONTH] == startCalendar[Calendar.MONTH]
        }

        dayInfo.isSelected = dayInfo.isToday
        if (type == 1) {
            dayInfo.day = -1
        }
        return dayInfo
    }

    /**
     * 获取某个日期按照每周开始（周天/周一）得到这一周的开始日期
     */
    private fun getListStartDate(bindDate: String, weekStart: Int): String {
        //得到绑定日期是周几
        val bindDayWeekIndex = dateUtil.getWeekIndexByDate(
            dateUtil.parseDateTimeStrToDate(
                "$bindDate 00:00:00",
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            )
        )

        var difDay = bindDayWeekIndex - (1 - weekStart)
        if (difDay < 0) {
            difDay += 7
        }

        //拿到绑定日期这一周的起始日期 周日开始
        return dateUtil.getOffsetDateByDay(
            -difDay,
            bindDate,
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        )
    }

    /**
     * 获取某个日期按照每周开始（周天/周一）得到这一周的结束日期
     */
    private fun getListEndDate(end: String, weekStart: Int): String {
        //得到今天是周几
        val endDayWeekIndex = dateUtil.getWeekIndexByDate(
            dateUtil.parseDateTimeStrToDate(
                end,
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            )
        )

        val difDay = ((6 - endDayWeekIndex) + (1 - weekStart)) % 7

        //拿到今天这一周的结束日期
        return dateUtil.getOffsetDateByDay(
            difDay,
            end,
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        )
    }

    fun isInitialization(): Boolean {
        return !TextUtils.isEmpty(startDate)
    }
}