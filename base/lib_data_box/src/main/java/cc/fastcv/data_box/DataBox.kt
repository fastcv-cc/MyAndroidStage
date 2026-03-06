package cc.fastcv.data_box

import android.content.Context
import java.lang.ref.SoftReference

class DataBox(
    context: Context,
    boxName: String,
    private val dataEditor: IBoxDataEditor,
    private val dataSerial: IBoxDataSerial
) : IDataBox {

    init {
        dataEditor.initBoxDataEditor(context, boxName)
    }

    private val onValueChangeListenerList = mutableListOf<OnValueChangeListener>()

    override suspend fun saveString(key: String, value: String) {
        dataEditor.saveString(key, value)
        notifyValueChanged(key)
    }

    override suspend fun getString(key: String, default: String): String {
        return dataEditor.getString(key, default)
    }

    override suspend fun saveFloat(key: String, value: Float) {
        dataEditor.saveFloat(key, value)
        notifyValueChanged(key)
    }

    override suspend fun getFloat(key: String, default: Float): Float {
        return dataEditor.getFloat(key, default)
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        dataEditor.saveBoolean(key, value)
        notifyValueChanged(key)
    }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean {
        return dataEditor.getBoolean(key, default)
    }

    override suspend fun saveLong(key: String, value: Long) {
        dataEditor.saveLong(key, value)
        notifyValueChanged(key)
    }

    override suspend fun getLong(key: String, default: Long): Long {
        return dataEditor.getLong(key, default)
    }

    override suspend fun saveInt(key: String, value: Int) {
        dataEditor.saveInt(key, value)
        notifyValueChanged(key)
    }

    override suspend fun getInt(key: String, default: Int): Int {
        return dataEditor.getInt(key, default)
    }

    override suspend fun <T> removeDataByKey(key: String) {
        dataEditor.removeDataByKey(key)
    }

    override suspend fun clearAllData() {
        dataEditor.clearAllData()
    }

    override suspend fun saveObject(key: String, value: Any) {
        val jsonStr = dataSerial.toJsonStr(value)
        saveString(key, jsonStr)
        notifyValueChanged(key)
    }

    override suspend fun <T> getObject(key: String, default: T?): T {
        val jsonStr = getString(key, "")
        return dataSerial.getObject(jsonStr)
    }

    override suspend fun addOnValueChangedListener(listener: OnValueChangeListener) {
        synchronized(onValueChangeListenerList) {
            if (!onValueChangeListenerList.contains(listener)) {
                onValueChangeListenerList.add(listener)
            }
        }
    }

    override suspend fun removeOnValueChangedListener(listener: OnValueChangeListener) {
        synchronized(onValueChangeListenerList) {
            if (onValueChangeListenerList.contains(listener)) {
                onValueChangeListenerList.remove(listener)
            }
        }
    }

    private fun notifyValueChanged(key: String) {
        onValueChangeListenerList.forEach {
            it.onValueChanged(this, key)
        }
    }
}