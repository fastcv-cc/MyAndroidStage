package cc.fastcv.exquisite_segdiagram.adapter

import cc.fastcv.exquisite_segdiagram.SegDiagramInfo

abstract class TipAdapter {
    abstract fun getTopText(position: Int, info: SegDiagramInfo): CharSequence
    abstract fun getBottomText(position: Int, info: SegDiagramInfo): CharSequence
}