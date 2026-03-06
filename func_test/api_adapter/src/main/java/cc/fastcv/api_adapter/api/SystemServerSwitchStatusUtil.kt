package cc.fastcv.api_adapter.api

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat

object SystemServerSwitchStatusUtil {

    /**
     * 获取GPS开关状态
     */
    fun obtainGPSSwitchStatus(context: Context): Boolean {
        val locationManager = context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * 获取蓝牙开关状态
     */
    fun obtainBluetoothSwitchStatus(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter.isEnabled
    }

    /**
     * 修改蓝牙开关状态
     */
    @SuppressLint("MissingPermission")
    fun modifyBluetoothSwitchStatus(context: Context, status: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            Log.w("SalamJourney", "On Android 12 and above, BLUETOOTH_CONNECT permission is required to operate the Bluetooth switch ！！")
            return
        }
        val manager = context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        if (status) {
            manager.adapter.enable()
        } else {
            manager.adapter.disable()
        }
    }

    /**
     * 获取免打扰dnd模式开关状态
     */
    fun obtainZenModeStatus(app: Application): Boolean {
        return Settings.Global.getInt(app.contentResolver, "zen_mode") != 0
    }

}