package cc.fastcv.lib_test.lib_utils

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cc.fastcv.lib_test.R
import cc.fastcv.stage.StageActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LibUtilsTestActivity : StageActivity() {
    
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    
    private val tabTitles = listOf(
        "日期时间工具",
        "手机信息工具",
        "Calendar扩展",
        "Context扩展",
        "View扩展"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lib_utils_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initViews()
        setupViewPager()
    }
    
    private fun initViews() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
    }
    
    private fun setupViewPager() {
        val adapter = UtilsFragmentAdapter(this)
        viewPager.adapter = adapter
        
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
    
    private inner class UtilsFragmentAdapter(activity: LibUtilsTestActivity) : 
        FragmentStateAdapter(activity) {
        
        override fun getItemCount(): Int = tabTitles.size
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ZonedDateTimeFragment()
                1 -> PhoneInfoFragment()
                2 -> CalendarExtFragment()
                3 -> ContextExtFragment()
                4 -> ViewExtFragment()
                else -> ZonedDateTimeFragment()
            }
        }
    }
}
