package cc.fastcv.round_progress_bar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColorInt

class RoundProgressBar : View {

    constructor(context: Context?) : super(context) {
        initView(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private var showWidth = 0
    private var rpbBackground = 0
    private var rpbProgressColor = 0
    private var progress = 0
    private var max = 0
    private var realWidth = 0
    private var realHeight = 0
    private var radius = 0f
    private var rectF: RectF? = null
    private var backgroundPath = Path()
    private var progressPath = Path()
    private val backgroundPathMeasure = PathMeasure(backgroundPath, true)

    private var startColor:Int = 0
    private var endColor:Int = 0

    private var shader: Shader? = null

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    fun setProgress(value: Int) {
        progress = value
        invalidate()
    }

    fun setProgressWidth(value: Int) {
        showWidth = dp2px(context, value)
        initPathMeasure()
        invalidate()
    }

    fun setProgressColor(color: Int) {
        rpbProgressColor = color
        invalidate()
    }

    fun setProgressBgColor(color: Int) {
        rpbBackground = color
        invalidate()
    }

    fun setGradientColor(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
        if (startColor == 0 && endColor == 0) {
            shader = null
            return
        }
        shader = LinearGradient(
            0f,
            realHeight / 2f,
            realWidth * 1.0f,
            realHeight / 2f,
            startColor,
            endColor,
            Shader.TileMode.MIRROR
        )
        invalidate()
    }

    @SuppressLint("ResourceType")
    private fun initView(context: Context?, attrs: AttributeSet?) {
        if (attrs != null && context != null) {
            context.withStyledAttributes(attrs, R.styleable.RoundProgressBar) {
                showWidth =
                    getDimensionPixelSize(
                        R.styleable.RoundProgressBar_showWidth,
                        dp2px(context, 30)
                    )
                rpbBackground =
                    getColor(
                        R.styleable.RoundProgressBar_rpbBackground,
                        "#FFFFFF".toColorInt()
                    )
                rpbProgressColor = getColor(
                    R.styleable.RoundProgressBar_rpbProgressColor,
                    "#000000".toColorInt()
                )
                progress = getInt(R.styleable.RoundProgressBar_rpbProgress, 0)
                max = getInt(R.styleable.RoundProgressBar_rpbMax, 100)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        realWidth = w
        realHeight = h
        radius = minOf(realWidth, realHeight) / 2.0f
        shader = if (startColor != 0 && endColor != 0) {
            LinearGradient(
                0f,
                realHeight / 2f,
                realWidth * 1.0f,
                realHeight / 2f,
                startColor,
                endColor,
                Shader.TileMode.MIRROR
            )
        } else {
            null
        }

        initPathMeasure()
    }

    private fun initPathMeasure() {
        rectF = RectF(
            showWidth / 2.0f,
            showWidth / 2.0f,
            realWidth - showWidth / 2.0f,
            realHeight - showWidth / 2.0f
        )
        backgroundPath.reset()
        backgroundPath.addRoundRect(rectF!!, radius, radius, Path.Direction.CCW)
        backgroundPathMeasure.setPath(backgroundPath, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.rotate(90.0f, realWidth / 2.0f, realHeight / 2.0f)
        paint.shader = null
        paint.strokeWidth = showWidth * 1.0f
        paint.color = rpbBackground
        canvas.drawPath(backgroundPath, paint)


        if (shader == null) {
            paint.color = rpbProgressColor
        } else {
            paint.shader = shader
        }
        progressPath.reset()
        backgroundPathMeasure.getSegment(
            (1f - ((progress * 1.0f / max))) * backgroundPathMeasure.length,
            backgroundPathMeasure.length,
            progressPath,
            true
        )
        canvas.drawPath(progressPath, paint)
    }

    private fun dp2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}