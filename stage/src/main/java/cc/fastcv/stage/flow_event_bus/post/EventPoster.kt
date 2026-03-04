package cc.fastcv.stage.flow_event_bus.post

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cc.fastcv.stage.flow_event_bus.core.EventBusCore
import cc.fastcv.stage.flow_event_bus.store.ApplicationScopeViewModelProvider

/**
 * 发送全局事件的方法
 */
inline fun <reified T> postGlobalEvent(event: T, timeMillis: Long = 0L) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .postEvent(T::class.java.name, event!!, timeMillis)
}

/**
 * 发送普通事件的方法
 */
inline fun <reified T> ViewModelStoreOwner.postEvent(event: T, timeMillis: Long = 0L) {
    ViewModelProvider(this)[EventBusCore::class.java]
        .postEvent(T::class.java.name, event!!, timeMillis)
}

/**
 * 发起单次处理事件的方法
 */
inline fun <reified T> postSingleEvent(event: T, timeMillis: Long = 0L) {
    ApplicationScopeViewModelProvider.getApplicationScopeViewModel(EventBusCore::class.java)
        .postSingleEvent(T::class.java.name, event!!, timeMillis)
}