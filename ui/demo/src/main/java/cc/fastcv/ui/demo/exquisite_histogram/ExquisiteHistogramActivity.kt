package cc.fastcv.ui.demo.exquisite_histogram

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.exquisite_histogram.ExquisiteHistogramView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R

class ExquisiteHistogramActivity : StageActivity() {

    private lateinit var histogram: ExquisiteHistogramView
    private lateinit var edData: EditText
    private lateinit var edMap: EditText

    private var data = mutableListOf(
        245,
        576,
        973,
        452,
        12,
        543,
        245,
        576,
        973,
        452,
        12,
        543,
        245,
        576,
        973,
        452,
        12,
        543,
        245,
        576,
        973,
        452,
        12,
        543,
        -1,
        100
    )

    private var list = mutableListOf(
        Pair(0,"0"),
        Pair(6,"6"),
        Pair(12,"12"),
        Pair(18,"18"),
        Pair(24,"0"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exquisite_histogram)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        histogram = findViewById(R.id.histogram)
        histogram.setData(data.map { it.toFloat() },list)
        edData = findViewById(R.id.ed_data)
        edMap = findViewById(R.id.ed_map)
        edData.setText(data.joinToString().trim())
        val sb = StringBuilder()
        for (entry in list) {
            sb.append("${entry.first}:${entry.second},")
        }
        val str = sb.toString()
        val removeRange = str.removeRange(str.length - 1, str.length)
        edData.setText(data.joinToString().trim())
        edMap.setText(removeRange)


        initHistogramWidth()
        initSideBarWidth()
        initSyncData()
        initSyncEqualSize()
        initSyncMap()
        initLtr()
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

    private fun initSyncData() {
        findViewById<Button>(R.id.bt_sync_data).setOnClickListener {
            val dataList = edData.text.toString().split(",")
            try {
                val temp = dataList.map { it.trim().toInt() }
                data.clear()
                data.addAll(temp)
                histogram.setData(data.map { it.toFloat() },list)
            } catch (e: Exception) {
                Toast.makeText(this, "数据格式错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSyncMap() {
        findViewById<Button>(R.id.bt_sync_map).setOnClickListener {
            val dataList = edMap.text.toString().split(",")
            try {
                list.clear()
                for (s in dataList) {
                    val split = s.split(":")
                    val key = split[0].toInt()
                    val value = split[1]
                    list.add(Pair(key,value))
                }
                histogram.setData(data.map { it.toFloat() },list)
            } catch (e: Exception) {
                Toast.makeText(this, "数据格式错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initHistogramWidth() {
        findViewById<SeekBar>(R.id.sb_histogram_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    histogram.setHistogramWidth(progress.toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}