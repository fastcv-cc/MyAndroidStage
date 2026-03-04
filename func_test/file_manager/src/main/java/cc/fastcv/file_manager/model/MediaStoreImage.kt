package cc.fastcv.file_manager.model

import android.net.Uri
import java.util.Date

class MediaStoreImage(
    val id: Long,
    val displayName: String?,
    val dateAdded: Date?,
    val contentUri: Uri?,
    val lat: Double?,
    val lon: Double?
)