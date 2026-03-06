package cc.fastcv.data_box.default

import cc.fastcv.data_box.IBoxDataSerial
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class GsonBoxDataSerial : IBoxDataSerial {

    private val gson by lazy {
        GsonBuilder()
            .disableHtmlEscaping()
            .create()
    }

    override fun <T> getObject(jsonStr: String): T {
        val type = object : TypeToken<MutableList<T>>() {}.type
        return gson.fromJson(jsonStr, type)
    }

    override fun toJsonStr(value: Any): String {
        return gson.toJson(value)
    }

}