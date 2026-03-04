package cc.fastcv.stage.flow_event_bus.core

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import cc.fastcv.stage.flow_event_bus.utils.launchWhenStateAtLeast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.lang.Exception

/**
 * 自定义EventBus的核心
 */
class EventBusCore : ViewModel() {

    //正常事件
    private val eventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    //粘性事件
    private val stickyEventFlows: HashMap<String, MutableSharedFlow<Any>> = HashMap()

    //单次处理事件
    private val eventLiveDatas: HashMap<String, MutableLiveData<Any>> = HashMap()

    private fun getEventFlow(eventName: String, isSticky: Boolean): MutableSharedFlow<Any> {
        return if (isSticky) {
            stickyEventFlows[eventName]
        } else {
            eventFlows[eventName]
        } ?: MutableSharedFlow<Any>(
            replay = if (isSticky) 1 else 0,
            extraBufferCapacity = Int.MAX_VALUE
        ).also {
            if (isSticky) {
                stickyEventFlows[eventName] = it
            } else {
                eventFlows[eventName] = it
            }
        }
    }


    private fun getEventLiveData(eventName: String): MutableLiveData<Any> {
        return eventLiveDatas[eventName] ?: MutableLiveData<Any>().also {
            eventLiveDatas[eventName] = it
        }
    }

    fun <T : Any> observeEvent(
        lifecycleOwner: LifecycleOwner,
        eventName: String,
        minState: Lifecycle.State,
        dispatcher: CoroutineDispatcher,
        isSticky: Boolean,
        onReceived: (T) -> Unit
    ): Job {
        return lifecycleOwner.launchWhenStateAtLeast(minState) {
            getEventFlow(eventName, isSticky).collect {
                this.launch(dispatcher) {
                    invokeReceived(it, onReceived)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SingleEvent> observeSingleEvent(
        lifecycleOwner: LifecycleOwner,
        eventName: String,
        dispatcher: CoroutineDispatcher,
        onReceived: (T) -> Unit
    ) {
        getEventLiveData(eventName).observe(lifecycleOwner) {
            lifecycleOwner.lifecycleScope.launch(dispatcher) {
                try {
                    ( it as T).apply {
                        if (!this.isAbandoned()) {
                            onReceived.invoke(this)
                            this.abandoned()
                            getEventLiveData(eventName).postValue(this)
                        }
                    }
                } catch (e: ClassCastException) {
                    Log.w("EventBusCore", "class cast error on message received: $it", e)
                } catch (e: Exception) {
                    Log.w("EventBusCore", "error on message received: $it", e)
                }
            }
        }
    }

    fun postEvent(eventName: String, value: Any, timeMillis: Long) {
        listOfNotNull(
            getEventFlow(eventName, false),
            getEventFlow(eventName, true)
        ).forEach {
            viewModelScope.launch {
                delay(timeMillis)
                it.emit(value)
            }
        }
    }

    fun postSingleEvent(eventName: String, value: Any, timeMillis: Long) {
        viewModelScope.launch {
            delay(timeMillis)
            getEventLiveData(eventName).postValue(value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> invokeReceived(value: Any, onReceived: (T) -> Unit) {
        try {
            onReceived.invoke(value as T)
        } catch (e: ClassCastException) {
            Log.w("EventBusCore", "class cast error on message received: $value", e)
        } catch (e: Exception) {
            Log.w("EventBusCore", "error on message received: $value", e)
        }
    }

}