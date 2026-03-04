package cc.fastcv.file_manager

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.file_manager.adapter.GalleryAdapter
import cc.fastcv.file_manager.model.MediaStoreImage
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.stage.StageActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class CustomSingleImageSelectActivity : StageActivity(),
    BaseRecyclerViewAdapter.OnItemClickListener<MediaStoreImage> {

    private lateinit var galleryAdapter: GalleryAdapter

    private lateinit var contentObserver: ContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_single_image_select)
        galleryAdapter = GalleryAdapter()

        contentObserver = object : ContentObserver(Handler()) {
            override fun onChange(selfChange: Boolean) {
                loadImages()
            }
        }

        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )


        val rv = findViewById<RecyclerView>(R.id.gallery)
        rv.setAdapter(galleryAdapter)
        loadImages()
        galleryAdapter.setOnItemClickListener(this)
    }

    private fun loadImages() {
        object : Thread() {
            override fun run() {
                val imageList: MutableList<MediaStoreImage> = queryImages()
                runOnUiThread { galleryAdapter.setData(imageList) }
            }
        }.start()
    }

    @SuppressLint("Recycle")
    private fun queryImages(): MutableList<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )
        val selection = MediaStore.Images.Media.DATE_ADDED + " >= ?"
        val selectionArgs = arrayOf(dateToTimestamp(22, 10, 2008).toString() + "")
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        var idColumn = 0
        if (cursor != null) {
            idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        }
        var dateModifiedColumn = 0
        if (cursor != null) {
            dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
        }
        var displayNameColumn = 0
        if (cursor != null) {
            displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        }
        if (cursor == null) {
            return images
        }
        while (cursor.moveToNext()) {
            // Here we'll use the column indexs that we found above.
            val id = cursor.getLong(idColumn)
            val dateModified = Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
            val displayName = cursor.getString(displayNameColumn)
            val contentUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            val lat = 0.0
            val lon = 0.0
            val image = MediaStoreImage(id, displayName, dateModified, contentUri, lat, lon)
            images.add(image)
        }
        cursor.close()
        return images
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val time: Long = try {
            simpleDateFormat.parse("$day.$month.$year")!!.time
        } catch (e: Exception) {
            0L
        }
        return TimeUnit.MICROSECONDS.toSeconds(time)
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }

    override fun onItemClick(view: View?, position: Int, t: MediaStoreImage) {
        val intent = Intent()
        intent.setData(t.contentUri)
        setResult(200, intent)
        finish()
    }
}