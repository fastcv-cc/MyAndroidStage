package cc.fastcv.api_adapter.api.locale.observer

import android.app.Activity
import android.app.Application
import android.os.Bundle
import cc.fastcv.api_adapter.api.locale.service.LocaleService

class ActivityCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LocaleService.refreshLocale(activity)
        LocaleService.refreshLocale(activity.application)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        LocaleService.refreshLocale(activity)
        LocaleService.refreshLocale(activity.application)
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    companion object {
        fun inject(application: Application) {
            application.registerActivityLifecycleCallbacks(ActivityCallback())
        }
    }
}