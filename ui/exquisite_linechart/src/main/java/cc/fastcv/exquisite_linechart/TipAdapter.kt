package cc.fastcv.exquisite_linechart

abstract class TipAdapter {
    abstract fun getTopText(position: Int, info: LineChartInfo): CharSequence
    abstract fun getBottomText(position: Int, info: LineChartInfo): CharSequence
}