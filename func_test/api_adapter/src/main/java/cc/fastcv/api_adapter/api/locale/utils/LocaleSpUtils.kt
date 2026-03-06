package cc.fastcv.api_adapter.api.locale.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import cc.fastcv.api_adapter.api.locale.service.LocaleService
import java.util.Locale

object LocaleSpUtils {
    private const val KEY_LANGUAGE = "key_language"
    private const val KEY_COUNTRY = "key_country"
    private var sSharedPreferencesName = "language_setting"

    @Volatile
    private var sCurrentLanguage: Locale? = null

    /**
     * 设置保存的 SharedPreferences 文件名（请在 Application 初始化之前设置，可以放在 Application 中的代码块或者静态代码块）
     */
    @Synchronized
    fun setSharedPreferencesName(name: String) {
        sSharedPreferencesName = name
    }

    @Synchronized
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(sSharedPreferencesName, Context.MODE_PRIVATE)
    }

    @Synchronized
    fun setAppLanguage(context: Context, locale: Locale) {
        sCurrentLanguage = locale
        getSharedPreferences(context).edit()
            .putString(KEY_LANGUAGE, locale.language)
            .putString(KEY_COUNTRY, locale.country)
            .apply()
    }

    @Synchronized
    fun getAppLanguage(context: Context): Locale? {
        if (sCurrentLanguage == null) {
            val language: String? = getSharedPreferences(context).getString(KEY_LANGUAGE, null)
            val country: String? = getSharedPreferences(context).getString(KEY_COUNTRY, null)
            if (!TextUtils.isEmpty(language)) {
                sCurrentLanguage = Locale(language, country)
            } else {
                sCurrentLanguage = LocaleToolUtils.getLocale(context)
            }
        }
        return sCurrentLanguage
    }

    @Synchronized
    fun isSystemLanguage(context: Context): Boolean {
        val language: String? = getSharedPreferences(context).getString(KEY_LANGUAGE, null)
        return language == null || "" == language
    }

    @Synchronized
    fun clearLanguage(context: Context) {
        sCurrentLanguage = LocaleService.getSystemLocale()
        getSharedPreferences(context).edit()
            .remove(KEY_LANGUAGE)
            .remove(KEY_COUNTRY)
            .apply()
    }
}