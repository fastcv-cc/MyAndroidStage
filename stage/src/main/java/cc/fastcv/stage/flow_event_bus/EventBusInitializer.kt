package cc.fastcv.stage.flow_event_bus

import android.app.Application

object EventBusInitializer {

    internal lateinit var application: Application

    fun init(application: Application) {
        EventBusInitializer.application = application
    }

}