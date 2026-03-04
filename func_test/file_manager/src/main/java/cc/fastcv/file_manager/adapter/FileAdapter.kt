package cc.fastcv.file_manager.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import cc.fastcv.file_manager.R
import cc.fastcv.lib_components.BaseRecyclerViewAdapter
import cc.fastcv.lib_components.BaseViewHolder
import java.io.File

class FileAdapter : BaseRecyclerViewAdapter<File>() {

    private val files = mutableListOf<File>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(files: List<File>) {
        this.files.clear()
        this.files.addAll(files)
        notifyDataSetChanged()
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_file
    }

    override fun getDataByPosition(position: Int): File {
        return files[position]
    }

    override fun getTotalSize(): Int {
        return files.size
    }

    override fun convert(holder: BaseViewHolder, data: File, position: Int) {
        if (data.isDirectory()) {
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.ic_folder)
        } else {
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.ic_file)
        }

        holder.getView<TextView>(R.id.tv_title).text = data.getName()
        holder.getView<ImageView>(R.id.iv_state).setImageResource(getStateResource(position))
    }

    private fun getStateResource(position: Int): Int {
        val read: Boolean = files[position].canRead()
        val write: Boolean = files[position].canRead()
        return if (read && !write) {
            R.drawable.ic_only_read
        } else if (!read && write) {
            R.drawable.ic_only_write
        } else {
            R.drawable.ic_read_and_write
        }
    }
}