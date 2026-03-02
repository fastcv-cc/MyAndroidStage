package cc.fastcv.exquisite_histogram

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


internal class LineView : View {
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
    private var dashedColor = Color.parseColor("#E8EAE6")

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
        strokeWidth = dp2px(context,dashedWidth)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = dashedColor
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
            it.save()
            it.translate(viewWidth/2f,0f)
            path.moveTo(0f, 0f + dp2px(context,dashedWidth)/2)
            path.lineTo(0f, viewHeight * 1f - dp2px(context,dashedWidth)/2)
            canvas.drawPath(path, paint)
            it.restore()
        }
    }

    private fun dp2px(context: Context, dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue,
            context.resources.displayMetrics
        )
    }


}