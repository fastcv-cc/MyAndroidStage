package cc.fastcv.api_adapter.api.locale.observer

import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.content.res.Resources
import cc.fastcv.api_adapter.api.locale.listener.OnLocaleChangedListener
import cc.fastcv.api_adapter.api.locale.service.LocaleService
import cc.fastcv.api_adapter.api.locale.utils.LocaleSpUtils
import cc.fastcv.api_adapter.api.locale.utils.LocaleToolUtils
import java.util.Locale

class LanguagesObserver : ComponentCallbacks {
    /**
     * 手机的配置发生了变化
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        val newLocale: Locale = LocaleToolUtils.getLocale(newConfig)
        val oldLocale = systemLanguage

        // 更新 Application 的配置，否则会出现横竖屏切换之后 Application 的 orientation 没有随之变化的问题
        LocaleToolUtils.updateConfigurationChanged(
            LocaleService.getApplication(),
            newConfig
        )
        if (newLocale == oldLocale) {
            return
        }
        systemLanguage = newLocale

        // 如果当前的语种是跟随系统变化的，那么就需要重置一下当前 App 的语种
        if (LocaleSpUtils.isSystemLanguage(LocaleService.getApplication())) {
            LocaleSpUtils.clearLanguage(LocaleService.getApplication())
        }
        val listeners: List<OnLocaleChangedListener> =
            LocaleService.getOnLocaleChangedListeners()
        if (listeners.isNotEmpty()) {
            for (i in listeners.indices) {
                val listener: OnLocaleChangedListener = listeners[i]
                listener.onSystemLocaleChange(oldLocale!!, newLocale)
            }
        }
    }

    override fun onLowMemory() {}

    companion object {
        /**
         * 获取系统的语种
         */
        /** 系统语种  */
        @Volatile
        var systemLanguage: Locale? = null
            private set

        init {
            // 获取当前系统的语种
            val configuration = Resources.getSystem().configuration
            if (configuration != null) {
                systemLanguage = LocaleToolUtils.getLocale(configuration)
            }
            if (systemLanguage == null) {
                systemLanguage = Locale("en-US")
            }
        }

        /**
         * 注册系统语种变化监听
         */
        fun register(application: Application) {
            application.registerComponentCallbacks(LanguagesObserver())
        }
    }
}