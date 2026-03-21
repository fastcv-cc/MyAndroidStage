package cc.fastcv.ui.demo.seg_diagram

import cc.fastcv.exquisite_segdiagram.SegDiagramData
import kotlin.random.Random

object RunningDataSimulator {
    /**
     * 生成心率数据
     *
     * @param totalMinutes 总运动时间（分钟）
     * @param minHr 最低心率
     * @param maxHr 最高心率
     * 每分钟生成一条数据，包含该分钟内心率的最小值和最大值
     */
    fun generateHeartRateData(
        totalMinutes: Int,
        minHr: Int,
        maxHr: Int
    ): MutableList<SegDiagramData> {
        val result = mutableListOf<SegDiagramData>()
        val avgHr = (minHr + maxHr) / 2
        for (minute in 0 until totalMinutes) {
            val phaseModifier = when {
                minute < totalMinutes * 0.15 -> -15     // 热身阶段心率较低
                minute > totalMinutes * 0.85 -> -10     // 收尾阶段心率回落
                else -> 0
            }
            val baseHr = avgHr + phaseModifier + Random.nextInt(-5, 5)
            val fluctuation = Random.nextInt(3, 12)
            val low = (baseHr - fluctuation).coerceIn(minHr, maxHr)
            val high = (baseHr + fluctuation).coerceIn(minHr, maxHr)
            val actualMin = minOf(low, high)
            val actualMax = maxOf(low, high)
            result.add(SegDiagramData(60 * (minute + 1), actualMin.toFloat(), actualMax.toFloat()))
        }
        return result
    }
}
