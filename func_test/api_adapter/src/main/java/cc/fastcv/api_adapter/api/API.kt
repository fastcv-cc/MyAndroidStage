package cc.fastcv.api_adapter.api

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

/**
 * 获取当前应用所有 Activity 的类名列表
 */
fun getAllActivities(context: Context): List<String> {
    return try {
        val pm = context.packageManager
        val packageName = context.packageName

        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        }

        packageInfo.activities?.map { it.name } ?: emptyList()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        emptyList()
    }
}

/**
 * 获取当前应用所有 Service 的类名列表
 */
fun getAllServices(context: Context): List<String> {
    return try {
        val pm = context.packageManager
        val packageName = context.packageName

        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_SERVICES.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(packageName, PackageManager.GET_SERVICES)
        }

        packageInfo.services?.map { it.name } ?: emptyList()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        emptyList()
    }
}