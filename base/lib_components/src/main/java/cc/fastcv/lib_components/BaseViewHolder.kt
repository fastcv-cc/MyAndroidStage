package cc.fastcv.lib_components

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@SuppressWarnings("unchecked")
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews = SparseArray<View>()

    companion object {
        fun getViewHolder(parent: ViewGroup, layoutId: Int): BaseViewHolder {
            val convertView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return BaseViewHolder(convertView)
        }
    }

    fun <T : View?> getView(id: Int): T {
        var v = mViews[id]
        if (v == null) {
            v = itemView.findViewById(id)
            mViews.put(id, v)
        }
        return v as T
    }
}