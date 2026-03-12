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
import cc.fastcv.lib_utils.ZonedDateTimeUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZonedDateTimeFragment : Fragment() {
    
    private lateinit var tvResult: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_zoned_datetime, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvResult = view.findViewById(R.id.tv_result)
        
        setupTests(view)
    }
    
    private fun setupTests(view: View) {
        // 测试1：日期偏移
        view.findViewById<Button>(R.id.bt_test_offset_day).setOnClickListener {
            val dateStr = view.findViewById<EditText>(R.id.ed_date).text.toString()
            val days = view.findViewById<EditText>(R.id.ed_days).text.toString().toIntOrNull() ?: 0
            
            try {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val result = ZonedDateTimeUtil.getOffsetDateByDay(days, dateStr, format)
                showResult("日期偏移结果：\n原日期：$dateStr\n偏移 $days 天后：$result")
            } catch (e: Exception) {
                showResult("错误：${e.message}")
            }
        }
        
        // 测试2：时间格式转换
        view.findViewById<Button>(R.id.bt_test_convert_format).setOnClickListener {
            val dateTime = view.findViewById<EditText>(R.id.ed_datetime).text.toString()
            
            try {
                val parseFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val result = ZonedDateTimeUtil.convertDateTimeFormat(dateTime, parseFormat, targetFormat)
                showResult("格式转换结果：\n原格式：$dateTime\n新格式：$result")
            } catch (e: Exception) {
                showResult("错误：${e.message}")
            }
        }
        
        // 测试3：时间戳转换
        view.findViewById<Button>(R.id.bt_test_timestamp).setOnClickListener {
            val dateStr = view.findViewById<EditText>(R.id.ed_date_for_timestamp).text.toString()
            
            try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val timestamp = ZonedDateTimeUtil.convertDateTimeStrToTimestamp(dateStr, format)
                val backToStr = ZonedDateTimeUtil.convertTimestampToDateTimeStr(timestamp, format)
                showResult("时间戳转换：\n日期：$dateStr\n时间戳：$timestamp\n转回日期：$backToStr")
            } catch (e: Exception) {
                showResult("错误：${e.message}")
            }
        }
        
        // 测试4：日期差值
        view.findViewById<Button>(R.id.bt_test_date_diff).setOnClickListener {
            val startDate = view.findViewById<EditText>(R.id.ed_start_date).text.toString()
            val endDate = view.findViewById<EditText>(R.id.ed_end_date).text.toString()
            
            try {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val start = format.parse(startDate)!!
                val end = format.parse(endDate)!!
                val daysDiff = ZonedDateTimeUtil.getDateDifferenceDay(start, end)
                val monthsDiff = ZonedDateTimeUtil.getDateDifferenceMonth(start, end)
                showResult("日期差值：\n起始：$startDate\n结束：$endDate\n相差天数：$daysDiff\n相差月数：$monthsDiff")
            } catch (e: Exception) {
                showResult("错误：${e.message}")
            }
        }
        
        // 测试5：月份最大天数
        view.findViewById<Button>(R.id.bt_test_max_day).setOnClickListener {
            val year = view.findViewById<EditText>(R.id.ed_year).text.toString().toIntOrNull() ?: 2024
            val month = view.findViewById<EditText>(R.id.ed_month).text.toString().toIntOrNull() ?: 1
            
            val maxDay = ZonedDateTimeUtil.getMaxDayByYearMonth(year, month)
            showResult("月份最大天数：\n年份：$year\n月份：$month\n最大天数：$maxDay")
        }
        
        // 测试6：获取时区
        view.findViewById<Button>(R.id.bt_test_timezone).setOnClickListener {
            val timezone = ZonedDateTimeUtil.getCurrentTimeZone()
            showResult("当前时区：UTC${if (timezone >= 0) "+" else ""}$timezone")
        }
        
        // 测试7：月份偏移
        view.findViewById<Button>(R.id.bt_test_offset_month).setOnClickListener {
            val dateStr = view.findViewById<EditText>(R.id.ed_date_for_month).text.toString()
            val months = view.findViewById<EditText>(R.id.ed_months).text.toString().toIntOrNull() ?: 0
            
            try {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = format.parse(dateStr)!!
                val result = ZonedDateTimeUtil.getOffsetDateByMonth(months, date)
                showResult("月份偏移结果：\n原日期：$dateStr\n偏移 $months 月后：${format.format(result)}")
            } catch (e: Exception) {
                showResult("错误：${e.message}")
            }
        }
    }
    
    private fun showResult(text: String) {
        tvResult.text = text
    }
}
