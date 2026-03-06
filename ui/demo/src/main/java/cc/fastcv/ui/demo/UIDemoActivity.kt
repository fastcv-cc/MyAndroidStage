package cc.fastcv.ui.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.date_switch_view.DateSwitchViewActivity
import cc.fastcv.ui.demo.exquisite_histogram.ExquisiteHistogramActivity
import cc.fastcv.ui.demo.flash_text_view.FlashTextViewActivity
import cc.fastcv.ui.demo.line_number_clock.LineNumberClockActivity
import cc.fastcv.ui.demo.modern_cardview.ModernCardViewActivity

class UIDemoActivity : StageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ui_demo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<AppCompatButton>(R.id.bt1).setOnClickListener {
            startActivity(Intent(this, FlashTextViewActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.bt2).setOnClickListener {
            startActivity(Intent(this, LineNumberClockActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.bt3).setOnClickListener {
            startActivity(Intent(this, ExquisiteHistogramActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.bt4).setOnClickListener {
            startActivity(Intent(this, DateSwitchViewActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.bt5).setOnClickListener {
            startActivity(Intent(this, ModernCardViewActivity::class.java))
        }
    }
}