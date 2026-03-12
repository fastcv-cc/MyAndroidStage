package cc.fastcv.lib_test.lib_utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import cc.fastcv.lib_test.R
import cc.fastcv.lib_utils.PhoneInfoSupport

class PhoneInfoFragment : Fragment() {
    
    private lateinit var tvResult: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phone_info, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvResult = view.findViewById(R.id.tv_result)
        
        setupTests(view)
    }
    
    private fun setupTests(view: View) {
        // 获取所有信息
        view.findViewById<Button>(R.id.bt_get_all_info).setOnClickListener {
            val info = buildString {
                append("=== 设备完整信息 ===\n\n")
                append("Android 版本：${PhoneInfoSupport.getPhoneVersion()}\n")
                append("手机厂商：${PhoneInfoSupport.getManufacturer()}\n")
                append("手机品牌：${PhoneInfoSupport.getProduct()}\n")
                append("设备型号：${PhoneInfoSupport.getModel()}\n")
                append("设备名称：${PhoneInfoSupport.getDevice()}\n")
                append("显示信息：${PhoneInfoSupport.getDisplayInfo()}\n")
                append("\nCPU 架构：\n")
                PhoneInfoSupport.getSupportedABIs().forEachIndexed { index, abi ->
                    append("  ${index + 1}. $abi\n")
                }
            }
            showResult(info)
        }
        
        // 单独测试各项
        view.findViewById<Button>(R.id.bt_get_version).setOnClickListener {
            val version = PhoneInfoSupport.getPhoneVersion()
            showResult("Android 版本：API Level $version")
        }
        
        view.findViewById<Button>(R.id.bt_get_manufacturer).setOnClickListener {
            val manufacturer = PhoneInfoSupport.getManufacturer()
            showResult("手机厂商：$manufacturer")
        }
        
        view.findViewById<Button>(R.id.bt_get_product).setOnClickListener {
            val product = PhoneInfoSupport.getProduct()
            showResult("手机品牌：$product")
        }
        
        view.findViewById<Button>(R.id.bt_get_model).setOnClickListener {
            val model = PhoneInfoSupport.getModel()
            showResult("设备型号：$model")
        }
        
        view.findViewById<Button>(R.id.bt_get_device).setOnClickListener {
            val device = PhoneInfoSupport.getDevice()
            showResult("设备名称：$device")
        }
        
        view.findViewById<Button>(R.id.bt_get_display).setOnClickListener {
            val display = PhoneInfoSupport.getDisplayInfo()
            showResult("显示信息：$display")
        }
        
        view.findViewById<Button>(R.id.bt_get_abis).setOnClickListener {
            val abis = PhoneInfoSupport.getSupportedABIs()
            val info = buildString {
                append("支持的 CPU 架构：\n\n")
                abis.forEachIndexed { index, abi ->
                    append("${index + 1}. $abi\n")
                }
            }
            showResult(info)
        }
    }
    
    private fun showResult(text: String) {
        tvResult.text = text
    }
}
