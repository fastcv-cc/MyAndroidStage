package cc.fastcv.data_box

interface IBoxDataSerial {

    fun <T> getObject(jsonStr: String): T

    fun toJsonStr(value: Any): String

}