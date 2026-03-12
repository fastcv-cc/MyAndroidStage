package cc.fastcv.flash_text_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.util.AttributeSet

class FlashTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    //矩阵对象
    private var gradientMatrix: Matrix? = null

    //渐变效果
    private var flashGradient: LinearGradient? = null

    //平移距离
    private var xTranslate = 0
    
    //闪烁颜色（默认为白色）
    private var flashColor: Int = 0xFFFFFF
    
    //动画速度（值越小越快，默认为5）
    private var animationSpeed: Int = 5
    
    //刷新延迟（毫秒，默认100ms）
    private var refreshDelay: Long = 100
    
    //渐变模式（0=三色渐变，1=双色渐变，2=单色闪烁）
    private var gradientMode: Int = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0) {
            updateGradient(w)
        }
    }
    
    private fun updateGradient(width: Int) {
        gradientMatrix = Matrix()
        
        flashGradient = when (gradientMode) {
            0 -> {
                // 三色渐变：原色 -> 闪烁色 -> 原色
                LinearGradient(
                    0f,
                    0f,
                    width * 1.0f,
                    0f,
                    intArrayOf(textColors.defaultColor, flashColor, textColors.defaultColor),
                    null,
                    Shader.TileMode.CLAMP
                )
            }
            1 -> {
                // 双色渐变：原色 -> 闪烁色
                LinearGradient(
                    0f,
                    0f,
                    width * 1.0f,
                    0f,
                    intArrayOf(textColors.defaultColor, flashColor),
                    null,
                    Shader.TileMode.CLAMP
                )
            }
            else -> {
                // 单色闪烁：闪烁色 -> 原色 -> 闪烁色
                LinearGradient(
                    0f,
                    0f,
                    width * 1.0f,
                    0f,
                    intArrayOf(flashColor, textColors.defaultColor, flashColor),
                    null,
                    Shader.TileMode.CLAMP
                )
            }
        }
        
        paint.setShader(flashGradient)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制文字之后
        if (gradientMatrix != null) {
            xTranslate += measuredWidth / animationSpeed
            if (xTranslate > 2 * measuredWidth) {
                xTranslate = -measuredWidth
            }
            gradientMatrix!!.setTranslate(xTranslate * 1.0f, 0f)
            flashGradient!!.setLocalMatrix(gradientMatrix)
            postInvalidateDelayed(refreshDelay)
        }
    }
    
    /**
     * 设置闪烁颜色
     * @param color 颜色值（如：0xFFFFFF 或 Color.WHITE）
     */
    fun setFlashColor(color: Int) {
        flashColor = color
        if (measuredWidth > 0) {
            updateGradient(measuredWidth)
            invalidate()
        }
    }
    
    /**
     * 设置动画速度
     * @param speed 速度值（1-20，值越小越快，默认5）
     */
    fun setAnimationSpeed(speed: Int) {
        animationSpeed = speed.coerceIn(1, 20)
    }
    
    /**
     * 设置刷新延迟
     * @param delay 延迟时间（毫秒，建议50-200，默认100）
     */
    fun setRefreshDelay(delay: Long) {
        refreshDelay = delay.coerceIn(50, 500)
    }
    
    /**
     * 设置渐变模式
     * @param mode 模式（0=三色渐变，1=双色渐变，2=单色闪烁）
     */
    fun setGradientMode(mode: Int) {
        gradientMode = mode.coerceIn(0, 2)
        if (measuredWidth > 0) {
            updateGradient(measuredWidth)
            invalidate()
        }
    }
    
    /**
     * 获取当前闪烁颜色
     */
    fun getFlashColor(): Int = flashColor
    
    /**
     * 获取当前动画速度
     */
    fun getAnimationSpeed(): Int = animationSpeed
    
    /**
     * 获取当前刷新延迟
     */
    fun getRefreshDelay(): Long = refreshDelay
    
    /**
     * 获取当前渐变模式
     */
    fun getGradientMode(): Int = gradientMode
}