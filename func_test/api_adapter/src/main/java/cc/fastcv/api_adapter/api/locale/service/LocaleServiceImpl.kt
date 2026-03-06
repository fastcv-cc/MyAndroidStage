package cc.fastcv.api_adapter.api.locale.service

import android.app.Application
import android.content.Context
import android.content.res.Resources
import cc.fastcv.api_adapter.api.locale.listener.OnLocaleChangedListener
import cc.fastcv.api_adapter.api.locale.observer.ActivityCallback
import cc.fastcv.api_adapter.api.locale.observer.LanguagesObserver
import cc.fastcv.api_adapter.api.locale.utils.LocaleSpUtils
import cc.fastcv.api_adapter.api.locale.utils.LocaleToolUtils
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

class LocaleServiceImpl : ILocaleService {
    private val listeners: MutableList<OnLocaleChangedListener> = ArrayList()
    private val isInit = AtomicBoolean(false)

    /**
     * 获取应用上下文
     */
    override fun getApplication(): Application {
        return sApplication!!
    }

    override fun init(application: Application, inject: Boolean) {
        if (!isInit.get()) {
            sApplication = application
            LanguagesObserver.register(application)
            LocaleToolUtils.setDefaultLocale(application)
            if (inject) {
                ActivityCallback.inject(application)
            }
            isInit.set(true)
        }
    }

    /**
     * 在上下文的子类中重写 attachBaseContext 方法（用于更新 Context 的语种）
     */
    override fun attachBaseContext(context: Context): Context {
        // 8.0需要使用createConfigurationContext处理
        return if (LocaleToolUtils.getLocale(context) == LocaleSpUtils.getAppLanguage(context)) {
            context
        } else {
            LocaleToolUtils.attachLanguages(context, LocaleSpUtils.getAppLanguage(context))
        }
    }

    override fun getCurrentLocale(): Locale? {
        return LocaleSpUtils.getAppLanguage(sApplication!!)
    }

    /**
     * 获取系统的语种
     */
    override fun getSystemLocale(): Locale? {
        return LanguagesObserver.systemLanguage
    }

    /**
     * 是否跟随系统的语种
     */
    override fun isSystemLocale(): Boolean {
        return LocaleSpUtils.isSystemLanguage(sApplication!!)
    }

    override fun getCurrentLocaleTag(): String {
        return ""
    }

    override fun getCurrentLang(): Locale? {
        return null
    }

    override fun getCurrentLangTag(): String {
        return ""
    }

    override fun getSupportLocaleList(): List<Locale?> {
        return ArrayList()
    }

    /**
     * 跟随系统语种
     */
    override fun clearAppLanguage(context: Context): Boolean {
        LocaleSpUtils.clearLanguage(context)
        if (LocaleToolUtils.getLocale(context) == getSystemLocale()) {
            return false
        }
        LocaleToolUtils.updateLanguages(context.resources, getSystemLocale())
        LocaleToolUtils.setDefaultLocale(context)
        if (context !== sApplication) {
            // 更新 Application 的语种
            LocaleToolUtils.updateLanguages(sApplication!!.resources, getSystemLocale())
        }
        return true
    }

    /**
     * 设置 App 的语种
     */
    override fun setAppLanguage(context: Context, newLocale: Locale): Boolean {
        LocaleSpUtils.setAppLanguage(context, newLocale)
        if (LocaleToolUtils.getLocale(context) == newLocale) {
            return false
        }
        val oldLocale: Locale = LocaleToolUtils.getLocale(context)
        // 更新 Context 的语种
        LocaleToolUtils.updateLanguages(context.resources, newLocale)
        if (context !== sApplication) {
            // 更新 Application 的语种
            LocaleToolUtils.updateLanguages(sApplication!!.resources, newLocale)
        }
        LocaleToolUtils.setDefaultLocale(context)
        val onLocaleChangedListeners: List<OnLocaleChangedListener> = getOnLocaleChangedListeners()
        if (onLocaleChangedListeners.isNotEmpty()) {
            for (i in onLocaleChangedListeners.indices) {
                val onLocaleChangedListener: OnLocaleChangedListener = onLocaleChangedListeners[i]
                onLocaleChangedListener.onAppLocaleChange(oldLocale, newLocale)
            }
        }
        return true
    }

    override fun addOnLocaleChangedListener(listener: OnLocaleChangedListener) {
        listeners.add(listener)
    }

    override fun removeOnLocaleChangedListener(listener: OnLocaleChangedListener) {
        listeners.remove(listener)
    }

    override fun getOnLocaleChangedListeners(): List<OnLocaleChangedListener> {
        return listeners
    }

    override fun refreshLocale(context: Context) {
        updateAppLanguage(context.resources)
    }

    /**
     * 更新 Resources 的语种
     */
    fun updateAppLanguage(resources: Resources) {
        if (LocaleToolUtils.getLocale(resources.configuration) == getCurrentLocale()) {
            return
        }
        LocaleToolUtils.updateLanguages(resources, getCurrentLocale())
    }

    companion object {
        /**
         * 应用上下文对象
         */
        private var sApplication: Application? = null
    }
}