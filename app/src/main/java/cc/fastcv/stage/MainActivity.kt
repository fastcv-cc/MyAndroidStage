package cc.fastcv.stage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.func_test.demo.FuncTestDemoActivity
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.lib_components.BaseViewHolder
import cc.fastcv.line_number_clock.LineNumberClockView
import cc.fastcv.ui.demo.UIDemoActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var clockView: LineNumberClockView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter
    
    private val menuItems = listOf(
        MenuItem(
            "UI 组件展示",
            "查看所有自定义 UI 组件",
            R.drawable.ic_func_test,
            UIDemoActivity::class.java
        ),
        MenuItem(
            "功能测试",
            "测试各种功能模块",
            R.drawable.ic_func_test,
            FuncTestDemoActivity::class.java
        )
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        initViews()
        setupRecyclerView()
    }
    
    private fun initViews() {
        clockView = findViewById(R.id.clock_view)
        clockView.bindLifecycle(lifecycle)
        
        recyclerView = findViewById(R.id.recycler_view)
    }
    
    private fun setupRecyclerView() {
        adapter = MainAdapter(menuItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        
        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<MenuItem> {
            override fun onItemClick(view: View?, position: Int, item: MenuItem) {
                startActivity(Intent(this@MainActivity, item.targetActivity))
            }
        })
    }
    
    /**
     * 菜单项数据类
     */
    data class MenuItem(
        val title: String,
        val subtitle: String,
        val iconResId: Int,
        val targetActivity: Class<*>
    )
    
    /**
     * RecyclerView 适配器
     */
    inner class MainAdapter(private val items: List<MenuItem>) : BaseRecyclerViewAdapter<MenuItem>() {
        
        override fun getLayoutId(viewType: Int): Int {
            return R.layout.item_main_menu
        }
        
        override fun getDataByPosition(position: Int): MenuItem {
            return items[position]
        }
        
        override fun getTotalSize(): Int {
            return items.size
        }
        
        override fun convert(holder: BaseViewHolder, data: MenuItem, position: Int) {
            // ModernCardView 会在布局中自动处理，这里只需要设置数据
            val cardView = holder.itemView as cc.fastcv.modern_cardview.ModernCardView
            cardView.setCardTitle(data.title)
            cardView.setCardSubtitle(data.subtitle)
            cardView.setCardIcon(data.iconResId)
        }
    }
}