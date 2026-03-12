package cc.fastcv.lib_utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.Collections
import java.util.UUID
import androidx.core.content.edit

/**
 * 设备标识符获取方法类
 */
internal class DeviceIdentifierUtil {

    companion object {
        private const val PREF_DEVICE_ID = "pref_device_id"
        private const val PREF_SP_NAME = "DeviceIdentifier"
    }

    fun getDeviceId(context: Context): String {
        var deviceId =
            context.getSharedPreferences(PREF_SP_NAME, Context.MODE_PRIVATE).getString(PREF_DEVICE_ID, "")?:""

        if (deviceId.isNotEmpty()) {
            return deviceId
        }

        deviceId = getIMEI(context)

        if (deviceId.isEmpty()) {
            deviceId = getSerialNumber()
        }

        if (deviceId.isEmpty()) {
            deviceId = getMacAddress(context)
        }

        if (deviceId.isEmpty()) {
            deviceId = getUUID(context)
        }

        if (deviceId.isEmpty()) {
            deviceId = getAndroidId(context)
        }

        if (deviceId.isEmpty()) {
            deviceId = getUniquePsuedoID()
        }

        deviceId = toMD5(deviceId)
        context.getSharedPreferences(PREF_SP_NAME, Context.MODE_PRIVATE).edit {
            putString(
                PREF_DEVICE_ID, deviceId
            )
        }
        return deviceId
    }


    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getIMEI(context: Context): String {
        return try {
            val telephonyManager =
                context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.deviceId ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun getAndroidId(context: Context): String {
        return Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }


    @SuppressLint("MissingPermission")
    private fun getSerialNumber(): String {
        return try {
            val serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Build.getSerial()
            } else {
                Build.SERIAL
            }
            if (serial.isEmpty() || serial == "UNKNOWN" || serial == "unknown") {
                ""
            } else {
                serial
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun getMacAddress(context: Context?): String {
        var macAddress: String? = ""
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Android  6.0 之前（不包括6.0）
            macAddress = getMacDefault(context)
            if (macAddress != null) {
                macAddress = macAddress.replace(":".toRegex(), "")
                if (!macAddress.equals("020000000000", ignoreCase = true)) {
                    return macAddress
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // Android 6.0（包括） - Android 7.0（不包括）
            macAddress = getMacBelowSeven()
            if (macAddress != null) {
                macAddress = macAddress.replace(":".toRegex(), "")
                if (!macAddress.equals("020000000000", ignoreCase = true)) {
                    return macAddress
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 高于7.0
            macAddress = getMacAboveSeven()
            if (macAddress != null) {
                macAddress = macAddress.replace(":".toRegex(), "")
                if (!macAddress.equals("020000000000", ignoreCase = true)) {
                    return macAddress
                }
            }
        }
        return macAddress ?: ""
    }

    private fun getMacAboveSeven(): String? {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            Log.d("Utils", "all:" + all.size)
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) {
                    continue
                }
                val macBytes = nif.hardwareAddress ?: return null
                Log.d("Utils", "macBytes:" + macBytes.size + "," + nif.name)
                val res1 = java.lang.StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getMacDefault(context: Context?): String? {
        var mac: String? = null
        if (context == null) {
            return mac
        }
        val wifi = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        var info: WifiInfo? = null
        try {
            info = wifi.connectionInfo
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        if (info == null) {
            return null
        }
        mac = info.macAddress
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.uppercase()
        }
        return mac
    }

    private fun getMacBelowSeven(): String? {
        var wifiAddress: String? = null
        try {
            wifiAddress =
                BufferedReader(FileReader(File("/sys/class/net/wlan0/address"))).readLine()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return wifiAddress
    }

    /**
     * 得到全局唯一UUID，保存数据到系统数据库中：Settings.System
     */
    private fun getUUID(context: Context): String {
        try {
            var uniqueIdentificationCode = ""
            //首先判断系统版本，高于6.0，则需要动态申请权限
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.System.canWrite(context)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + context.packageName)
                    )
                    context.startActivity(intent)
                } else {
                    uniqueIdentificationCode =
                        Settings.System.getString(
                            context.contentResolver,
                            "uniqueIdentificationCode"
                        )
                }
            } else {
                //获取系统配置文件中的数据，第一个参数固定的，但是需要上下文，第二个参数是之前保存的Key，第三个参数表示如果没有这个key的情况的默认值
                uniqueIdentificationCode =
                    Settings.System.getString(context.contentResolver, "uniqueIdentificationCode")
            }
            if (TextUtils.isEmpty(uniqueIdentificationCode)) {
                uniqueIdentificationCode = "${System.currentTimeMillis()}${
                    UUID.randomUUID().toString().substring(20)
                }".replace("-", "")
                //设置系统配置文件中的数据，第一个参数固定的，但是需要上下文，第二个参数是保存的Key，第三个参数是保存的value
                Settings.System.putString(
                    context.contentResolver,
                    "uniqueIdentificationCode",
                    uniqueIdentificationCode
                )
            }
            return uniqueIdentificationCode
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }


    private fun getUniquePsuedoID(): String {
        val mSzDevIDShort =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 位
        val serial = try {
            Build::class.java.getField("SERIAL")[null].toString()
        } catch (exception: Exception) {
            //serial需要一个初始化
            "serial" // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return UUID(mSzDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }

    private fun toMD5(text: String): String {
        //获取摘要器 MessageDigest
        val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
        //通过摘要器对字符串的二进制字节数组进行hash计算
        val digest: ByteArray = messageDigest.digest(text.toByteArray())
        val sb: StringBuilder = StringBuilder()

        for (element in digest) {
            //循环每个字符 将计算结果转化为正整数
            val digestInt = element.toInt() and 0xff
            //将10进制转化为较短的16进制
            val hexString = Integer.toHexString(digestInt)
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length < 2) {
                sb.append(0)
            }
            //将循环结果添加到缓冲区
            sb.append(hexString)
        }
        //返回整个结果
        return sb.toString()
    }
}