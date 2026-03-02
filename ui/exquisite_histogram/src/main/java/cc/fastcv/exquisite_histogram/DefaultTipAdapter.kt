package cc.fastcv.exquisite_histogram

internal class DefaultTipAdapter : TipAdapter() {
    override fun getTopText(position: Int, info: HistogramInfo): CharSequence {
        return "index:$position"
    }

    override fun getBottomText(position: Int, info: HistogramInfo): CharSequence {
        return "${info.value}"
    }
}