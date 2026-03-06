package cc.fastcv.api_adapter.api

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

/**
 * 通知处理/判断相关的方法类
 */
object NotificationUtil {

    private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

    /**
     * 安卓8.0及以上判断通知渠道是否开启了通知权限
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun isNotificationEnabled(context: Context, channel: NotificationChannel): Boolean {
        return channel.importance != NotificationManager.IMPORTANCE_NONE
    }

    /**
     * 判断通知总开关是否开启
     */
    fun isNotificationEnabled(context: Context): Boolean {
        var isOpened = false
        isOpened = try {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
        return isOpened
    }

    /**
     * 跳转去开启通知监听服务
     */
    fun Context.openNotificationAccessAuthorizationActivity() {
        this.startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    /**
     * 判断通知监听服务是否开启
     */
    fun getNotificationListenerServerStatus(context: Context): Boolean {
        val pkgName: String = context.packageName
        val flat: String = Settings.Secure.getString(
            context.contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
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
}