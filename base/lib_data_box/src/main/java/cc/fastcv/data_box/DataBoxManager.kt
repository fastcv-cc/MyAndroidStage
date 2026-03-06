package cc.fastcv.data_box

import android.app.Application
import cc.fastcv.data_box.default.DataStorageBoxDataEditor
import cc.fastcv.data_box.default.GsonBoxDataSerial

object DataBoxManager {

    private var app: Application? = null

    private val boxMap = mutableMapOf<String, IDataBox>()

    fun init(application: Application) {
        app = application
    }

    fun getDataBoxByName(
        boxName: String,
        dataEditor: IBoxDataEditor? = null,
        dataSerial: IBoxDataSerial? = null
    ): IDataBox {

        synchronized(this) {
            if (app == null) {
                throw IllegalArgumentException("DataBoxManager: please call DataBoxManager.init(...) first!!!")
            } else {
                var box = boxMap[boxName]
                if (box == null) {
                    box = DataBox(
                        app!!,
                        boxName,
                        dataEditor ?: DataStorageBoxDataEditor(),
                        dataSerial ?: GsonBoxDataSerial()
                    )
                    boxMap[boxName] = box
                }
                return box
            }
        }
    }

    fun getDefaultDataBox(): IDataBox {
        if (app == null) {
            throw IllegalArgumentException("DataBoxManager: please call DataBoxManager.init(...) first!!!")
        } else {
            return getDataBoxByName(app!!.packageName)
        }
    }

}