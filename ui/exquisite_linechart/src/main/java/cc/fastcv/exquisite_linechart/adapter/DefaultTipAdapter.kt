package cc.fastcv.exquisite_linechart.adapter

import cc.fastcv.exquisite_linechart.LineChartInfo
import cc.fastcv.exquisite_linechart.adapter.TipAdapter

internal class DefaultTipAdapter : TipAdapter() {
    override fun getTopText(position: Int, info: LineChartInfo): CharSequence {
        return "index:$position"
    }

    override fun getBottomText(position: Int, info: LineChartInfo): CharSequence {
        return "${info.value.x} - ${info.value.x}"
    }
}