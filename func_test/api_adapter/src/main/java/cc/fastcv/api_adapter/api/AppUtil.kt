package cc.fastcv.api_adapter.api

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * App相关方法类
 */
object AppUtil {
    /**
     * 获取语言
     */
    fun getLocaleLanguage(context: Context): String {
        return context.resources.configuration.locale.language
    }

    /**
     * 获取国家码
     */
    fun getCountryZipCode(context: Context): String {
        return context.resources.configuration.locale.country
    }

    /**
     * 获取应用版本名称
     */
    fun getVersionName(context: Context): String {
        var verName = ""
        try {
            verName = context.packageManager.getPackageInfo(
                context.packageName, 0
            ).versionName?:""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    /**
     * 获取应用版本号
     */
    fun getVersionCode(context: Context): Long {
        var versionName = 0L
        try {
            versionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(
                    context.packageName, 0
                ).longVersionCode
            } else {
                context.packageManager.getPackageInfo(
                    context.packageName, 0
                ).versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

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
    fun getSupportedAbis(): Array<String> {
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

