package cc.fastcv.ui.demo.date_switch_view

import android.graphics.Color
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
import cc.fastcv.date_switch_view.DateInfo
import cc.fastcv.date_switch_view.DateSelectCallback
import cc.fastcv.date_switch_view.DateSwitchView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateSwitchViewActivity : StageActivity(), DateSelectCallback {

    private lateinit var dsv: DateSwitchView
    private lateinit var tvShow: TextView
    private lateinit var tvSelectedInfo: TextView
    
    // 显示当前值的 TextView
    private lateinit var tvMarginValue: TextView
    private lateinit var tvWidthValue: TextView
    private lateinit var tvHeightValue: TextView
    private lateinit var tvTextSizeValue: TextView
    private lateinit var tvWeekMarginValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_date_switch_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initDefaultDateRange()
        initAllControls()
    }
    
    private fun initViews() {
        dsv = findViewById(R.id.dsv)
        dsv.setDateSelectCallback(this)
        
        tvShow = findViewById(R.id.tv_show)
        tvSelectedInfo = findViewById(R.id.tv_selected_info)
        
        tvMarginValue = findViewById(R.id.tv_margin_value)
        tvWidthValue = findViewById(R.id.tv_width_value)
        tvHeightValue = findViewById(R.id.tv_height_value)
        tvTextSizeValue = findViewById(R.id.tv_text_size_value)
        tvWeekMarginValue = findViewById(R.id.tv_week_margin_value)
    }
    
    private fun initDefaultDateRange() {
        // 默认设置从2020年到今天
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        dsv.setDateRange("2020-01-01", today)
    }
    
    private fun initAllControls() {
        initDateRangeControls()
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
        initResetButton()
    }
    
    private fun initDateRangeControls() {
        val edStartDate = findViewById<EditText>(R.id.ed_start_date)
        val edEndDate = findViewById<EditText>(R.id.ed_end_date)
        val btSetDateRange = findViewById<Button>(R.id.bt_set_date_range)
        val btSetToday = findViewById<Button>(R.id.bt_set_today)
        
        // 设置默认值
        edStartDate.setText("2020-01-01")
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        edEndDate.setText(today)
        
        // 设置日期范围
        btSetDateRange.setOnClickListener {
            val startDate = edStartDate.text.toString()
            val endDate = edEndDate.text.toString()
            
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "日期不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
                Toast.makeText(this, "日期格式错误！请使用 yyyy-MM-dd 格式", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            try {
                dsv.setDateRange(startDate, endDate)
                Toast.makeText(this, "日期范围设置成功", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "日期范围设置失败：${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 快速设置到今天
        btSetToday.setOnClickListener {
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            edEndDate.setText(todayStr)
        }
    }
    
    private fun isValidDateFormat(date: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.isLenient = false
            format.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    private fun initResetButton() {
        findViewById<Button>(R.id.bt_reset).setOnClickListener {
            // 重置所有配置到默认值
            findViewById<SeekBar>(R.id.sb_margin).progress = 6
            findViewById<SeekBar>(R.id.sb_width).progress = 160
            findViewById<SeekBar>(R.id.sb_height).progress = 40
            findViewById<SeekBar>(R.id.sb_text_size).progress = 15
            findViewById<SeekBar>(R.id.sb_week_text_margin).progress = 10
            
            findViewById<EditText>(R.id.ed_tab1).setText("")
            findViewById<EditText>(R.id.ed_tab2).setText("")
            findViewById<EditText>(R.id.ed_select_color).setText("F9F9F9")
            findViewById<EditText>(R.id.ed_color).setText("61003324")
            findViewById<EditText>(R.id.ed_duration).setText("250")
            
            findViewById<SwitchCompat>(R.id.sc_week_start).isChecked = false
            findViewById<SwitchCompat>(R.id.sc_ltr).isChecked = false
            
            // 重新设置日期范围
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            dsv.setDateRange("2020-01-01", today)
            
            Toast.makeText(this, "已重置为默认配置", Toast.LENGTH_SHORT).show()
        }
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
                Toast.makeText(this, "时间不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val duration = durationStr.toLong()
                dsv.setAnimationDuration(duration)
                Toast.makeText(this, "动画时长已设置为 ${duration}ms", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "时间格式错误！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initNormalColor() {
        findViewById<Button>(R.id.bt_sync_color).setOnClickListener {
            val color = findViewById<EditText>(R.id.ed_color).text.toString()
            if (color.isEmpty()) {
                Toast.makeText(this, "颜色不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val colorValue = Color.parseColor("#$color")
                dsv.setTabTextColor(colorValue)
                Toast.makeText(this, "未选中文字颜色已更新", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "颜色格式错误！请使用十六进制格式", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSelectColor() {
        findViewById<Button>(R.id.bt_sync_select_color).setOnClickListener {
            val color = findViewById<EditText>(R.id.ed_select_color).text.toString()
            if (color.isEmpty()) {
                Toast.makeText(this, "颜色不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val colorValue = Color.parseColor("#$color")
                dsv.setTabTextSelectedColor(colorValue)
                Toast.makeText(this, "选中文字颜色已更新", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "颜色格式错误！请使用十六进制格式", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initTab2Text() {
        findViewById<Button>(R.id.bt_sync_text_2).setOnClickListener {
            val text = findViewById<EditText>(R.id.ed_tab2).text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, "文本不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dsv.setTabMonthText(text)
            Toast.makeText(this, "月Tab文本已更新", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initTab1Text() {
        findViewById<Button>(R.id.bt_sync_text_1).setOnClickListener {
            val text = findViewById<EditText>(R.id.ed_tab1).text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, "文本不能为空！", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dsv.setTabDayText(text)
            Toast.makeText(this, "日Tab文本已更新", Toast.LENGTH_SHORT).show()
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
                    tvWeekMarginValue.text = "${progress}dp"
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
                    tvTextSizeValue.text = "${progress}sp"
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
                    tvHeightValue.text = "${progress}dp"
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
                    tvWidthValue.text = "${progress}dp"
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
                    tvMarginValue.text = "${progress}dp"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    override fun onDateSelect(info: DateInfo.DayInfoDetail) {
        // 显示选中的日期
        tvShow.text = info.getDateYmd()
        
        // 显示详细信息
        val detailInfo = buildString {
            append("年份: ${info.year}\n")
            append("月份: ${info.month}\n")
            if (info.day != -1) {
                append("日期: ${info.day}\n")
            }
            append("是否今天: ${if (info.isToday) "是" else "否"}\n")
            append("是否选中: ${if (info.isSelected) "是" else "否"}\n")
            append("是否无效: ${if (info.isInvalid) "是" else "否"}")
        }
        tvSelectedInfo.text = detailInfo
    }
}