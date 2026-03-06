package cc.fastcv.ui.demo.modern_cardview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.fastcv.modern_cardview.ModernCardView
import cc.fastcv.stage.StageActivity
import cc.fastcv.ui.demo.R

class ModernCardViewActivity : StageActivity() {

    private lateinit var modernCard1: ModernCardView
    private lateinit var modernCard2: ModernCardView
    private lateinit var modernCard3: ModernCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modern_cardview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        modernCard1 = findViewById(R.id.modernCard1)
        modernCard2 = findViewById(R.id.modernCard2)
        modernCard3 = findViewById(R.id.modernCard3)

        modernCard1.apply {
            setCardTitle("代码仓库")
            setCardSubtitle("Modern design patterns")
            setCardIcon(R.drawable.ic_code)
        }

        // 设置第二个卡片 - 星标主题
        modernCard2.apply {
            setCardTitle("项目展示")
            setCardSubtitle("Featured repositories")
            setCardIcon(R.drawable.ic_star)
        }

        // 设置第三个卡片 - 文档主题
        modernCard3.apply {
            setCardTitle("文档中心")
            setCardSubtitle("Documentation & guides")
            setCardIcon(R.drawable.ic_repo)
        }
    }

}