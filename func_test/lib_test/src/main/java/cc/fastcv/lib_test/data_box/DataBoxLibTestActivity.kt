package cc.fastcv.lib_test.data_box

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import cc.fastcv.data_box.DataBox
import cc.fastcv.data_box.DataBoxManager
import cc.fastcv.data_box.OnValueChangeListener
import cc.fastcv.lib_test.R
import cc.fastcv.stage.StageActivity
import kotlinx.coroutines.launch
import kotlin.random.Random

class DataBoxLibTestActivity : StageActivity(), OnValueChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_box_lib_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        DataBoxManager.init(application)

        val dataBox = DataBoxManager.getDataBoxByName("test")

        findViewById<AppCompatButton>(R.id.bt1).setOnClickListener {
            lifecycleScope.launch {
                //存储数据
                dataBox.saveInt("Key", Random.nextInt(1,100))
            }
        }

        findViewById<AppCompatButton>(R.id.bt2).setOnClickListener {
            //读取数据
            lifecycleScope.launch {
                //存储数据
                val value = dataBox.getInt("Key",0)
                Log.d("DataBoxLibTest", "onCreate: value = $value")
            }
        }

        findViewById<AppCompatButton>(R.id.bt3).setOnClickListener {
            //监听数据
            lifecycleScope.launch {
                dataBox.addOnValueChangedListener(this@DataBoxLibTestActivity)
            }
        }
    }

    override fun onValueChanged(dataBox: DataBox, key: String) {
        if (key == "Key") {
            lifecycleScope.launch {
                //存储数据
                val value = dataBox.getInt("Key",0)
                Log.d("DataBoxLibTest", "onCreate: value = $value")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            DataBoxManager.getDataBoxByName("test").removeOnValueChangedListener(this@DataBoxLibTestActivity)
        }
    }
}