package com.example.auth.util

import android.graphics.Bitmap
import android.graphics.Canvas
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.createBitmap

fun CreateQrCode(data: String, width: Int = 512, height: Int = 512): Bitmap {
    val bitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return addQuietZone(bitmap)
}

fun addQuietZone(
    original: Bitmap
): Bitmap {
    val paddingPx = 4
    val newWidth = original.width + 2 * paddingPx
    val newHeight = original.height + 2 * paddingPx
    val config = original.config ?: Bitmap.Config.ARGB_8888
    val newBitmap = createBitmap(newWidth, newHeight, config)
    val canvas = Canvas(newBitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    canvas.drawBitmap(original, paddingPx.toFloat(), paddingPx.toFloat(), null)
    return newBitmap
}