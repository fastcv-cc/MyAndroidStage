package cc.fastcv.ui.demo.exquisite_linechart

import kotlin.random.Random

object RunningDataSimulator {
    /**
     * 生成跑步数据
     *
     * @param totalMinutes 总运动时间（分钟）
     * @param minPace 最快配速（秒/km）
     * @param maxPace 最慢配速（秒/km）
     */
    fun generateRunData(
        totalMinutes: Int,
        minPace: Int,
        maxPace: Int
    ): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        val avgPace = (minPace + maxPace) / 2
        for (minute in 0 until totalMinutes) {
            val phaseModifier = when {
                minute < totalMinutes * 0.15 -> 30      // 热身慢
                minute > totalMinutes * 0.85 -> 25      // 收尾慢
                else -> 0
            }
            val randomFluctuation = Random.nextInt(-8, 8)
            var pace = avgPace + phaseModifier + randomFluctuation
            if (pace < minPace) pace = minPace
            if (pace > maxPace) pace = maxPace
            result.add(Pair(pace, 60 * (minute + 1)))
        }
        return result
    }
}