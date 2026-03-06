package cc.fastcv.api_adapter.api

import android.content.Context

fun Context.getDeviceId(): String = DeviceIdentifierUtil().getDeviceId(this)