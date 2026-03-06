package cc.fastcv.api_adapter.api.locale.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.core.os.LocaleListCompat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object LocaleToolUtils {
    /**
     * 获取手机app的locale
     */
    fun getAppLocale(context: Context): Locale {
        var locale: Locale?
        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        if (locale == null) {
            locale = Locale("en-US")
        }
        return locale
    }

    val sysLocale: Locale
        /**
         * 获取真正的，手机系统设置里的locale。
         * @retrun 手机系统设置里的locale，为null则返回en-US。
         */
        get() {
            var locale: Locale?
            locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //大于24
                Resources.getSystem().configuration.locales[0]
            } else {
                Resources.getSystem().configuration.locale
            }
            if (locale == null) {
                locale = Locale("en-US")
            }
            return locale
        }

    @get:Deprecated("")
    val defaultLocale: Locale
        /**
         * 获取默认的locale
         * @return                          Locale对象
         */
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }

    fun attachBaseContext(context: Context, newLocale: Locale?): Context {
        var newLocale = newLocale
        if (newLocale == null) {
            //如果配置里locale为空，则获取app本身的locale
            newLocale = getAppLocale(context.applicationContext)
        }
        return setLocale(context, newLocale)
    }

    /**
     * 更新locale
     * 问题：
     * Android7.0及之后版本，使用了LocaleList，Configuration中的语言设置可能获取的不同，而是生效于各自的Context。
     * 这会导致：Android7.0使用就的方式，有些Activity可能会显示为手机的系统语言。
     * 解决方案：
     * Android7.0 优化了对多语言的支持，废弃了updateConfiguration()方法;
     * 替代方法：createConfigurationContext(), 而返回的是Context。
     * @param context               上下文
     * @param newLocale             新locale
     */
    fun setLocale(context: Context, newLocale: Locale?): Context {
        val resources =
            if (context.applicationContext == null) context.resources else context.applicationContext.resources
        val configuration = resources.configuration
        Locale.setDefault(newLocale)
        val newBase: Context
        newBase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale)
            val localeList = LocaleList(newLocale)
            configuration.setLocales(localeList)
            LocaleList.setDefault(localeList)
            context.createConfigurationContext(configuration)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //7.0以上
                configuration.setLocale(newLocale)
                context.createConfigurationContext(configuration)
            } else {
                val displayMetrics = resources.displayMetrics
                context.resources.updateConfiguration(configuration, displayMetrics)
                context
            }
        }
        return newBase
    }

    /**
     * 获取语种对象
     */
    fun getLocale(context: Context): Locale {
        return getLocale(context.resources.configuration)
    }

    fun getLocale(config: Configuration): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales[0]
        } else {
            config.locale
        }
    }

    /**
     * 设置语种对象
     */
    fun setLocale(config: Configuration, locale: Locale?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(locale)
                config.setLocales(localeList)
            } else {
                config.setLocale(locale)
            }
        } else {
            config.locale = locale
        }
    }

    /**
     * 设置默认的语种环境（日期格式化会用到）
     */
    fun setDefaultLocale(context: Context) {
        val configuration = context.resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.setDefault(configuration.locales)
        } else {
            Locale.setDefault(configuration.locale)
        }
    }

    /**
     * 绑定当前 App 的语种
     */
    fun attachLanguages(context: Context, locale: Locale?): Context {
        var context = context
        val resources = context.resources
        val config = Configuration(resources.configuration)
        setLocale(config, locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config)
        }
        resources.updateConfiguration(config, resources.displayMetrics)
        return context
    }

    /**
     * 更新 Resources 语种
     */
    fun updateLanguages(resources: Resources, locale: Locale?) {
        val config = resources.configuration
        setLocale(config, locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * 更新手机配置信息变化
     */
    fun updateConfigurationChanged(context: Context, newConfig: Configuration?) {
        val config = Configuration(newConfig)
        // 绑定当前语种到这个新的配置对象中
        setLocale(config, LocaleSpUtils.getAppLanguage(context))
        val resources = context.resources
        // 更新上下文的配置信息
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * 获取某个语种下的 Resources 对象
     */
    fun getLanguageResources(context: Context, locale: Locale?): Resources {
        val config = Configuration()
        setLocale(config, locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config).resources
        } else Resources(
            context.assets,
            context.resources.displayMetrics, config
        )
    }

    /**
     * 根据localeTag获取对应的locale
     * @param localeTag                         localeTag
     * @return
     */
    fun tagToLocale(localeTag: String): Locale {
        val realLocaleTag = localeTag.replace("_", "-")
        val localeListCompat = LocaleListCompat.forLanguageTags(realLocaleTag)
        return localeListCompat[0]
    }

    /**
     * 比较两个语言，是否相同
     *
     */
    fun equalLocales(cur: Locale, tar: Locale): Boolean {
        val curLang = cur.language
        val tarLang = tar.language
        return curLang.startsWith(tarLang)
    }

    /**
     * 获取某个语种下的 String
     */
    fun getLanguageString(context: Context, locale: Locale?, id: Int): String {
        return getLanguageResources(context, locale).getString(id)
    }

    /**
     * 对比两个语言是否是同一个语种（比如：中文的简体和繁体，英语的美式和英式）
     * @return true：同一语言，false：不同语言
     */
    fun equalsLanguage(locale1: Locale, locale2: Locale): Boolean {
        return locale1.language == locale2.language
    }

    /**
     * 比较两个语言，是否相同
     * @return true：同一语言，false：不同语言
     */
    fun equalLocales(curTag: String, tarTag: String): Boolean {
        val cur = tagToLocale(curTag)
        val tar = tagToLocale(tarTag)
        return equalLocales(cur, tar)
    }

    fun localeToTag(locale: Locale?): String {
        val localeListCompat = LocaleListCompat.create(locale)
        return localeListCompat.toLanguageTags()
    }

    fun getTimeZoneUtcOffset(timezone: TimeZone?): Int {
        return if (timezone != null) {
            var rawOffset = timezone.rawOffset / '\uea60'.code
            if (timezone.inDaylightTime(Date())) {
                rawOffset += 60
            }
            rawOffset
        } else {
            -1
        }
    }

    val timeZoneUtcOffset: Int
        get() = getTimeZoneUtcOffset(TimeZone.getDefault())
}