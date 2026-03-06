package cc.fastcv.api_adapter.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtil {

    /**
     * 将位图[bitmap] 根据格式[format] 转换成 [ByteArray]
     * > [format] 参见 [Bitmap.CompressFormat]
     */
    fun bitmapToBytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(format, 100, baos)
        return baos.toByteArray()
    }

    /**
     * 将Byte数组[bytes] 转换成 [Bitmap]
     */
    fun bytesToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * 将 [Drawable] 转换成 [Bitmap]
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        @Suppress("DEPRECATION")
        val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1, 1,
                if (drawable.opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
            )
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE)
                    Bitmap.Config.ARGB_8888
                else
                    Bitmap.Config.RGB_565
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /** 创建位图 */
    fun convertViewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun convertViewToPNG(context: Context, view: View, callback:((path:String) -> Unit)) {
        val targetFile = File(context.cacheDir , "${System.currentTimeMillis()}.png"
        )
        if (!targetFile.exists()) {
            targetFile.createNewFile()
        }

        try {
            val bos = BufferedOutputStream(FileOutputStream(targetFile))
            convertViewToBitmap(view).compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
            callback(targetFile.absolutePath)
        } catch (e: IOException) {
            callback("")
            e.printStackTrace()
        }
    }

}