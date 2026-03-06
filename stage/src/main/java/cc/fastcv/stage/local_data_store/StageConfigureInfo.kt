package cc.fastcv.stage.local_data_store

import cc.fastcv.data_box.DataBoxManager

object StageConfigureInfo {

    private const val DATA_BOX_NAME = "StageConfigureInfo"

    /**
     * 是否第一次使用此App
     */
    private const val KEY_DATA_STORE_IS_FIRST_USE_APP = "isFirstUseApp"

    private val dataBox = DataBoxManager.getDataBoxByName(DATA_BOX_NAME)

    suspend fun isFirstUseApp(): Boolean {
        return dataBox.getBoolean(KEY_DATA_STORE_IS_FIRST_USE_APP, false)
    }

    suspend fun confirmUseApp() {
        dataBox.saveBoolean(KEY_DATA_STORE_IS_FIRST_USE_APP, true)
    }

}