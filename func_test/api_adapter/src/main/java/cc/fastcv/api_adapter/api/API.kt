package cc.fastcv.api_adapter.api

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import java.io.File

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

fun startCompatAppApplicationInfoActivity(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = "package:${context.packageName}".toUri()
    context.startActivity(intent)
}


fun startSelfPageInGooglePlay(context: Context) {
    val uri = "market://details?id=${context.packageName}".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.android.vending")
    context.startActivity(intent)
}

/**
 * 获取从后台拉起 或者 启动app的Intent
 * 高版本似乎只能拉起自己
 */
fun getBackAppIntent(packageManager: PackageManager, packageName: String): Intent? {
    return packageManager.getLaunchIntentForPackage(packageName)
}

/**
 * 获取跳转拨号盘并携带号码的Intent
 */
fun startCallPhone(context: Context, contactPhone: String) {
    context.startActivity(
        Intent(
            Intent.ACTION_DIAL,
            "tel:$contactPhone".toUri()
        )
    )
}

fun startCompatSystemNotificationSettingActivity(context: Context,) {
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= 26) {
        // android 8.0引导
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
    } else {
        // android 5.0-7.0
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
    }
    intent.putExtra("app_package", context.packageName)
    intent.putExtra("app_uid", context.applicationInfo.uid)
    context.startActivity(intent)
}


/**
 * 分享纯文字
 */
fun shareText(context: Context, link: String?, title: String?) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, link)
    context.startActivity(Intent.createChooser(intent, title))
}

/**
 * 分享单张图片
 */
fun shareImage(context: Context, uri: Uri?, title: String?) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/png"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    context.startActivity(Intent.createChooser(intent, title))
}

/**
 * 分享单张图片
 */
fun shareImage(context: Context, imagePath: String?) {
    //String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
    //由文件得到uri
    val imageUri = Uri.fromFile(File(imagePath))
    //输出：file:///storage/emulated/0/test.jpg
    Log.d("share", "uri:$imageUri")
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
    shareIntent.type = "image/*"
    context.startActivity(Intent.createChooser(shareIntent, "分享到"))
}

/**
 * 分享功能
 * @param context       上下文
 * @param msgTitle      消息标题
 * @param msgText       消息内容
 * @param imgPath       图片路径，不分享图片则传null
 */
fun shareTextAndImage(context: Context, msgTitle: String?, msgText: String?, imgPath: String?) {
    val intent = Intent(Intent.ACTION_SEND)
    if (imgPath == null || imgPath == "") {
        // 纯文本
        intent.type = "text/plain"
    } else {
        val f = File(imgPath)
        if (f.exists() && f.isFile) {
            intent.type = "image/jpg"
            val u = Uri.fromFile(f)
            intent.putExtra(Intent.EXTRA_STREAM, u)
        }
    }
    intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
    intent.putExtra(Intent.EXTRA_TEXT, msgText)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(Intent.createChooser(intent, "分享到"))
}

/**
 * 分享多个文件
 */
fun shareFile(context: Context, imageUris: ArrayList<Uri?>?) {
    /*ArrayList<Uri> imageUris = new ArrayList<>();
    Uri uri1 = Uri.parse(getResourcesUri(R.drawable.dog));
    Uri uri2 = Uri.parse(getResourcesUri(R.drawable.shu_1));
    imageUris.add(uri1);
    imageUris.add(uri2);*/
    val mulIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
    mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
    mulIntent.type = "image/jpeg"
    context.startActivity(Intent.createChooser(mulIntent, "多文件分享"))
}