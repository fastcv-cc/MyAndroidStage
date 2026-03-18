package cc.fastcv.exquisite_linechart

internal class DefaultTipAdapter : TipAdapter() {
    override fun getTopText(position: Int, info: LineChartInfo): CharSequence {
        return "index:$position"
    }

    override fun getBottomText(position: Int, info: LineChartInfo): CharSequence {
        return "${info.value}"
    }
}