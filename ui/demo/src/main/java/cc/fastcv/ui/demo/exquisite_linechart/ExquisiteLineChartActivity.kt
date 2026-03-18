package cc.fastcv.ui.demo.exquisite_linechart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.exquisite_linechart.ExquisiteLineChartView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R

class ExquisiteLineChartActivity : StageActivity() {

    private lateinit var histogram: ExquisiteLineChartView

    val data = RunningDataSimulator.generateRunData(
        totalMinutes = 30,
        minPace = 390,
        maxPace = 460
    )

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
        histogram.setData(data)

        initHistogramWidth()
        initSideBarWidth()
        initSyncEqualSize()
        initDataSync()
        initLtr()
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
            histogram.setData(newData)
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

    private fun initSyncEqualSize() {
        findViewById<Button>(R.id.bt_sync_equal_size).setOnClickListener {
            val equalSize = findViewById<EditText>(R.id.ed_equal_size).text.toString()
            try {
                val size = equalSize.toInt()
                histogram.setEqualPartsSize(size)
            } catch (e: Exception) {
                Toast.makeText(this, "数据格式错误", Toast.LENGTH_SHORT).show()
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
                    histogram.setSideBarWith(progress)
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
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv1).text = "设置折线宽度：${progress}dp"
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
                    histogram.setPathEffect(progress.toFloat())
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv2).text = "设置折线圆角度：${progress}dp"
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
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv3).text = "底部留白高度：${progress}dp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}