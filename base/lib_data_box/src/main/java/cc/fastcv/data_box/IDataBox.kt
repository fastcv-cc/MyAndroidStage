package cc.fastcv.data_box

interface IDataBox {

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

    suspend fun <T> removeDataByKey(key: String)

    suspend fun clearAllData()

    /**
     * Extend Data Object Type
     */
    suspend fun saveObject(key: String, value: Any)

    suspend fun <T> getObject(key: String, default: T?): T

    suspend fun addOnValueChangedListener(listener: OnValueChangeListener)

    suspend fun removeOnValueChangedListener(listener: OnValueChangeListener)
}