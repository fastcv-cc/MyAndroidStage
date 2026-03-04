package cc.fastcv.api_adapter

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            getActivities(this).forEach {
                Log.d("APIAdapterActivity", "onCreate: $it")
            }
        }

    }


    private fun getActivities(context: Context): MutableList<ActivityInfo?> {
        val activities: MutableList<ActivityInfo?> = ArrayList()
        val resolveInfos = context.packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN), 0
        )
        for (resolveInfo in resolveInfos) {
            if (resolveInfo.activityInfo != null) {
                activities.add(resolveInfo.activityInfo)
            }
        }
        return activities
    }
}