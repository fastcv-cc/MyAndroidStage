package cc.fastcv.exquisite_linechart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withTranslation


internal class DashedView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * 线宽
     */
    private var dashedWidth = 1f

    /**
     * 线颜色
     */
    private var dashedColor = "#A3B352".toColorInt()

    /**
     * 每格虚线的宽
     */
    private var mDottedLineWidth = 3f

    /**
     * 高度
     */
    private var viewHeight = 0

    /**
     * 宽度
     */
    private var viewWidth = 0

    /**
     * 画笔
     */
    private var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dashedWidth,
            context.resources.displayMetrics
        )
        style = Paint.Style.STROKE
        color = dashedColor
        pathEffect = DashPathEffect(
            floatArrayOf(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    mDottedLineWidth,
                    context.resources.displayMetrics
                ),
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    mDottedLineWidth,
                    context.resources.displayMetrics
                )
            ), 0f
        )
    }

    /**
     * 路径
     */
    private var path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
            it.withTranslation(viewWidth / 2f, 0f) {
                path.moveTo(0f, 0f)
                path.lineTo(0f, viewHeight * 1f)
                canvas.drawPath(path, paint)
            }
        }
    }
}