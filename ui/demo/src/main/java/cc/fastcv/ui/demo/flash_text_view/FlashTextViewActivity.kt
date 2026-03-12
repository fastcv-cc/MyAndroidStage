package cc.fastcv.ui.demo.flash_text_view

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.flash_text_view.FlashTextView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R

class FlashTextViewActivity : StageActivity() {
    
    private lateinit var flashTextView1: FlashTextView
    private lateinit var flashTextView2: FlashTextView
    private lateinit var flashTextView3: FlashTextView
    private lateinit var customFlashTextView: FlashTextView
    
    private lateinit var tvSpeedValue: TextView
    private lateinit var tvDelayValue: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flash_text_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initViews()
        setupControls()
    }
    
    private fun initViews() {
        flashTextView1 = findViewById(R.id.flash_text_1)
        flashTextView2 = findViewById(R.id.flash_text_2)
        flashTextView3 = findViewById(R.id.flash_text_3)
        customFlashTextView = findViewById(R.id.custom_flash_text)
        
        tvSpeedValue = findViewById(R.id.tv_speed_value)
        tvDelayValue = findViewById(R.id.tv_delay_value)
    }
    
    private fun setupControls() {
        // 文本内容控制
        val edText = findViewById<EditText>(R.id.ed_text)
        val btSetText = findViewById<Button>(R.id.bt_set_text)
        btSetText.setOnClickListener {
            val text = edText.text.toString()
            if (text.isNotEmpty()) {
                customFlashTextView.text = text
            }
        }
        
        // 文字大小控制
        val sbTextSize = findViewById<SeekBar>(R.id.sb_text_size)
        sbTextSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                customFlashTextView.textSize = progress.toFloat()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        // 文字颜色控制
        val edTextColor = findViewById<EditText>(R.id.ed_text_color)
        val btSetTextColor = findViewById<Button>(R.id.bt_set_text_color)
        btSetTextColor.setOnClickListener {
            val colorStr = edTextColor.text.toString()
            if (colorStr.isNotEmpty()) {
                try {
                    val color = Color.parseColor("#$colorStr")
                    customFlashTextView.setTextColor(color)
                } catch (e: Exception) {
                    // 颜色格式错误
                }
            }
        }
        
        // 背景颜色控制
        val edBgColor = findViewById<EditText>(R.id.ed_bg_color)
        val btSetBgColor = findViewById<Button>(R.id.bt_set_bg_color)
        btSetBgColor.setOnClickListener {
            val colorStr = edBgColor.text.toString()
            if (colorStr.isNotEmpty()) {
                try {
                    val color = Color.parseColor("#$colorStr")
                    customFlashTextView.setBackgroundColor(color)
                } catch (e: Exception) {
                    // 颜色格式错误
                }
            }
        }
        
        // 闪烁颜色控制
        val edFlashColor = findViewById<EditText>(R.id.ed_flash_color)
        val btSetFlashColor = findViewById<Button>(R.id.bt_set_flash_color)
        btSetFlashColor.setOnClickListener {
            val colorStr = edFlashColor.text.toString()
            if (colorStr.isNotEmpty()) {
                try {
                    val color = Color.parseColor("#$colorStr")
                    customFlashTextView.setFlashColor(color)
                } catch (e: Exception) {
                    // 颜色格式错误
                }
            }
        }
        
        // 动画速度控制
        val sbSpeed = findViewById<SeekBar>(R.id.sb_speed)
        sbSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speed = progress.coerceIn(1, 20)
                customFlashTextView.setAnimationSpeed(speed)
                tvSpeedValue.text = "$speed"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        // 刷新延迟控制
        val sbDelay = findViewById<SeekBar>(R.id.sb_delay)
        sbDelay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val delay = (progress + 50).toLong()
                customFlashTextView.setRefreshDelay(delay)
                tvDelayValue.text = "${delay}ms"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        
        // 渐变模式控制
        val rgGradientMode = findViewById<RadioGroup>(R.id.rg_gradient_mode)
        rgGradientMode.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_mode_0 -> customFlashTextView.setGradientMode(0)
                R.id.rb_mode_1 -> customFlashTextView.setGradientMode(1)
                R.id.rb_mode_2 -> customFlashTextView.setGradientMode(2)
            }
        }
        
        // 预设样式按钮
        findViewById<Button>(R.id.bt_style_1).setOnClickListener {
            customFlashTextView.apply {
                text = "闪烁效果样式 1"
                textSize = 24f
                setTextColor(Color.parseColor("#FF5722"))
                setBackgroundColor(Color.parseColor("#FFF3E0"))
                setFlashColor(Color.parseColor("#FFEB3B"))
                setAnimationSpeed(5)
                setRefreshDelay(100)
                setGradientMode(0)
            }
            updateControlValues(5, 100)
        }
        
        findViewById<Button>(R.id.bt_style_2).setOnClickListener {
            customFlashTextView.apply {
                text = "闪烁效果样式 2"
                textSize = 28f
                setTextColor(Color.parseColor("#2196F3"))
                setBackgroundColor(Color.parseColor("#E3F2FD"))
                setFlashColor(Color.parseColor("#00BCD4"))
                setAnimationSpeed(3)
                setRefreshDelay(80)
                setGradientMode(1)
            }
            updateControlValues(3, 80)
        }
        
        findViewById<Button>(R.id.bt_style_3).setOnClickListener {
            customFlashTextView.apply {
                text = "闪烁效果样式 3"
                textSize = 32f
                setTextColor(Color.parseColor("#4CAF50"))
                setBackgroundColor(Color.parseColor("#E8F5E9"))
                setFlashColor(Color.parseColor("#8BC34A"))
                setAnimationSpeed(8)
                setRefreshDelay(150)
                setGradientMode(2)
            }
            updateControlValues(8, 150)
        }
        
        // 重置按钮
        findViewById<Button>(R.id.bt_reset).setOnClickListener {
            customFlashTextView.apply {
                text = "自定义闪烁文本"
                textSize = 20f
                setTextColor(Color.parseColor("#333333"))
                setBackgroundColor(Color.parseColor("#FFFFFF"))
                setFlashColor(0xFFFFFF)
                setAnimationSpeed(5)
                setRefreshDelay(100)
                setGradientMode(0)
            }
            edText.setText("")
            edTextColor.setText("333333")
            edBgColor.setText("FFFFFF")
            edFlashColor.setText("FFFFFF")
            sbTextSize.progress = 20
            sbSpeed.progress = 5
            sbDelay.progress = 50
            rgGradientMode.check(R.id.rb_mode_0)
            updateControlValues(5, 100)
        }
    }
    
    private fun updateControlValues(speed: Int, delay: Long) {
        tvSpeedValue.text = "$speed"
        tvDelayValue.text = "${delay}ms"
    }
}