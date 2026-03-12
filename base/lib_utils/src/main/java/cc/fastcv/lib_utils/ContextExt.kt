package cc.fastcv.lib_utils

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat

/**
 * 将值用作dp转px
 */
fun Context.dip2pxExt(dpValue: Float): Float {
    val density = resources.displayMetrics.density
    return dpValue * density
}

/**
 * 将值用作px转dp
 */
fun Context.px2dpExt(pxValue: Float): Float {
    val density = resources.displayMetrics.density
    return pxValue / density
}

/**
 * 将值用作px转dp
 */
fun Context.px2dpiExt(pxValue: Float): Float {
    return px2dpExt(pxValue)
}

/** 将**sp**单位的值[spValue]转换为**px**单位的值[Float] */
fun Context.sp2pxExt(spValue: Float): Float {
    val density = resources.displayMetrics.scaledDensity
    return spValue * density
}

/**
 * 将**px**单位的值[pxValue]转换为**sp**单位的值[Float]
 */
fun Context.px2spExt(pxValue: Float): Float {
    val density = resources.displayMetrics.scaledDensity
    return pxValue / density
}

/**
 * 获取语言
 */
fun Context.getLocaleLanguageExt(): String {
    return resources.configuration.locale.language
}

/**
 * 获取国家码
 */
fun Context.getCountryZipCodeExt(): String {
    return resources.configuration.locale.country
}

/**
 * 获取应用版本名称
 */
fun Context.getVersionNameExt(): String {
    var verName = ""
    try {
        verName = packageManager.getPackageInfo(
            packageName, 0
        ).versionName ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return verName
}

/**
 * 获取应用版本号
 */
fun Context.getVersionCodeExt(): Long {
    var versionName = 0L
    try {
        versionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(
                packageName, 0
            ).longVersionCode
        } else {
            packageManager.getPackageInfo(
                packageName, 0
            ).versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return versionName
}

fun Context.getDeviceIdentifierExt(): String = DeviceIdentifierUtil().getDeviceId(this)

/**
 * 获取GPS开关状态
 */
fun Context.obtainGPSSwitchStatusExt(): Boolean {
    val locationManager =
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

/**
 * 获取蓝牙开关状态
 */
fun Context.obtainBluetoothSwitchStatusExt(): Boolean {
    val manager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    return manager.adapter.isEnabled
}

/**
 * 修改蓝牙开关状态
 */
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun Context.modifyBluetoothSwitchStatusExt(status: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_DENIED
    ) {
        Log.w(
            "SalamJourney",
            "On Android 12 and above, BLUETOOTH_CONNECT permission is required to operate the Bluetooth switch ！！"
        )
        return
    }
    val manager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    if (status) {
        manager.adapter.enable()
    } else {
        manager.adapter.disable()
    }
}

/**
 * 获取免打扰dnd模式开关状态
 */
fun Context.obtainZenModeStatusExt(app: Application): Boolean {
    return Settings.Global.getInt(app.contentResolver, "zen_mode") != 0
}

/**
 * 安卓8.0及以上判断通知渠道是否开启了通知权限
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Context.isNotificationEnabledExt(channel: NotificationChannel): Boolean {
    return channel.importance != NotificationManager.IMPORTANCE_NONE
}

/**
 * 判断通知总开关是否开启
 */
fun Context.isNotificationEnabledExt(): Boolean {
    var isOpened = false
    isOpened = try {
        NotificationManagerCompat.from(this).areNotificationsEnabled()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
    return isOpened
}

/**
 * 跳转去开启通知监听服务
 */
fun Context.openNotificationAccessAuthorizationActivityExt() {
    this.startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
}

/**
 * 判断通知监听服务是否开启
 */
fun Context.getNotificationListenerServerStatusExt(): Boolean {
    val pkgName: String = packageName
    val flat: String = Settings.Secure.getString(
        contentResolver,
        "enabled_notification_listeners"
    )
    if (!TextUtils.isEmpty(flat)) {
        val names = flat.split(":").toTypedArray()
        for (i in names.indices) {
            val cn = ComponentName.unflattenFromString(names[i])
            if (cn != null) {
                if (TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
    }
    return false
}