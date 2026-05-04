package ci.nsu.mobile.auth.qr

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

object QrImageSaver {
    fun saveToGallery(context: Context, bitmap: Bitmap): Result<Uri> {
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveWithMediaStore(context, bitmap)
            } else {
                saveToPublicPictures(context, bitmap)
            }
        }
    }

    private fun saveWithMediaStore(context: Context, bitmap: Bitmap): Uri {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName())
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DepositAuth")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = requireNotNull(
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        ) { "Не удалось создать запись в галерее" }

        resolver.openOutputStream(uri).use { stream ->
            requireNotNull(stream) { "Не удалось открыть поток записи" }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
        return uri
    }

    @Suppress("DEPRECATION")
    private fun saveToPublicPictures(context: Context, bitmap: Bitmap): Uri {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "DepositAuth"
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName())
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf("image/png"), null)
        return Uri.fromFile(file)
    }

    private fun fileName(): String = "auth_qr_${System.currentTimeMillis()}.png"
}
