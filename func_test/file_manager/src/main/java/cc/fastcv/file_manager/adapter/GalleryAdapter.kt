package cc.fastcv.file_manager.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.widget.ImageView
import cc.fastcv.file_manager.R
import cc.fastcv.file_manager.model.MediaStoreImage
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.lib_components.BaseViewHolder
import java.io.IOException

class GalleryAdapter : BaseRecyclerViewAdapter<MediaStoreImage>() {

    private val items = mutableListOf<MediaStoreImage>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(datas: List<MediaStoreImage>) {
        this.items.clear()
        this.items.addAll(datas)
        notifyDataSetChanged()
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_gallery_layout
    }

    override fun getDataByPosition(position: Int): MediaStoreImage {
        return items[position]
    }

    override fun getTotalSize(): Int {
        return items.size
    }

    override fun convert(holder: BaseViewHolder, data: MediaStoreImage, position: Int) {
        holder.getView<ImageView>(R.id.image).tag = data

        val thumbnail: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                holder.itemView.context.contentResolver.loadThumbnail(
                    data.contentUri!!, Size(640, 480), null
                )
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
        if (thumbnail == null) {
            holder.getView<ImageView>(R.id.image).setImageURI(null)
            holder.getView<ImageView>(R.id.image).setImageURI(data.contentUri)
        } else {
            holder.getView<ImageView>(R.id.image).setImageBitmap(thumbnail)
        }
    }
}