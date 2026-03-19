package cc.fastcv.exquisite_linechart.adapter

import cc.fastcv.exquisite_linechart.LineChartInfo

abstract class TipAdapter {
    abstract fun getTopText(position: Int, info: LineChartInfo): CharSequence
    abstract fun getBottomText(position: Int, info: LineChartInfo): CharSequence
}