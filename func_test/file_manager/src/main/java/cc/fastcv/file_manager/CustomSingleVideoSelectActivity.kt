package cc.fastcv.file_manager

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.file_manager.adapter.VideoAdapter
import cc.fastcv.file_manager.model.VideoInfo
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.stage.StageActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.math.max

class CustomSingleVideoSelectActivity : StageActivity(),
    BaseRecyclerViewAdapter.OnItemClickListener<VideoInfo> {

    companion object {
        private const val VIDEO_FIRST_FRAME_TIME_US = 1000L

        /**
         * 视频缩略图默认压缩尺寸
         */
        private const val THUMBNAIL_DEFAULT_COMPRESS_VALUE = 512f
    }

    private lateinit var videoAdapter: VideoAdapter

    private lateinit var contentObserver: ContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_single_video_select)
        videoAdapter = VideoAdapter()

        contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                loadImages()
            }
        }

        contentResolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )

        val rv = findViewById<RecyclerView>(R.id.gallery)
        rv.setAdapter(videoAdapter)
        videoAdapter.setOnItemClickListener(this)
        loadImages()
    }

    private fun loadImages() {
        object : Thread() {
            override fun run() {
                val videoList: MutableList<VideoInfo> = queryVideos()
                runOnUiThread { videoAdapter.setData(videoList) }
            }
        }.start()
    }

    @SuppressLint("Recycle", "Range")
    private fun queryVideos(): MutableList<VideoInfo> {
        val videos = mutableListOf<VideoInfo>()
        val projection: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            arrayOf(
                MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.BITRATE
            )
        } else {
            arrayOf(
                MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED
            )
        }
        val selection = MediaStore.Video.Media.DATE_ADDED + " >= ?"
        val selectionArgs = arrayOf<String>(dateToTimestamp(22, 10, 2008).toString() + "")
        val sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
            ?: return videos
        while (cursor.moveToNext()) {
            val videoInfo = VideoInfo()
            videoInfo.mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
            videoInfo.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
            videoInfo.width = cursor.getFloat(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH))
            videoInfo.height = cursor.getFloat(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT))
            videoInfo.localPath =
                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            videoInfo.localPathUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoInfo.mediaId!!.toLong()
            )
            videoInfo.fileName =
                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
            videoInfo.mimeType =
                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
            videoInfo.firstFrame = getVideoThumbnail(videoInfo.localPathUri!!)
            videoInfo.duration =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                videoInfo.biteRate =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.BITRATE))
            } else {
                videoInfo.biteRate =
                    (8 * videoInfo.size * 1024 / (videoInfo.duration / 1000f)).toLong()
            }
            videoInfo.addTime =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
            videoInfo.lastModified =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
            videos.add(videoInfo)
        }
        cursor.close()
        return videos
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val time: Long = try {
            simpleDateFormat.parse("$day.$month.$year")!!.time
        } catch (e: ParseException) {
            0L
        }
        return TimeUnit.MICROSECONDS.toSeconds(time)
    }

    /**
     * 获取视频缩略图：通过绝对路径抓取第一帧
     */
    private fun getVideoThumbnail(uri: Uri): Bitmap? {
        val bitmap: Bitmap?
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(this, uri)
            // OPTION_CLOSEST_SYNC：在给定的时间，检索最近一个同步与数据源相关联的的帧（关键帧）
            // OPTION_CLOSEST：表示获取离该时间戳最近帧（I帧或P帧）
            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                retriever.getScaledFrameAtTime(
                    VIDEO_FIRST_FRAME_TIME_US,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC,
                    THUMBNAIL_DEFAULT_COMPRESS_VALUE.toInt(),
                    THUMBNAIL_DEFAULT_COMPRESS_VALUE.toInt()
                )
            } else {
                compressVideoThumbnail(retriever.getFrameAtTime(VIDEO_FIRST_FRAME_TIME_US))
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 压缩视频缩略图
     */
    private fun compressVideoThumbnail(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        val width = bitmap.getWidth()
        val height = bitmap.getHeight()
        val max = max(width.toDouble(), height.toDouble()).toInt()
        if (max > THUMBNAIL_DEFAULT_COMPRESS_VALUE) {
            val scale = THUMBNAIL_DEFAULT_COMPRESS_VALUE / max
            val w = (scale * width).toInt()
            val h = (scale * height).toInt()
            return compressVideoThumbnail(bitmap, w, h)
        }
        return bitmap
    }

    /**
     * 压缩视频缩略图：宽高压缩
     * 注：如果用户期望的长度和宽度和原图长度宽度相差太多的话，图片会很不清晰。
     *
     * @param bitmap 视频缩略图
     */
    private fun compressVideoThumbnail(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }

    override fun onItemClick(view: View?, position: Int, t: VideoInfo) {
        val intent = Intent()
        intent.setData(t.localPathUri)
        setResult(200, intent)
        finish()
    }

}