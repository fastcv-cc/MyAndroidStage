package cc.fastcv.file_manager.model

import android.graphics.Bitmap

class VideoInfo : MediaInfo() {

    // 视频第一帧图
    var firstFrame: Bitmap? = null

    // 视频长度 ms
    var duration = 0L

    // 视频码率 bps
    var biteRate = 0L

    /* --------not necessary, maybe not value---- */ // 视频添加时间
    var addTime = 0L

    // 视频方向
    var videoRotation = 0

}
