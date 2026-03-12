package cc.fastcv.lib_test

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.lib_test.data_box.DataBoxLibTestActivity
import cc.fastcv.lib_test.lib_utils.LibUtilsTestActivity
import cc.fastcv.stage.StageActivity

class LibTestMainActivity : StageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lib_test_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<AppCompatButton>(R.id.bt1).setOnClickListener {
            startActivity(Intent(this, DataBoxLibTestActivity::class.java))
        }
        
        findViewById<AppCompatButton>(R.id.bt2).setOnClickListener {
            startActivity(Intent(this, LibUtilsTestActivity::class.java))
        }
    }
}