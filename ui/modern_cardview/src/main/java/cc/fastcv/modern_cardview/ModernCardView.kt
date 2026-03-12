package cc.fastcv.modern_cardview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import cc.fastcv.flash_text_view.FlashTextView

class ModernCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private lateinit var containerLayout: ConstraintLayout
    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: FlashTextView
    private lateinit var subtitleTextView: TextView
    private lateinit var backgroundGradient: View

    private var currentGradientIndex = 0
    private val gradientBackgrounds = arrayOf(
        intArrayOf(0xFFfa709a.toInt(), 0xFFfee140.toInt()),
        intArrayOf(0xFF43e97b.toInt(), 0xFF38f9d7.toInt()),
        intArrayOf(0xFFf093fb.toInt(), 0xFFf5576c.toInt())
    )

    init {
        setupCardStyle()
        inflateLayout()
        setupViews()
        applyInitialGradient()
    }

    private fun setupCardStyle() {
        // 设置卡片基本样式
        radius = 20f
        cardElevation = 12f
        useCompatPadding = true
        
        // 设置外边距
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(16, 16, 16, 16)
        }
        this.layoutParams = layoutParams
    }

    private fun inflateLayout() {
        // 创建内部布局
        containerLayout = ConstraintLayout(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            setPadding(24, 24, 24, 24)
        }

        // 创建渐变背景视图
        backgroundGradient = View(context).apply {
            id = generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                200
            ).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
        }

        // 创建图标
        iconImageView = ImageView(context).apply {
            id = generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(64, 64).apply {
                topToTop = backgroundGradient.id
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = 32
                marginStart = 32
            }
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            setImageResource(R.drawable.ic_code) // 默认图标
            setColorFilter(Color.WHITE)
        }

        // 创建标题
        titleTextView = FlashTextView(context).apply {
            id = generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToTop = iconImageView.id
                startToEnd = iconImageView.id
                marginStart = 16
            }
            text = "Modern Card"
            textSize = 20f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }

        // 创建副标题
        subtitleTextView = TextView(context).apply {
            id = generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = titleTextView.id
                startToStart = titleTextView.id
                topMargin = 8
            }
            text = "Tap to change style"
            textSize = 14f
            setTextColor("#E0FFFFFF".toColorInt())
        }

        // 添加所有视图到容器
        containerLayout.addView(backgroundGradient)
        containerLayout.addView(iconImageView)
        containerLayout.addView(titleTextView)
        containerLayout.addView(subtitleTextView)
        
        addView(containerLayout)
    }

    private fun setupViews() {
        // 设置点击事件
        setOnClickListener {
            animateAndChangeGradient()
        }

        // 设置长按事件
        setOnLongClickListener {
            playPulseAnimation()
            true
        }
    }

    private fun applyInitialGradient() {
        applyGradient(currentGradientIndex)
    }

    private fun applyGradient(index: Int) {
        val colors = gradientBackgrounds[index]
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            colors
        ).apply {
            cornerRadius = 16f
        }
        backgroundGradient.background = gradientDrawable
    }

    private fun animateAndChangeGradient() {
        // 播放缩放动画
        animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                // 切换渐变
                currentGradientIndex = (currentGradientIndex + 1) % gradientBackgrounds.size
                applyGradient(currentGradientIndex)
                
                // 恢复缩放
                animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    private fun playPulseAnimation() {
        // 播放脉冲动画
        animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(300)
            .withEndAction {
                animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }

    /**
     * 设置卡片标题
     */
    fun setCardTitle(title: String) {
        titleTextView.text = title
    }

    /**
     * 设置卡片副标题
     */
    fun setCardSubtitle(subtitle: String) {
        subtitleTextView.text = subtitle
    }

    /**
     * 设置卡片图标
     */
    fun setCardIcon(iconResId: Int) {
        iconImageView.setImageResource(iconResId)
        iconImageView.setColorFilter(Color.WHITE)
    }

    /**
     * 移除图标颜色过滤器
     */
    fun removeIconColorFilter() {
        iconImageView.colorFilter = null
    }

    /**
     * 手动切换到下一个渐变
     */
    fun nextGradient() {
        currentGradientIndex = (currentGradientIndex + 1) % gradientBackgrounds.size
        applyGradient(currentGradientIndex)
    }

    /**
     * 设置自定义渐变颜色
     */
    fun setCustomGradient(startColor: Int, endColor: Int) {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(startColor, endColor)
        ).apply {
            cornerRadius = 16f
        }
        backgroundGradient.background = gradientDrawable
    }
} 