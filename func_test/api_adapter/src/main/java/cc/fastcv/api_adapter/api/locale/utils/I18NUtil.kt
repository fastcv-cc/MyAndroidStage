package cc.fastcv.api_adapter.api.locale.utils

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.*

/**
 * APP多语言设置界面
 */
object I18NUtil {
    /**
     * 获取国家码
     */
    fun getCountryZipCode(resources: Resources): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].country
        } else {
            resources.configuration.locale.country
        }
    }

    fun getISOLanguages(): List<String> {
        return Locale.getISOLanguages().toList()
    }

    fun applyLanguage(context: Context?, language: String): Context? {
        return if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //7.0及以上的方法
                createConfiguration(context, language)
            } else {
                updateConfiguration(context, language)
                context
            }
        } else {
            context
        }
    }

    fun getCurrentSystemLanguage(resources: Resources): String {
        val language: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        } else {
            resources.configuration.locale.language
        }
        return language
    }

    /**
     *  更新Application的Resource local，应用不重启的情况才调用，因为部分会用到application中的context
     *  切记不能走新api createConfigurationContext，亲测
     */
    fun updateApplicationLocale(app: Application, language: String) {
        val configuration = app.resources.configuration
        val locale = Locale(language)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
        } else {
            configuration.setLocale(locale)
        }
        val dm = app.resources.displayMetrics
        app.resources.updateConfiguration(configuration, dm)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createConfiguration(context: Context, language: String): Context {
        val resources = context.resources
        val locale = Locale(language)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    private fun updateConfiguration(context: Context, language: String) {
        val resources = context.resources
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        val displayMetrics = resources.displayMetrics
        resources.updateConfiguration(configuration, displayMetrics)
    }
}