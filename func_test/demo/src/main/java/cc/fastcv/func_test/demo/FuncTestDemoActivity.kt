package cc.fastcv.func_test.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.api_adapter.APIAdapterActivity
import cc.fastcv.file_manager.FileManagerMainActivity
import cc.fastcv.lib_test.LibTestMainActivity
import cc.fastcv.stage.StageActivity

class FuncTestDemoActivity : StageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_func_test_demo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<AppCompatButton>(R.id.bt1).setOnClickListener {
            startActivity(Intent(this, FileManagerMainActivity::class.java))
        }


        findViewById<AppCompatButton>(R.id.bt2).setOnClickListener {
            startActivity(Intent(this, APIAdapterActivity::class.java))
        }

        findViewById<AppCompatButton>(R.id.bt3).setOnClickListener {
            startActivity(Intent(this, LibTestMainActivity::class.java))
        }




    }
}