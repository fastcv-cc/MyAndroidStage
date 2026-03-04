package cc.fastcv.file_manager.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.widget.ImageView
import cc.fastcv.file_manager.R
import cc.fastcv.file_manager.model.VideoInfo
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.lib_components.BaseViewHolder
import java.io.IOException

class VideoAdapter : BaseRecyclerViewAdapter<VideoInfo>() {

    private val items = mutableListOf<VideoInfo>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(datas: List<VideoInfo>) {
        this.items.clear()
        this.items.addAll(datas)
        notifyDataSetChanged()
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_video_layout
    }

    override fun getDataByPosition(position: Int): VideoInfo {
        return items[position]
    }

    override fun getTotalSize(): Int {
        return items.size
    }

    override fun convert(holder: BaseViewHolder, data: VideoInfo, position: Int) {
        holder.getView<ImageView>(R.id.image).tag = data

        val thumbnail: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                holder.itemView.context.contentResolver.loadThumbnail(
                    data.localPathUri!!, Size(640, 480), null
                )
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }

        if (thumbnail != null) {
            holder.getView<ImageView>(R.id.image).setImageBitmap(thumbnail)
        }
    }
}