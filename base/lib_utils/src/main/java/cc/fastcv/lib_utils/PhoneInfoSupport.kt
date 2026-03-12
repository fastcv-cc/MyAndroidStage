package cc.fastcv.lib_utils

import android.os.Build

object PhoneInfoSupport {

    /**
     * 获取设备安卓版本
     */
    fun getPhoneVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取手机厂商名称
     */
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取手机品牌
     */
    fun getProduct(): String {
        return Build.PRODUCT
    }

    /**
     * 获取手机支持的cpu型号
     */
    fun getSupportedABIs(): Array<String> {
        return Build.SUPPORTED_ABIS
    }

    /**
     * 获取手机型号
     */
    fun getDevice(): String {
        return Build.DEVICE
    }

    /**
     * 获取手机屏幕显示信息
     */
    fun getDisplayInfo(): String {
        return Build.DISPLAY
    }

    /**
     * 获取设备型号
     */
    fun getModel(): String {
        return Build.MODEL
    }

}