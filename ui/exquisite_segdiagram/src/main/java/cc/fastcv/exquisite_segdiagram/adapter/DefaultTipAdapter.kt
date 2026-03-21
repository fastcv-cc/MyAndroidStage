package cc.fastcv.exquisite_segdiagram.adapter

import cc.fastcv.exquisite_segdiagram.SegDiagramInfo

internal class DefaultTipAdapter : TipAdapter() {
    override fun getTopText(position: Int, info: SegDiagramInfo): CharSequence {
        return "index:$position"
    }

    override fun getBottomText(position: Int, info: SegDiagramInfo): CharSequence {
        return "${info.value.min} - ${info.value.max}"
    }
}