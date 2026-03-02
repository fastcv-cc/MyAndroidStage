package cc.fastcv.exquisite_histogram

interface HistogramCoordinatorCallback {
    fun onHistogramSelect(position:Int, info: HistogramInfo?)
    fun onHistogramCalc()
}