package cc.fastcv.exquisite_segdiagram.adapter

import cc.fastcv.exquisite_segdiagram.SegDiagramInfo

abstract class TipAdapter {
    abstract fun getTopText(position: Int, info: SegDiagramInfo): CharSequence
    abstract fun getBottomText(position: Int, info: SegDiagramInfo): CharSequence

    /**
     * 返回Tip背景颜色，返回0则使用默认背景
     */
    open fun getTipBackgroundColor(position: Int, info: SegDiagramInfo): Int = 0
}