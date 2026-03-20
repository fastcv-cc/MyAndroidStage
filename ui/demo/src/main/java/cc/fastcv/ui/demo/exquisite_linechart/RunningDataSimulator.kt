package cc.fastcv.ui.demo.exquisite_linechart

import android.graphics.PointF
import kotlin.random.Random

object RunningDataSimulator {
    /**
     * 生成跑步数据
     *
     * @param totalMinutes 总运动时间（分钟）
     * @param minPace 最快配速（秒/km）
     * @param maxPace 最慢配速（秒/km）
     * x 时长
     * y 配速
     */
    fun generateRunData(
        totalMinutes: Int,
        minPace: Int,
        maxPace: Int
    ): MutableList<PointF> {
        val result = mutableListOf<PointF>()
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
            result.add(PointF(60 * (minute + 1) * 1.0f, pace * 1.0f))
        }
        return result
    }
}