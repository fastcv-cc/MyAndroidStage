package cc.fastcv.ui.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.exquisite_linechart.ExquisiteLineChartView
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.lib_components.BaseViewHolder
import cc.fastcv.line_number_clock.LineNumberClockView
import cc.fastcv.modern_cardview.ModernCardView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.date_switch_view.DateSwitchViewActivity
import cc.fastcv.ui.demo.exquisite_histogram.ExquisiteHistogramActivity
import cc.fastcv.ui.demo.exquisite_linechart.ExquisiteLineChartActivity
import cc.fastcv.ui.demo.flash_text_view.FlashTextViewActivity
import cc.fastcv.ui.demo.line_number_clock.LineNumberClockActivity
import cc.fastcv.ui.demo.modern_cardview.ModernCardViewActivity

class UIDemoActivity : StageActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UIComponentAdapter

    private val uiComponents = listOf(
        UIComponent(
            "FlashTextView",
            "闪烁文字视图组件",
            R.drawable.ic_flash_text,
            FlashTextViewActivity::class.java
        ),
        UIComponent(
            "LineNumberClock",
            "线条数字时钟组件",
            R.drawable.ic_clock,
            LineNumberClockActivity::class.java
        ),
        UIComponent(
            "ExquisiteHistogram",
            "精美柱状图组件",
            R.drawable.ic_histogram,
            ExquisiteHistogramActivity::class.java
        ),
        UIComponent(
            "DateSwitchView",
            "日期切换视图组件",
            R.drawable.ic_date_switch,
            DateSwitchViewActivity::class.java
        ),
        UIComponent(
            "ModernCardView",
            "现代卡片视图组件",
            R.drawable.ic_card,
            ModernCardViewActivity::class.java
        ),
        UIComponent(
            "ExquisiteLineChart",
            "精美折线图组件",
            R.drawable.ic_histogram,
            ExquisiteLineChartActivity::class.java
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ui_demo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recycler_view)
    }

    private fun setupRecyclerView() {
        adapter = UIComponentAdapter(uiComponents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object :
            BaseRecyclerViewAdapter.OnItemClickListener<UIComponent> {
            override fun onItemClick(view: View?, position: Int, item: UIComponent) {
                startActivity(Intent(this@UIDemoActivity, item.targetActivity))
            }
        })
    }

    data class UIComponent(
        val title: String,
        val subtitle: String,
        val iconResId: Int,
        val targetActivity: Class<*>
    )

    inner class UIComponentAdapter(private val items: List<UIComponent>) :
        BaseRecyclerViewAdapter<UIComponent>() {

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.item_ui_component
        }

        override fun getDataByPosition(position: Int): UIComponent {
            return items[position]
        }

        override fun getTotalSize(): Int {
            return items.size
        }

        override fun convert(holder: BaseViewHolder, data: UIComponent, position: Int) {
            val cardView = holder.itemView as ModernCardView
            cardView.setCardTitle(data.title)
            cardView.setCardSubtitle(data.subtitle)
            cardView.setCardIcon(data.iconResId)
        }
    }
}