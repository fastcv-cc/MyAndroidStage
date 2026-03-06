package cc.fastcv.api_adapter

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.api_adapter.api.getAllActivities
import cc.fastcv.api_adapter.api.getAllServices
import cc.fastcv.api_adapter.api.startCallPhone
import cc.fastcv.api_adapter.api.startCompatAppApplicationInfoActivity
import cc.fastcv.api_adapter.api.startCompatSystemNotificationSettingActivity
import cc.fastcv.api_adapter.api.startSelfPageInGooglePlay

class APIAdapterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_api_adapter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<AppCompatButton>(R.id.bt1).setOnClickListener {
            getAllActivities(this).forEach {
                Log.d("APIAdapterActivity", "onCreate: $it")
            }
        }

        findViewById<AppCompatButton>(R.id.bt2).setOnClickListener {
            getAllServices(this).forEach {
                Log.d("APIAdapterActivity", "onCreate: $it")
            }
        }

        findViewById<AppCompatButton>(R.id.bt3).setOnClickListener {
            startCompatAppApplicationInfoActivity(this)
        }

        findViewById<AppCompatButton>(R.id.bt4).setOnClickListener {
            startSelfPageInGooglePlay(this)
        }

        findViewById<AppCompatButton>(R.id.bt5).setOnClickListener {
            startCallPhone(this,"110")
        }

        findViewById<AppCompatButton>(R.id.bt6).setOnClickListener {
            startCompatSystemNotificationSettingActivity(this)
        }

    }
}