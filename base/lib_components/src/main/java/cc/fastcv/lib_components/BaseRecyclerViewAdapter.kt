package cc.fastcv.lib_components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    private var onItemClickListener: OnItemClickListener<T>? = null
    private var onItemLongClickListener: OnItemLongClickListener<T>? = null

    abstract fun getLayoutId(viewType: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.Companion.getViewHolder(parent, getLayoutId(viewType))
    }

    protected abstract fun getDataByPosition(position: Int): T

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(
                    holder.itemView,
                    position,
                    getDataByPosition(position)
                )
            }
        }
        holder.itemView.setOnLongClickListener {
            if (onItemLongClickListener != null) {
                onItemLongClickListener!!.onItemLongClick(
                    holder.itemView,
                    position,
                    getDataByPosition(position)
                )
            }
            true
        }
        convert(holder, getDataByPosition(position), position)
    }

    override fun getItemCount(): Int {
        return getTotalSize()
    }

    abstract fun getTotalSize(): Int

    /**
     * 设置Item的点击事件
     */
    open fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        onItemClickListener = listener
    }

    /**
     * 设置Item的长按事件
     */
    open fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    abstract fun convert(holder: BaseViewHolder, data: T, position: Int)

    interface OnItemClickListener<S> {
        fun onItemClick(view: View?, position: Int, t: S)
    }

    interface OnItemLongClickListener<S> {
        fun onItemLongClick(view: View?, position: Int, t: S)
    }
}