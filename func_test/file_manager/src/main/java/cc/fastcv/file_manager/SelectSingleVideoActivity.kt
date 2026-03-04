package cc.fastcv.file_manager

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import cc.fastcv.stage.StageActivity
import java.io.File

class SelectSingleVideoActivity : StageActivity() {

    private var uri: Uri? = null

    private val captureVideo: ActivityResultContracts.CaptureVideo =
        ActivityResultContracts.CaptureVideo()

    private val openGalleryContact = StartActivityForResult()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_single_video)

        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this@SelectSingleVideoActivity,
                "$packageName.fileprovider",
                File(externalCacheDir, "video.mp4")
            )
        } else {
            Uri.fromFile(File(externalCacheDir, "video.mp4"))
        }


        val videoView = findViewById<VideoView>(R.id.vv_show)

        val videoContact = registerForActivityResult(
            captureVideo
        ) { result: Boolean ->
            if (result) {
                videoView.setVideoURI(uri)
                videoView.start()
            }
        }

        val openGalleryResult = registerForActivityResult(
            openGalleryContact
        ) { result: ActivityResult ->
            var uri: Uri? = null
            if (result.data != null) {
                uri = result.data!!.data
            }
            if (uri != null) {
                videoView.setVideoURI(uri)
                videoView.start()
            }
        }

        findViewById<View>(R.id.btCaptureVideo).setOnClickListener {
            videoContact.launch(
                uri
            )
        }

        findViewById<View>(R.id.btSystemGallery).setOnClickListener {
            openGalleryResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        findViewById<View>(R.id.btCustomGallery).setOnClickListener {
            openGalleryResult.launch(
                Intent(
                    this@SelectSingleVideoActivity,
                    CustomSingleVideoSelectActivity::class.java
                )
            )
        }

    }

}