package cc.fastcv.ui.demo.seg_diagram

import android.annotation.SuppressLint
import android.graphics.Color
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
import cc.fastcv.exquisite_segdiagram.ExquisiteSegDiagramView
import cc.fastcv.exquisite_segdiagram.SegDiagramData
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R
import java.util.Locale

class ExquisiteSegDiagramActivity : StageActivity() {

    private lateinit var segDiagram: ExquisiteSegDiagramView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seg_diagram)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        segDiagram = findViewById(R.id.segDiagram)

        initLineWidth()
        initBottomSpace()
        initLineColor()
        initCentralLineWidth()
        initCentralLineColor()
        initSideBarWidth()
        initTouchRange()
        initXAxisLabelTextSize()
        initXAxisLabelTextColor()
        initYAxisLabelTextSize()
        initYAxisLabelTextColor()
        initDataSync()
        initLtr()

        val data = RunningDataSimulator.generateHeartRateData(
            totalMinutes = 30,
            minHr = 70,
            maxHr = 140
        )
        setData(data)
    }

    private fun setData(data: List<SegDiagramData>) {
        val timeList = data.map { it.time.toFloat() }
        val hrMinList = data.map { it.min }
        val hrMaxList = data.map { it.max }

        // x轴：时间标签
        val xMax = timeList.maxOf { it }
        val xMin = timeList.minOf { it }
        val avgX = xMax / 5.0f
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

        // y轴：心率标签
        val yMin = hrMinList.minOf { it }
        val yMax = hrMaxList.maxOf { it }
        val avgY = (yMax - yMin) / 4.0f
        val yLabelList = mutableListOf<String>()
        for (i in 0..4) {
            yLabelList.add((i * avgY + yMin).toInt().toString())
        }
        yLabelList.reverse()
        segDiagram.setXAxisParams(xLabelList, xMin, xMax)
        segDiagram.setYAxisParams(yLabelList, yMin, yMax)
        segDiagram.setPoints(data)
    }

    private fun initDataSync() {
        findViewById<Button>(R.id.bt_sync_data).setOnClickListener {
            val totalMinutes = findViewById<EditText>(R.id.edTotalMinutes).text.toString().toInt()
            val minHr = findViewById<EditText>(R.id.edMinHr).text.toString().toInt()
            val maxHr = findViewById<EditText>(R.id.edMaxHr).text.toString().toInt()

            val autoLineWidth = findViewById<AppCompatCheckBox>(R.id.cbAutoLineWidth).isChecked
            segDiagram.setAutoLineWidth(autoLineWidth)
            val newData = RunningDataSimulator.generateHeartRateData(
                totalMinutes = totalMinutes,
                minHr = minHr,
                maxHr = maxHr
            )
            setData(newData)
        }
    }

    private fun initLtr() {
        findViewById<SwitchCompat>(R.id.sc_ltr).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                segDiagram.layoutDirection = View.LAYOUT_DIRECTION_RTL
            } else {
                segDiagram.layoutDirection = View.LAYOUT_DIRECTION_LTR
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initLineWidth() {
        findViewById<SeekBar>(R.id.sb_line_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    segDiagram.setDiagramLineWidth(progress.toFloat())
                    this@ExquisiteSegDiagramActivity.findViewById<TextView>(R.id.tv_line_width).text =
                        "设置线段宽度：${progress}dp"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initBottomSpace() {
        findViewById<SeekBar>(R.id.sb_bottom_space).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    segDiagram.setBottomSpaceHeight(progress.toFloat())
                    this@ExquisiteSegDiagramActivity.findViewById<TextView>(R.id.tv_bottom_space).text =
                        "底部留白高度：${progress}dp"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initLineColor() {
        findViewById<Button>(R.id.bt_line_color_green).setOnClickListener {
            segDiagram.setChartLineColor("#026543".toColorInt())
        }
        findViewById<Button>(R.id.bt_line_color_red).setOnClickListener {
            segDiagram.setChartLineColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_line_color_blue).setOnClickListener {
            segDiagram.setChartLineColor(Color.BLUE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCentralLineWidth() {
        findViewById<SeekBar>(R.id.sb_central_line_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    segDiagram.setCentralLineWidth(progress.toFloat())
                    this@ExquisiteSegDiagramActivity.findViewById<TextView>(R.id.tv_central_line_width).text =
                        "中间线线宽：${progress}dp"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initCentralLineColor() {
        findViewById<Button>(R.id.bt_central_color_gray).setOnClickListener {
            segDiagram.setCentralLineColor("#C9CDC6".toColorInt())
        }
        findViewById<Button>(R.id.bt_central_color_red).setOnClickListener {
            segDiagram.setCentralLineColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_central_color_blue).setOnClickListener {
            segDiagram.setCentralLineColor(Color.BLUE)
        }
    }

    private fun initSideBarWidth() {
        findViewById<SeekBar>(R.id.sb_side_bar_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    segDiagram.setSideBarWith(progress * 1.0f)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTouchRange() {
        findViewById<SeekBar>(R.id.sb_touch_range).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    segDiagram.setXAxisTouchRange(progress.toFloat())
                    this@ExquisiteSegDiagramActivity.findViewById<TextView>(R.id.tv_touch_range).text =
                        "选择点x轴扩充范围：$progress"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initXAxisLabelTextSize() {
        findViewById<SeekBar>(R.id.sb_x_label_text_size).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val size = progress.coerceAtLeast(1).toFloat()
                    segDiagram.setXAxisLabelTextSize(size)
                    this@ExquisiteSegDiagramActivity.findViewById<TextView>(R.id.tv_x_label_text_size).text =
                        "X轴标签文字大小：${size.toInt()}sp"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initXAxisLabelTextColor() {
        findViewById<Button>(R.id.bt_x_label_color_default).setOnClickListener {
            segDiagram.setXAxisLabelTextColor("#6F756B".toColorInt())
        }
        findViewById<Button>(R.id.bt_x_label_color_red).setOnClickListener {
            segDiagram.setXAxisLabelTextColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_x_label_color_blue).setOnClickListener {
            segDiagram.setXAxisLabelTextColor(Color.BLUE)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initYAxisLabelTextSize() {
        findViewById<SeekBar>(R.id.sb_y_label_text_size).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val size = progress.coerceAtLeast(1).toFloat()
                    segDiagram.setYAxisLabelTextSize(size)
                    this@ExquisiteSegDiagramActivity.findViewById<TextView>(R.id.tv_y_label_text_size).text =
                        "Y轴标签文字大小：${size.toInt()}sp"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initYAxisLabelTextColor() {
        findViewById<Button>(R.id.bt_y_label_color_default).setOnClickListener {
            segDiagram.setYAxisLabelTextColor("#6F756B".toColorInt())
        }
        findViewById<Button>(R.id.bt_y_label_color_red).setOnClickListener {
            segDiagram.setYAxisLabelTextColor(Color.RED)
        }
        findViewById<Button>(R.id.bt_y_label_color_blue).setOnClickListener {
            segDiagram.setYAxisLabelTextColor(Color.BLUE)
        }
    }
}
