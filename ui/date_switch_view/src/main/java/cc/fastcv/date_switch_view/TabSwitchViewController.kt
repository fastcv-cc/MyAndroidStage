package cc.fastcv.date_switch_view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.math.abs

class TabSwitchViewController(private val dateSwitchView: DateSwitchView) {

    private var viewSwitchBg: View = dateSwitchView.findViewById(R.id.view_switch_bg)
    private var viewBg: View = dateSwitchView.findViewById(R.id.view_bg)
    private var btDay: TextView = dateSwitchView.findViewById<TextView>(R.id.bt_day).apply {
        setOnClickListener {
            dateSwitchView.runAnim(0)
        }
    }
    private var btMonth: TextView = dateSwitchView.findViewById<TextView>(R.id.bt_month).apply {
        setOnClickListener {
            dateSwitchView.runAnim(1)
        }
    }

    fun syncTextParam() {
        btDay.text = dateSwitchView.params.dayTabText
        btMonth.text = dateSwitchView.params.monthTabText


        if (dateSwitchView.params.selectedIndex == 0) {
            btDay.setTextColor(dateSwitchView.params.customizeTabSelectedTextColor)
            btMonth.setTextColor(dateSwitchView.params.customizeTabTextColor)
        } else {
            btDay.setTextColor(dateSwitchView.params.customizeTabTextColor)
            btMonth.setTextColor(dateSwitchView.params.customizeTabSelectedTextColor)
        }

        btDay.textSize = dateSwitchView.params.tabTextSize
        btMonth.textSize = dateSwitchView.params.tabTextSize
    }

    fun updateLayoutParams() {
        updateLayoutParams(
            viewSwitchBg,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabWidth,
            dateSwitchView.params.customizeTabHeight
        )

        updateLayoutParams(
            btDay,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabMargin,
            0,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabWidth,
            dateSwitchView.params.customizeTabHeight
        )
        updateLayoutParams(
            btMonth,
            0,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabMargin,
            dateSwitchView.params.customizeTabWidth,
            dateSwitchView.params.customizeTabHeight
        )
        updateLayoutParams(
            viewBg,
            0,
            0,
            0,
            0,
            -1,
            dateSwitchView.params.customizeTabHeight + dateSwitchView.params.customizeTabMargin * 2
        )
    }

    private fun updateLayoutParams(
        view: View,
        marginStart: Int,
        marginTop: Int,
        marginEnd: Int,
        marginBottom: Int,
        width: Int,
        height: Int
    ) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = marginStart
        layoutParams.marginEnd = marginEnd
        layoutParams.topMargin = marginTop
        layoutParams.bottomMargin = marginBottom
        if (width != -1) {
            layoutParams.width = width
        }

        if (height != -1) {
            layoutParams.height = height
        }

        view.layoutParams = layoutParams
    }

    fun getTabButtonMargin(): Float {
        return abs(btMonth.x - btDay.x)
    }

    fun getViewSwitchBgMarginStart(): Int {
        return (viewSwitchBg.layoutParams as FrameLayout.LayoutParams).marginStart
    }

    fun updateViewSwitchBgMarginStart(marginStart: Int) {
        val layoutParams = viewSwitchBg.layoutParams as FrameLayout.LayoutParams
        layoutParams.marginStart = marginStart
        viewSwitchBg.layoutParams = layoutParams
    }
}