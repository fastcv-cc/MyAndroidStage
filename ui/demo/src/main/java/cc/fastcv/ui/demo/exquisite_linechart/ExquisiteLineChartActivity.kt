package cc.fastcv.ui.demo.exquisite_linechart

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.PointF
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatCheckBox
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
        initChartLineColor()
        initCentralLineWidth()
        initCentralLineColor()
        initTouchRange()
        initShader()
        initXAxisLabelTextSize()
        initXAxisLabelTextColor()
        initYAxisLabelTextSize()
        initYAxisLabelTextColor()
        val data = RunningDataSimulator.generateRunData(
            totalMinutes = 30,
            minPace = 390,
            maxPace = 460
        )
        setData(data, false)
    }

    private fun setData(data: MutableList<PointF>, yAxisIncrement: Boolean) {
        //时间
        val xList = data.map { it.x }
        //配速
        val yList = data.map { it.y }

        val avgX = xList.last() / 5.0f
        val xMax = xList.maxOf { it }
        val xMin = xList.minOf { it }
        val xLabelList = mutableListOf<String>()
        for (i in 0..5) {
            xLabelList.add(
                String.format(
                    Locale.ENGLISH,
                    "%d:%02d",
                    (i * avgX / 60).toInt(),
                    (i * avgX % 60).toInt()
                )
            )
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
        if (yAxisIncrement) {
            yLabelList.reverse()
        }
        histogram.setYAxisIncrement(yAxisIncrement)
        histogram.setXAxisParams(xLabelList, xMin, xMax)
        histogram.setYAxisParams(yLabelList, yMin, yMax)
        histogram.setPoints(data)
    }

    private fun initDataSync() {
        findViewById<Button>(R.id.bt_sync_data).setOnClickListener {
            val totalMinutes = findViewById<EditText>(R.id.edTotalMinutes).text.toString().toInt()
            val minPace = findViewById<EditText>(R.id.edMinPace).text.toString().toInt()
            val maxPace = findViewById<EditText>(R.id.edMaxPace).text.toString().toInt()
            val addZero = findViewById<AppCompatCheckBox>(R.id.cbAddVirtualZero).isChecked
            val yAxisIncrement = findViewById<AppCompatCheckBox>(R.id.cbYAxisIncrement).isChecked
            val autoLineWidth = findViewById<AppCompatCheckBox>(R.id.cbAutoLineWidth).isChecked
            val newData = RunningDataSimulator.generateRunData(
                totalMinutes = totalMinutes,
                minPace = minPace,
                maxPace = maxPace
            )
            Log.d("xcl_debug", "initDataSync: addZero = $addZero")
            histogram.setAddZeroPoint(addZero)
            histogram.setAutoLineWidth(autoLineWidth)
            setData(newData,yAxisIncrement)
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

    private fun initChartLineColor() {
        findViewById<Button>(R.id.bt_line_color_green).setOnClickListener {
            histogram.setChartLineColor("#026543".toColorInt())
        }
        findViewById<Button>(R.id.bt_line_color_red).setOnClickListener {
            histogram.setChartLineColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_line_color_blue).setOnClickListener {
            histogram.setChartLineColor(Color.BLUE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCentralLineWidth() {
        findViewById<SeekBar>(R.id.sb_central_line_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setCentralLineWidth(progress.toFloat())
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv_central_line_width).text =
                        "中间线线宽：${progress}dp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initCentralLineColor() {
        findViewById<Button>(R.id.bt_central_color_gray).setOnClickListener {
            histogram.setCentralLineColor("#C9CDC6".toColorInt())
        }
        findViewById<Button>(R.id.bt_central_color_red).setOnClickListener {
            histogram.setCentralLineColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_central_color_blue).setOnClickListener {
            histogram.setCentralLineColor(Color.BLUE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTouchRange() {
        findViewById<SeekBar>(R.id.sb_touch_range).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setXAxisTouchRange(progress.toFloat())
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv_touch_range).text =
                        "选择点x轴扩充范围：$progress"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initShader() {
        findViewById<Button>(R.id.bt_shader_none).setOnClickListener {
            histogram.setShader(null)
        }
        findViewById<Button>(R.id.bt_shader_blue).setOnClickListener {
            histogram.setShader(
                LinearGradient(
                    0f, 0f, 0f, 400f,
                    "#330000FF".toColorInt(),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
            )
        }
        findViewById<Button>(R.id.bt_shader_green).setOnClickListener {
            histogram.setShader(
                LinearGradient(
                    0f, 0f, 0f, histogram.height * 1.0f,
                    "#3300FF00".toColorInt(),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initXAxisLabelTextSize() {
        findViewById<SeekBar>(R.id.sb_x_label_text_size).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val size = progress.coerceAtLeast(1).toFloat()
                    histogram.setXAxisLabelTextSize(size)
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv_x_label_text_size).text =
                        "X轴标签文字大小：${size.toInt()}sp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initXAxisLabelTextColor() {
        findViewById<Button>(R.id.bt_x_label_color_default).setOnClickListener {
            histogram.setXAxisLabelTextColor("#6F756B".toColorInt())
        }
        findViewById<Button>(R.id.bt_x_label_color_red).setOnClickListener {
            histogram.setXAxisLabelTextColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_x_label_color_blue).setOnClickListener {
            histogram.setXAxisLabelTextColor(Color.BLUE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initYAxisLabelTextSize() {
        findViewById<SeekBar>(R.id.sb_y_label_text_size).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val size = progress.coerceAtLeast(1).toFloat()
                    histogram.setYAxisLabelTextSize(size)
                    this@ExquisiteLineChartActivity.findViewById<TextView>(R.id.tv_y_label_text_size).text =
                        "Y轴标签文字大小：${size.toInt()}sp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initYAxisLabelTextColor() {
        findViewById<Button>(R.id.bt_y_label_color_default).setOnClickListener {
            histogram.setYAxisLabelTextColor("#6F756B".toColorInt())
        }
        findViewById<Button>(R.id.bt_y_label_color_red).setOnClickListener {
            histogram.setYAxisLabelTextColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_y_label_color_blue).setOnClickListener {
            histogram.setYAxisLabelTextColor(Color.BLUE)
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