package cc.fastcv.ui.demo.date_switch_view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.date_switch_view.DateInfo
import cc.fastcv.date_switch_view.DateSelectCallback
import cc.fastcv.date_switch_view.DateSwitchView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R

class DateSwitchViewActivity : StageActivity(), DateSelectCallback {

    private lateinit var dsv: DateSwitchView
    private lateinit var tvShow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_date_switch_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dsv = findViewById(R.id.dsv)
        dsv.setDateRange("2020-03-10", "2023-03-16")
        dsv.setDateSelectCallback(this)

        tvShow = findViewById(R.id.tv_show)

        initMarginSeekbar()
        initMarginWidth()
        initMarginHeight()
        initTextSize()
        initWeekTextMargin()
        initTab1Text()
        initTab2Text()
        initSelectColor()
        initNormalColor()
        initAnimDuration()
        initWeekStart()
        initLtr()
    }

    private fun initLtr() {
        findViewById<SwitchCompat>(R.id.sc_ltr).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dsv.layoutDirection = View.LAYOUT_DIRECTION_RTL
            } else {
                dsv.layoutDirection = View.LAYOUT_DIRECTION_LTR
            }
        }
    }

    private fun initWeekStart() {
        findViewById<SwitchCompat>(R.id.sc_week_start).setOnCheckedChangeListener { _, isChecked ->
            dsv.setWeekStartWithMonday(isChecked)
        }
    }


    private fun initAnimDuration() {
        findViewById<Button>(R.id.bt_sync_duration).setOnClickListener {
            val durationStr = findViewById<EditText>(R.id.ed_duration).text.toString()
            if (durationStr.isEmpty()) {
                Toast.makeText(this, "时间不能为空！！！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val duration = durationStr.toLong()
                dsv.setAnimationDuration(duration)
            } catch (e: Exception) {
                Toast.makeText(this, "时间格式错误！！！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initNormalColor() {
        findViewById<Button>(R.id.bt_sync_color).setOnClickListener {
            val color = findViewById<EditText>(R.id.ed_color).text.toString()
            if (color.isEmpty()) {
                Toast.makeText(this, "颜色不能为空！！！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val colorValue = Color.parseColor("#$color")
                dsv.setTabTextColor(colorValue)
            } catch (e: Exception) {
                Toast.makeText(this, "颜色格式错误！！！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSelectColor() {
        findViewById<Button>(R.id.bt_sync_select_color).setOnClickListener {
            val color = findViewById<EditText>(R.id.ed_select_color).text.toString()
            if (color.isEmpty()) {
                Toast.makeText(this, "颜色不能为空！！！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val colorValue = Color.parseColor("#$color")
                dsv.setTabTextSelectedColor(colorValue)
            } catch (e: Exception) {
                Toast.makeText(this, "颜色格式错误！！！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initTab2Text() {
        findViewById<Button>(R.id.bt_sync_text_2).setOnClickListener {
            val text = findViewById<EditText>(R.id.ed_tab2).text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, "文本不能为空！！！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dsv.setTabMonthText(text)
        }
    }

    private fun initTab1Text() {
        findViewById<Button>(R.id.bt_sync_text_1).setOnClickListener {
            val text = findViewById<EditText>(R.id.ed_tab1).text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, "文本不能为空！！！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dsv.setTabDayText(text)
        }
    }

    private fun initWeekTextMargin() {
        findViewById<SeekBar>(R.id.sb_week_text_margin).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    dsv.setWeekTextMargin(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initTextSize() {
        findViewById<SeekBar>(R.id.sb_text_size).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    dsv.setTabTextSize(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initMarginHeight() {
        findViewById<SeekBar>(R.id.sb_height).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    dsv.setCustomizeTabHeight(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initMarginWidth() {
        findViewById<SeekBar>(R.id.sb_width).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    dsv.setCustomizeTabWidth(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun initMarginSeekbar() {
        findViewById<SeekBar>(R.id.sb_margin).apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    dsv.setCustomizeTabMargin(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    override fun onDateSelect(info: DateInfo.DayInfoDetail) {
        tvShow.text = info.getDateYmd()
    }
}