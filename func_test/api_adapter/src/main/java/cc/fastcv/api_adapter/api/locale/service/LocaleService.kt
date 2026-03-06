package cc.fastcv.api_adapter.api.locale.service

import android.app.Application
import android.content.Context
import cc.fastcv.api_adapter.api.locale.listener.OnLocaleChangedListener
import java.util.Locale

object LocaleService : ILocaleService {
    private val mDelegate: ILocaleService = LocaleServiceImpl()
    override fun init(application: Application, inject: Boolean) {
        mDelegate.init(application,inject)
    }

    override fun getApplication(): Application {
        return mDelegate.getApplication()
    }

    override fun attachBaseContext(context: Context): Context {
        return mDelegate.attachBaseContext(context)
    }

    override fun getCurrentLocale(): Locale? {
        return mDelegate.getCurrentLocale()
    }

    override fun getSystemLocale(): Locale? {
        return mDelegate.getSystemLocale()
    }

    override fun isSystemLocale(): Boolean {
        return false
    }

    override fun getCurrentLocaleTag(): String {
        return mDelegate.getCurrentLocaleTag()
    }

    override fun getCurrentLang(): Locale? {
        return mDelegate.getCurrentLang()
    }

    override fun getCurrentLangTag(): String {
        return mDelegate.getCurrentLangTag()
    }

    override fun getSupportLocaleList(): List<Locale?> {
        return mDelegate.getSupportLocaleList()
    }

    override fun clearAppLanguage(context: Context): Boolean {
        return mDelegate.clearAppLanguage(context)
    }

    override fun setAppLanguage(context: Context, newLocale: Locale): Boolean {
        return mDelegate.setAppLanguage(context, newLocale)
    }

    override fun addOnLocaleChangedListener(listener: OnLocaleChangedListener) {
        mDelegate.addOnLocaleChangedListener(listener)
    }

    override fun removeOnLocaleChangedListener(listener: OnLocaleChangedListener) {
        mDelegate.removeOnLocaleChangedListener(listener)
    }

    override fun getOnLocaleChangedListeners(): List<OnLocaleChangedListener> {
        return mDelegate.getOnLocaleChangedListeners()
    }

    override fun refreshLocale(context: Context) {
        mDelegate.refreshLocale(context)
    }
}