package cc.fastcv.lib_test.lib_utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import cc.fastcv.lib_test.R
import cc.fastcv.lib_utils.*

class ContextExtFragment : Fragment() {
    
    private lateinit var tvResult: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_context_ext, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvResult = view.findViewById(R.id.tv_result)
        
        setupTests(view)
    }
    
    private fun setupTests(view: View) {
        val context = requireContext()
        
        // 测试1：获取所有基本信息
        view.findViewById<Button>(R.id.bt_test_all_info).setOnClickListener {
            val info = buildString {
                append("=== Context 扩展测试 ===\n\n")
                append("语言：${context.getLocaleLanguageExt()}\n")
                append("国家码：${context.getCountryZipCodeExt()}\n")
                append("版本名称：${context.getVersionNameExt()}\n")
                append("版本号：${context.getVersionCodeExt()}\n")
                append("设备ID：${context.getDeviceIdentifierExt()}\n")
            }
            showResult(info)
        }
        
        // 测试2：dp/px 转换
        view.findViewById<Button>(R.id.bt_test_dp_px).setOnClickListener {
            val dpValue = 16f
            val pxValue = 48f
            val info = buildString {
                append("单位转换测试：\n\n")
                append("${dpValue}dp = ${context.dip2pxExt(dpValue)}px\n")
                append("${pxValue}px = ${context.px2dpExt(pxValue)}dp\n")
                append("${pxValue}px = ${context.px2dpiExt(pxValue)}dpi\n")
            }
            showResult(info)
        }
        
        // 测试3：sp/px 转换
        view.findViewById<Button>(R.id.bt_test_sp_px).setOnClickListener {
            val spValue = 14f
            val pxValue = 42f
            val info = buildString {
                append("字体单位转换测试：\n\n")
                append("${spValue}sp = ${context.sp2pxExt(spValue)}px\n")
                append("${pxValue}px = ${context.px2spExt(pxValue)}sp\n")
            }
            showResult(info)
        }
        
        // 测试4：获取语言
        view.findViewById<Button>(R.id.bt_test_language).setOnClickListener {
            showResult("当前语言：${context.getLocaleLanguageExt()}")
        }
        
        // 测试5：获取国家码
        view.findViewById<Button>(R.id.bt_test_country).setOnClickListener {
            showResult("国家码：${context.getCountryZipCodeExt()}")
        }
        
        // 测试6：获取版本信息
        view.findViewById<Button>(R.id.bt_test_version).setOnClickListener {
            val info = buildString {
                append("应用版本信息：\n\n")
                append("版本名称：${context.getVersionNameExt()}\n")
                append("版本号：${context.getVersionCodeExt()}\n")
            }
            showResult(info)
        }
        
        // 测试7：获取设备ID
        view.findViewById<Button>(R.id.bt_test_device_id).setOnClickListener {
            showResult("设备ID：\n${context.getDeviceIdentifierExt()}")
        }
        
        // 测试8：GPS 状态
        view.findViewById<Button>(R.id.bt_test_gps).setOnClickListener {
            val status = context.obtainGPSSwitchStatusExt()
            showResult("GPS 状态：${if (status) "已开启" else "已关闭"}")
        }
        
        // 测试9：蓝牙状态
        view.findViewById<Button>(R.id.bt_test_bluetooth).setOnClickListener {
            val status = context.obtainBluetoothSwitchStatusExt()
            showResult("蓝牙状态：${if (status) "已开启" else "已关闭"}")
        }
        
        // 测试10：通知权限状态
        view.findViewById<Button>(R.id.bt_test_notification).setOnClickListener {
            val status = context.isNotificationEnabledExt()
            showResult("通知权限：${if (status) "已开启" else "已关闭"}")
        }
        
        // 测试11：通知监听服务状态
        view.findViewById<Button>(R.id.bt_test_notification_listener).setOnClickListener {
            val status = context.getNotificationListenerServerStatusExt()
            showResult("通知监听服务：${if (status) "已开启" else "已关闭"}")
        }
        
        // 测试12：打开通知监听授权页面
        view.findViewById<Button>(R.id.bt_open_notification_settings).setOnClickListener {
            try {
                context.openNotificationAccessAuthorizationActivityExt()
                showResult("已跳转到通知监听授权页面")
            } catch (e: Exception) {
                showResult("跳转失败：${e.message}")
            }
        }
    }
    
    private fun showResult(text: String) {
        tvResult.text = text
    }
}
