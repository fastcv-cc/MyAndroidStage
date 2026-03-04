package cc.fastcv.file_manager

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.core.content.FileProvider
import cc.fastcv.stage.StageActivity
import java.io.File

class SelectSingleImageActivity : StageActivity() {

    private lateinit var ivShow: ImageView

    private val takePicture = TakePicture()

    private val openGalleryContact = StartActivityForResult()

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_single_image)

        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this@SelectSingleImageActivity,
                "$packageName.fileprovider",
                File(externalCacheDir, "avatar.png")
            )
        } else {
            Uri.fromFile(File(externalCacheDir, "avatar.png"))
        }

        ivShow = findViewById(R.id.iv_show)

        val cameraContact = registerForActivityResult(takePicture) { result: Boolean ->
            if (result) {
                ivShow.setImageURI(null)
                ivShow.setImageURI(uri)
            }
        }

        val openGalleryResult = registerForActivityResult(openGalleryContact
        ) { result: ActivityResult ->
            var uri: Uri? = null
            if (result.data != null) {
                uri = result.data!!.data
            }
            if (uri != null) {
                ivShow.setImageURI(null)
                ivShow.setImageURI(uri)
            }
        }

        findViewById<View>(R.id.btPhotograph).setOnClickListener {
            cameraContact.launch(
                uri
            )
        }

        findViewById<View>(R.id.btSystemGallery).setOnClickListener {
            openGalleryResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        findViewById<View>(R.id.btCustomGallery).setOnClickListener {
            openGalleryResult.launch(
                Intent(
                    this@SelectSingleImageActivity,
                    CustomSingleImageSelectActivity::class.java
                )
            )
        }

    }

}