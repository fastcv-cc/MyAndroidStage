package cc.fastcv.exquisite_histogram

abstract class TipAdapter {
    abstract fun getTopText(position: Int, info: HistogramInfo): CharSequence
    abstract fun getBottomText(position: Int, info: HistogramInfo): CharSequence
}