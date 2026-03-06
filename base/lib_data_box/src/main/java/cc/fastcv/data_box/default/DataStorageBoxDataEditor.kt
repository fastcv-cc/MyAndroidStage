package cc.fastcv.data_box.default

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import cc.fastcv.data_box.IBoxDataEditor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStorageBoxDataEditor : IBoxDataEditor {

    private lateinit var dataStore: DataStore<Preferences>

    override fun initBoxDataEditor(context: Context, boxName: String) {
        dataStore = preferencesDataStore(context = context, name = boxName)
    }

    override suspend fun saveString(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    override suspend fun getString(key: String, default: String): String {
        return dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
    }

    override suspend fun saveFloat(key: String, value: Float) {
        dataStore.edit { it[floatPreferencesKey(key)] = value }
    }

    override suspend fun getFloat(key: String, default: Float): Float {
        return dataStore.data.map { it[floatPreferencesKey(key)] ?: default }.first()
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean {
        return dataStore.data.map { it[booleanPreferencesKey(key)] ?: default }.first()
    }

    override suspend fun saveLong(key: String, value: Long) {
        dataStore.edit { it[longPreferencesKey(key)] = value }
    }

    override suspend fun getLong(key: String, default: Long): Long {
        return dataStore.data.map { it[longPreferencesKey(key)] ?: default }.first()
    }

    override suspend fun saveInt(key: String, value: Int) {
        dataStore.edit { it[intPreferencesKey(key)] = value }
    }

    override suspend fun getInt(key: String, default: Int): Int {
        return dataStore.data.map { it[intPreferencesKey(key)] ?: default }.first()
    }

    override suspend fun removeDataByKey(key: String) {
        dataStore.edit {
            it.remove(stringPreferencesKey(key))
            it.remove(floatPreferencesKey(key))
            it.remove(booleanPreferencesKey(key))
            it.remove(longPreferencesKey(key))
            it.remove(intPreferencesKey(key))
        }
    }

    override suspend fun clearAllData() {
        dataStore.edit { it.clear() }
    }
}