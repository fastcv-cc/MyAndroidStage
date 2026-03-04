package cc.fastcv.file_manager.model

import android.net.Uri

open class MediaInfo {
    // 大小 单位B
    var size: Long = 0

    // 宽
    var width = 0f

    // 高
    var height = 0f

    // 媒体文件Uri
    var localPathUri: Uri? = null

    // 本地文件Path
    var localPath: String? = null

    // 文件名
    var fileName: String? = null

    // 媒体类型
    var mimeType: String? = null

    // 媒体ID
    var mediaId: String? = null

    // 最后更改时间
    var lastModified: Long? = null
}
