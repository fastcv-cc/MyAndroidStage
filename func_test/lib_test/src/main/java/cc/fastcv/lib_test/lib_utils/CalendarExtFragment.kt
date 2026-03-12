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
import java.util.Calendar

class CalendarExtFragment : Fragment() {
    
    private lateinit var tvResult: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_ext, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvResult = view.findViewById(R.id.tv_result)
        
        setupTests(view)
    }
    
    private fun setupTests(view: View) {
        val calendar = Calendar.getInstance()
        
        // 测试1：获取所有日期时间信息
        view.findViewById<Button>(R.id.bt_test_all_info).setOnClickListener {
            val info = buildString {
                append("=== Calendar 扩展测试 ===\n\n")
                append("年份：${calendar.getYearExt()}\n")
                append("月份：${calendar.getMonthExt() + 1}\n")
                append("日期：${calendar.getDayExt()}\n")
                append("小时：${calendar.getHourExt()}\n")
                append("分钟：${calendar.getMinuteExt()}\n")
                append("秒数：${calendar.getSecondExt()}\n")
                append("上午/下午：${if (calendar.isAMExt()) "上午" else "下午"}\n")
                append("星期索引：${calendar.getWeekIndexExt()}\n")
            }
            showResult(info)
        }
        
        // 测试2：获取年份
        view.findViewById<Button>(R.id.bt_test_year).setOnClickListener {
            showResult("当前年份：${calendar.getYearExt()}")
        }
        
        // 测试3：获取月份
        view.findViewById<Button>(R.id.bt_test_month).setOnClickListener {
            showResult("当前月份：${calendar.getMonthExt() + 1} (注意：Calendar.MONTH 从0开始)")
        }
        
        // 测试4：获取日期
        view.findViewById<Button>(R.id.bt_test_day).setOnClickListener {
            showResult("当前日期：${calendar.getDayExt()}")
        }
        
        // 测试5：获取小时
        view.findViewById<Button>(R.id.bt_test_hour).setOnClickListener {
            showResult("当前小时：${calendar.getHourExt()} (24小时制)")
        }
        
        // 测试6：获取分钟
        view.findViewById<Button>(R.id.bt_test_minute).setOnClickListener {
            showResult("当前分钟：${calendar.getMinuteExt()}")
        }
        
        // 测试7：获取秒数
        view.findViewById<Button>(R.id.bt_test_second).setOnClickListener {
            showResult("当前秒数：${calendar.getSecondExt()}")
        }
        
        // 测试8：判断上午/下午
        view.findViewById<Button>(R.id.bt_test_am_pm).setOnClickListener {
            val isAM = calendar.isAMExt()
            showResult("当前时间：${if (isAM) "上午 (AM)" else "下午 (PM)"}")
        }
        
        // 测试9：获取星期索引
        view.findViewById<Button>(R.id.bt_test_week_index).setOnClickListener {
            val weekIndex = calendar.getWeekIndexExt()
            val weekNames = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日")
            showResult("星期索引：$weekIndex\n对应：${weekNames[weekIndex]}")
        }
        
        // 测试10：格式化日期
        view.findViewById<Button>(R.id.bt_test_format).setOnClickListener {
            val formats = listOf(
                "yyyy-MM-dd",
                "yyyy-MM-dd HH:mm:ss",
                "MM/dd/yyyy",
                "HH:mm:ss"
            )
            val info = buildString {
                append("日期格式化测试：\n\n")
                formats.forEach { format ->
                    append("$format\n")
                    append("  ${calendar.formatExt(format)}\n\n")
                }
            }
            showResult(info)
        }
        
        // 测试11：判断是否同一天
        view.findViewById<Button>(R.id.bt_test_same_day).setOnClickListener {
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -1)
            }
            val tomorrow = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 1)
            }
            
            val info = buildString {
                append("同一天判断测试：\n\n")
                append("今天 vs 今天：${today.isSameDayExt(today)}\n")
                append("今天 vs 昨天：${today.isSameDayExt(yesterday)}\n")
                append("今天 vs 明天：${today.isSameDayExt(tomorrow)}\n")
            }
            showResult(info)
        }
    }
    
    private fun showResult(text: String) {
        tvResult.text = text
    }
}
