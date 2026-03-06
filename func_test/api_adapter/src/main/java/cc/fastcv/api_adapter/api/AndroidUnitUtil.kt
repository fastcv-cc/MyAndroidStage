package cc.fastcv.api_adapter.api

import android.content.Context
import kotlin.math.roundToInt

object AndroidUnitUtil {

    /**
     * 将值用作dp转px
     */
    fun <N : Number> dip2px(dpValue: N, context: Context): Float {
        val density = context.resources.displayMetrics.density
        return dpValue.toFloat() * density
    }

    /**
     * 将值用作px转dp
     */
    fun <N : Number> px2dp(pxValue: N, context: Context): Float {
        val density = context.resources.displayMetrics.density
        return pxValue.toFloat() / density
    }

    /**
     * 将值用作px转dp
     */
    fun <N : Number> px2dpi(pxValue: N, context: Context): Int {
        return px2dp(pxValue, context).roundToInt()
    }

    /** 将**sp**单位的值[spValue]转换为**px**单位的值[Float] */
    fun <N : Number> sp2px(spValue: N, context: Context): Float {
        val density = context.resources.displayMetrics.scaledDensity
        return spValue.toFloat() * density
    }

    /**
     * 将**px**单位的值[pxValue]转换为**sp**单位的值[Float]
     */
    fun <N : Number> px2sp(pxValue: N, context: Context): Float {
        val density = context.resources.displayMetrics.scaledDensity
        return pxValue.toFloat() / density
    }

}