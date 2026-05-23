package com.example.auth.util

import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.NotFoundException
import com.google.zxing.common.HybridBinarizer

class QRCodeAnalyzer(
    private val onQrCodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val reader = MultiFormatReader()
    private var lastScanTime = 0L
    private val SCAN_DELAY_MS = 500L

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastScanTime < SCAN_DELAY_MS) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val result = decodeQRCode(mediaImage, imageProxy)
        if (result != null) {
            lastScanTime = currentTime
            onQrCodeDetected(result)
        }

        imageProxy.close()
    }

    private fun decodeQRCode(image: Image, imageProxy: ImageProxy): String? {
        try {
            val planes = image.planes
            val yPlane = planes[0]
            val uPlane = planes[1]
            val vPlane = planes[2]

            val yBuffer = yPlane.buffer
            val uBuffer = uPlane.buffer
            val vBuffer = vPlane.buffer
            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()

            val nv21 = ByteArray(ySize + uSize + vSize)
            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)

            val source = PlanarYUVLuminanceSource(
                nv21,
                imageProxy.width,
                imageProxy.height,
                0,
                0,
                imageProxy.width,
                imageProxy.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = reader.decode(binaryBitmap)

            return result.text
        } catch (e: NotFoundException) {
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}