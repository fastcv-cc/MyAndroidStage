package cc.fastcv.exquisite_linechart

interface LineChartCoordinatorCallback {
    fun onLineChartSelect(position:Int, info: LineChartInfo?)
    fun onLineChartCalc()
}