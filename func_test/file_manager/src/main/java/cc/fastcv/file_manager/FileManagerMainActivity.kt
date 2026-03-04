package cc.fastcv.file_manager

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AlertDialog
import cc.fastcv.stage.StageActivity

class FileManagerMainActivity : StageActivity() {

    private var resultCallback =
        ActivityResultCallback { result: Map<String, Boolean> ->
            if (!result.containsValue(false)) {
                FileManagerActivity.intoActivity(
                    this,
                    Environment.getExternalStorageDirectory().absolutePath
                )
            }
        }

    private var resultCallback4 =
        ActivityResultCallback { result: Uri? ->
            Log.d(
                "MainActivity",
                ":result = $result"
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p1_main)

        val intentLauncher =
            registerForActivityResult(RequestMultiplePermissions(), resultCallback)
        val intentLauncher4 = registerForActivityResult(OpenDocument(), resultCallback4)

        findViewById<View>(R.id.bt_internal_storage).setOnClickListener {
            filesDir.getParent()?.let { FileManagerActivity.Companion.intoActivity(this, it) }
        }

        findViewById<View>(R.id.bt_external_private_storage).setOnClickListener {
            getExternalFilesDir("")!!.getParent()?.let {
                FileManagerActivity.Companion.intoActivity(
                    this,
                    it
                )
            }
        }

        findViewById<View>(R.id.bt_external_public_storage).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val builder =
                        AlertDialog.Builder(this)
                            .setMessage("本程序需要您同意允许访问所有文件权限")
                            .setPositiveButton(
                                "确定"
                            ) { _: DialogInterface?, _: Int ->
                                startActivity(
                                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                                )
                            }.create()
                    builder.show()
                } else {
                    FileManagerActivity.Companion.intoActivity(
                        this,
                        Environment.getExternalStorageDirectory().absolutePath
                    )
                }
            } else {
                val builder =
                    AlertDialog.Builder(this)
                        .setMessage("本程序需要您同意允许读写文件权限").setPositiveButton(
                            "确定"
                        ) { _: DialogInterface?, _: Int ->
                            intentLauncher.launch(
                                arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            )
                        }.create()
                builder.show()
            }
        }

        findViewById<View>(R.id.bt_saf).setOnClickListener {
            intentLauncher4.launch(
                arrayOf("*")
            )
        }

        findViewById<View>(R.id.bt_select_single_image).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SelectSingleImageActivity::class.java
                )
            )
        }

        findViewById<View>(R.id.bt_select_single_video).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SelectSingleVideoActivity::class.java
                )
            )
        }


    }

}