package cc.fastcv.data_box

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface IBoxDataEditor {

    fun initBoxDataEditor(context: Context, boxName: String)

    suspend fun saveString(key: String, value: String)

    suspend fun getString(key: String, default: String): String

    suspend fun saveFloat(key: String, value: Float)

    suspend fun getFloat(key: String, default: Float): Float

    suspend fun saveBoolean(key: String, value: Boolean)

    suspend fun getBoolean(key: String, default: Boolean): Boolean

    suspend fun saveLong(key: String, value: Long)

    suspend fun getLong(key: String, default: Long): Long

    suspend fun saveInt(key: String, value: Int)

    suspend fun getInt(key: String, default: Int): Int

    suspend fun removeDataByKey(key: String)

    suspend fun clearAllData()

}