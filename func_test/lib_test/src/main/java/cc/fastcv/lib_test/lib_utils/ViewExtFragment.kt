package cc.fastcv.lib_test.lib_utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import cc.fastcv.lib_test.R
import cc.fastcv.lib_utils.clickWithTrigger

class ViewExtFragment : Fragment() {
    
    private lateinit var tvResult: TextView
    private lateinit var edDelay: EditText
    private var clickCount = 0
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_ext, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvResult = view.findViewById(R.id.tv_result)
        edDelay = view.findViewById(R.id.ed_delay)
        
        setupTests(view)
    }
    
    private fun setupTests(view: View) {
        // 测试1：默认延迟（500ms）
        view.findViewById<Button>(R.id.bt_test_default_delay).clickWithTrigger {
            clickCount++
            showResult("默认延迟测试 (500ms)\n点击次数：$clickCount\n\n快速连续点击此按钮，观察是否有防抖效果")
        }
        
        // 测试2：自定义延迟
        view.findViewById<Button>(R.id.bt_test_custom_delay).setOnClickListener {
            val delay = edDelay.text.toString().toLongOrNull() ?: 1000L
            
            // 重新设置点击监听器
            view.findViewById<Button>(R.id.bt_click_test).clickWithTrigger(delay) { btn ->
                clickCount++
                showResult("自定义延迟测试 (${delay}ms)\n点击次数：$clickCount\n\n快速连续点击测试按钮，观察防抖效果")
            }
            
            showResult("已设置延迟为 ${delay}ms\n请点击下方的「测试按钮」进行测试")
        }
        
        // 测试3：重置计数器
        view.findViewById<Button>(R.id.bt_reset).setOnClickListener {
            clickCount = 0
            showResult("计数器已重置")
        }
        
        // 测试按钮（用于自定义延迟测试）
        view.findViewById<Button>(R.id.bt_click_test).clickWithTrigger(1000) {
            clickCount++
            showResult("测试按钮点击\n点击次数：$clickCount")
        }
        
        // 测试4：对比普通点击
        var normalClickCount = 0
        view.findViewById<Button>(R.id.bt_normal_click).setOnClickListener {
            normalClickCount++
            showResult("普通点击（无防抖）\n点击次数：$normalClickCount\n\n快速连续点击此按钮，观察与防抖按钮的区别")
        }
        
        // 测试5：短延迟测试（100ms）
        var shortDelayCount = 0
        view.findViewById<Button>(R.id.bt_short_delay).clickWithTrigger(100) {
            shortDelayCount++
            showResult("短延迟测试 (100ms)\n点击次数：$shortDelayCount\n\n快速连续点击此按钮，观察防抖效果")
        }
        
        // 测试6：长延迟测试（2000ms）
        var longDelayCount = 0
        view.findViewById<Button>(R.id.bt_long_delay).clickWithTrigger(2000) {
            longDelayCount++
            showResult("长延迟测试 (2000ms)\n点击次数：$longDelayCount\n\n快速连续点击此按钮，观察防抖效果")
        }
        
        // 显示说明
        showResult("""
            View 扩展测试 - clickWithTrigger
            
            这个扩展函数用于防止按钮连击，通过设置延迟时间来控制点击频率。
            
            使用方法：
            button.clickWithTrigger(delay = 500) {
                // 点击事件处理
            }
            
            请点击上方按钮进行测试
        """.trimIndent())
    }
    
    private fun showResult(text: String) {
        tvResult.text = text
    }
}
