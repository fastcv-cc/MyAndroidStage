package cc.fastcv.date_switch_view

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.date_switch_view.DateSwitchView.Companion.WEEK_START_WITH_MONDAY

internal class DateSelectController(private val dateSwitchView: DateSwitchView) {

    private var lastWeekStart = WEEK_START_WITH_MONDAY

    private var llWeeks: LinearLayout = dateSwitchView.findViewById(R.id.ll_weeks)
    private var tvWeek1: TextView = dateSwitchView.findViewById(R.id.tv_week_1)
    private var tvWeek2: TextView = dateSwitchView.findViewById(R.id.tv_week_2)
    private var tvWeek3: TextView = dateSwitchView.findViewById(R.id.tv_week_3)
    private var tvWeek4: TextView = dateSwitchView.findViewById(R.id.tv_week_4)
    private var tvWeek5: TextView = dateSwitchView.findViewById(R.id.tv_week_5)
    private var tvWeek6: TextView = dateSwitchView.findViewById(R.id.tv_week_6)
    private var tvWeek7: TextView = dateSwitchView.findViewById(R.id.tv_week_7)
    private var dateSwitchAdapter: DateSwitchAdapter = DateSwitchAdapter(mutableListOf()).apply {
        setItemClickListener(object :
            OnDateItemClickListener<DateInfo.DayInfoDetail> {
            override fun onDateItemClick(t: DateInfo.DayInfoDetail) {
                dateSwitchView.selectDate(t)
            }
        })
    }

    private var rvDate: RecyclerView =
        dateSwitchView.findViewById<RecyclerView>(R.id.rv_date).apply {
            adapter = dateSwitchAdapter
            CardLinearSnapHelper().attachToRecyclerView(this)
        }

    private val dateBuildFactory = DateBuildFactory()

    /**
     * 周组件高度
     */
    private var weekLayoutHeight = 0

    /**
     * 格式：yyyy-MM-dd
     */
    fun setDateRange(start: String, end: String) {
        dateBuildFactory.setDateRange(start, end)
        val list = if (dateSwitchView.params.selectedIndex == 0) {
            dateBuildFactory.buildDayList(lastWeekStart)
        } else {
            dateBuildFactory.buildMonthList()
        }
        dateSwitchAdapter.updateDataList(list)
    }

    fun switchToDayModel() {
        if (dateBuildFactory.isInitialization()) {
            val list = dateBuildFactory.buildDayList(lastWeekStart)
            dateSwitchAdapter.updateDataList(list)
            rvDate.scrollToPosition(dateSwitchAdapter.itemCount - 1)
        }
    }

    fun switchToMonthModel() {
        if (dateBuildFactory.isInitialization()) {
            val list = dateBuildFactory.buildMonthList()
            dateSwitchAdapter.updateDataList(list)
            rvDate.scrollToPosition(dateSwitchAdapter.itemCount - 1)
        }
    }

    fun syncLayoutParams() {
        updateLayoutParams(tvWeek1, dateSwitchView.params.weekTextMargin)
        updateLayoutParams(tvWeek2, dateSwitchView.params.weekTextMargin)
        updateLayoutParams(tvWeek3, dateSwitchView.params.weekTextMargin)
        updateLayoutParams(tvWeek4, dateSwitchView.params.weekTextMargin)
        updateLayoutParams(tvWeek5, dateSwitchView.params.weekTextMargin)
        updateLayoutParams(tvWeek6, dateSwitchView.params.weekTextMargin)
        dateSwitchAdapter.setWeekTextMargin(dateSwitchView.params.weekTextMargin)
    }

    fun setWeekText() {
        if (dateSwitchView.params.weekStart == WEEK_START_WITH_MONDAY) {
            tvWeek1.text = dateSwitchView.params.weekStrArrays[0]
            tvWeek2.text = dateSwitchView.params.weekStrArrays[1]
            tvWeek3.text = dateSwitchView.params.weekStrArrays[2]
            tvWeek4.text = dateSwitchView.params.weekStrArrays[3]
            tvWeek5.text = dateSwitchView.params.weekStrArrays[4]
            tvWeek6.text = dateSwitchView.params.weekStrArrays[5]
            tvWeek7.text = dateSwitchView.params.weekStrArrays[6]
        } else {
            tvWeek1.text = dateSwitchView.params.weekStrArrays[6]
            tvWeek2.text = dateSwitchView.params.weekStrArrays[0]
            tvWeek3.text = dateSwitchView.params.weekStrArrays[1]
            tvWeek4.text = dateSwitchView.params.weekStrArrays[2]
            tvWeek5.text = dateSwitchView.params.weekStrArrays[3]
            tvWeek6.text = dateSwitchView.params.weekStrArrays[4]
            tvWeek7.text = dateSwitchView.params.weekStrArrays[5]
        }
    }


    private fun updateLayoutParams(
        view: View,
        marginEnd: Int,
    ) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = 0
        layoutParams.marginEnd = marginEnd
        view.layoutParams = layoutParams
    }

    fun initWeekLayoutHeightIfNeed() {
        if (weekLayoutHeight <= 0) {
            weekLayoutHeight = llWeeks.height
        }
    }

    fun updateWeekLayoutHeightAndAlpha(faction: Float) {
        val height = faction * weekLayoutHeight
        val weekLayoutParams = llWeeks.layoutParams as ViewGroup.MarginLayoutParams
        weekLayoutParams.height = height.toInt()
        llWeeks.alpha = faction
        llWeeks.layoutParams = weekLayoutParams
    }

    fun rebuildData() {
        //如果选中的是周 并且周开始有变化时
        if (lastWeekStart != dateSwitchView.params.weekStart && dateSwitchView.params.selectedIndex == 0) {
            if (dateBuildFactory.isInitialization()) {
                lastWeekStart = dateSwitchView.params.weekStart
                val list = dateBuildFactory.buildDayList(lastWeekStart)
                dateSwitchAdapter.updateDataList(list)
                rvDate.scrollToPosition(dateSwitchAdapter.itemCount - 1)
            }
        }
    }

}