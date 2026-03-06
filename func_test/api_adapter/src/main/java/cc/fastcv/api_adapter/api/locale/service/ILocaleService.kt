package cc.fastcv.api_adapter.api.locale.service

import android.app.Application
import android.content.Context
import cc.fastcv.api_adapter.api.locale.listener.OnLocaleChangedListener
import java.util.Locale

interface ILocaleService {

    /**
     * 初始化操作
     */
    fun init(application: Application, inject: Boolean)

    /**
     * 获取上下文
     */
    fun getApplication(): Application

    /**
     * 用这个方法替换 Activity 的 attachBaseContext 内部处理了 7.0 及之后版本切换 locale 的逻辑
     *
     * @param context context
     * @return N 版本及之后版本，返回 ConfigurationContext，之前版本直接返回原本的 context
     */
    fun attachBaseContext(context: Context): Context

    /**
     * 获取当前 app 使用的 locale
     */
    fun getCurrentLocale(): Locale?

    /**
     * 获取当前 系统 使用的 locale
     */
    fun getSystemLocale(): Locale?

    /**
     * 判断是否是系统使用的locale
     */
    fun isSystemLocale(): Boolean

    /**
     * 获取当前 app 使用的 locale tag
     */
    fun getCurrentLocaleTag(): String

    /**
     * 获取当前 app 使用的 lang
     */
    fun getCurrentLang(): Locale?

    /**
     * 获取当前 app 使用的 lang tag
     */
    fun getCurrentLangTag(): String

    /**
     * 获取支持的语言列表
     *
     * 用于侧边栏选择语言 可能是从Apollo读出来的
     */
    fun getSupportLocaleList(): List<Locale?>

    /**
     * 跟随系统语种（返回 true 表示需要重启 App）
     */
    fun clearAppLanguage(context: Context): Boolean

    /**
     * 设置当前的语种（返回 true 表示需要重启 App）
     */
    fun setAppLanguage(context: Context, newLocale: Locale): Boolean

    /**
     * 添加locale变化监听
     */
    fun addOnLocaleChangedListener(listener: OnLocaleChangedListener)

    /**
     * 移除locale变化监听
     *
     * @param listener listener监听事件
     */
    fun removeOnLocaleChangedListener(listener: OnLocaleChangedListener)

    /**
     * 获取locale变化监听集合数组
     *
     * @return list数组
     */
    fun getOnLocaleChangedListeners(): List<OnLocaleChangedListener>

    /**
     * 更新locale操作
     *
     * @param context 上下文
     */
    fun refreshLocale(context: Context)

}