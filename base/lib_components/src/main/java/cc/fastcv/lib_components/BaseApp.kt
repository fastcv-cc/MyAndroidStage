package cc.fastcv.lib_components

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner


class BaseApp : Application(), DefaultLifecycleObserver {

    companion object {
        var isForeground: Boolean = false

        fun isAppInForeground(): Boolean {
            return isForeground
        }
    }

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        isForeground = true
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        isForeground = false
    }


}