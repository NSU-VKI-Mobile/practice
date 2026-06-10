package ci.nsu.mobile.calculations.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object ImageSaver {

    suspend fun saveToGallery(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveUsingMediaStore(context, bitmap, fileName)
            } else {
                saveUsingExternalStorage(context, bitmap, fileName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun saveUsingMediaStore(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/QRCodes")
        }

        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: return false

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return true
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun saveUsingExternalStorage(context: Context, bitmap: Bitmap, fileName: String): Boolean {
        val picturesDir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES)
        val qrDir = File(picturesDir, "QRCodes")

        if (!qrDir.exists()) {
            qrDir.mkdirs()
        }

        val file = File(qrDir, fileName)
        return FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            true
        }
    }
}