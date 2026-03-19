package cc.fastcv.ui.demo.exquisite_linechart

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.PointF
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.exquisite_linechart.ExquisiteLineChartView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R
import java.util.Locale

class ExquisiteLineChartActivity : StageActivity() {

    private lateinit var histogram: ExquisiteLineChartView

//    private var shader = LinearGradient(
//        0f, 0f,
//        0f, height.toFloat(),
//        "#330000FF".toColorInt(),
//        Color.TRANSPARENT,
//        Shader.TileMode.CLAMP
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exquisite_linechart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        histogram = findViewById(R.id.histogram)


        initHistogramWidth()
        initSideBarWidth()
        initDataSync()
        initLtr()
        val data = RunningDataSimulator.generateRunData(
            totalMinutes = 30,
            minPace = 390,
            maxPace = 460
        )
        setData(data)
    }

    private fun setData(data: List<PointF>) {
        //时间
        val xList = data.map { it.x }
        //配速
        val yList = data.map { it.y }

        val avgX = xList.last() / 5.0f
        val xMax = xList.maxOf { it }
        val xMin = xList.minOf { it }
        val xLabelList = mutableListOf<String>()
        for (i in 0..5) {
            xLabelList.add(String.format(Locale.ENGLISH, "%d:%02d", (i * avgX / 60).toInt(), (i * avgX % 60).toInt()))
        }


        val yMax = yList.maxOf { it }
        val yMin = yList.minOf { it }
        val avgY = (yMax - yMin) / 4.0f
        val yLabelList = mutableListOf<String>()
        for (i in 0..4) {
            yLabelList.add(
                String.format(
                    Locale.ENGLISH,
                    "%d:%02d",
                    ((i * avgY + yMin) / 60).toInt(),
                    ((i * avgY + yMin) % 60).toInt()
                )
            )
        }

        histogram.setXAxisParams(xLabelList, xMin, xMax)
        histogram.setYAxisParams(yLabelList, yMin, yMax)
        histogram.setPoints(data)
    }

    private fun initDataSync() {
        findViewById<Button>(R.id.bt_sync_data).setOnClickListener {
            val totalMinutes = findViewById<EditText>(R.id.edTotalMinutes).text.toString().toInt()
            val minPace = findViewById<EditText>(R.id.edMinPace).text.toString().toInt()
            val maxPace = findViewById<EditText>(R.id.edMaxPace).text.toString().toInt()
            val newData = RunningDataSimulator.generateRunData(
                totalMinutes = totalMinutes,
                minPace = minPace,
                maxPace = maxPace
            )
            setData(newData)
        }
    }

    private fun initLtr() {
        findViewById<SwitchCompat>(R.id.sc_ltr).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                histogram.layoutDirection = View.LAYOUT_DIRECTION_RTL
            } else {
                histogram.layoutDirection = View.LAYOUT_DIRECTION_LTR
            }
        }
    }


    private fun initSideBarWidth() {
        findViewById<SeekBar>(R.id.sb_side_bar_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setSideBarWith(progress * 1.0f)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initHistogramWidth() {
        findViewById<SeekBar>(R.id.sb_histogram_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setLineChartWidth(progress.toFloat())
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv1).text =
                        "设置折线宽度：${progress}dp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        findViewById<SeekBar>(R.id.sb_line_chart_effect).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setLineChartRadius(progress.toFloat())
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv2).text =
                        "设置折线圆角度：${progress}dp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }


        findViewById<SeekBar>(R.id.sb_line_chart_botton_space_height).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setBottomSpaceHeight(progress.toFloat())
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv3).text =
                        "底部留白高度：${progress}dp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}