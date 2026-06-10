package ci.nsu.mobile.auth.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object QrCodeGenerator {

    private const val QR_SIZE = 500
    private const val QUIET_ZONE = 50

    fun generateQrCode(data: String): Bitmap? {
        return try {
            val bitMatrix = MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE,
                QR_SIZE - QUIET_ZONE * 2,
                QR_SIZE - QUIET_ZONE * 2,
                null
            )
            createBitmapWithQuietZone(bitMatrix)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createBitmapWithQuietZone(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val totalSize = width + QUIET_ZONE * 2

        val bitmap = Bitmap.createBitmap(totalSize, totalSize, Bitmap.Config.ARGB_8888)

        for (x in 0 until totalSize) {
            for (y in 0 until totalSize) {
                val matrixX = x - QUIET_ZONE
                val matrixY = y - QUIET_ZONE

                val pixelColor = if (matrixX in 0 until width &&
                    matrixY in 0 until height &&
                    bitMatrix[matrixX, matrixY]) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
                bitmap.setPixel(x, y, pixelColor)
            }
        }
        return bitmap
    }
}