package ci.nsu.mobile.auth.qr

import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.atomic.AtomicBoolean

class QrCodeImageAnalyzer(
    private val onPayloadDetected: (QrAuthPayload) -> Unit
) : ImageAnalysis.Analyzer {
    private val reader = MultiFormatReader().apply {
        setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)))
    }
    private val handled = AtomicBoolean(false)

    override fun analyze(imageProxy: ImageProxy) {
        if (handled.get()) {
            imageProxy.close()
            return
        }

        val image = imageProxy.image
        if (image == null) {
            imageProxy.close()
            return
        }

        val source = PlanarYUVLuminanceSource(
            image.toLumaByteArray(),
            image.width,
            image.height,
            0,
            0,
            image.width,
            image.height,
            false
        )

        val rawValue = decode(source)

        val payload = rawValue?.let(QrAuthPayload::fromRaw)
        if (payload != null && handled.compareAndSet(false, true)) {
            onPayloadDetected(payload)
        }

        imageProxy.close()
    }

    private fun decode(source: LuminanceSource): String? {
        return try {
            reader.decodeWithState(BinaryBitmap(HybridBinarizer(source))).text
        } catch (_: NotFoundException) {
            null
        } finally {
            reader.reset()
        }
    }

    private fun Image.toLumaByteArray(): ByteArray {
        val yPlane = planes[0]
        val buffer = yPlane.buffer
        val rowStride = yPlane.rowStride
        val pixelStride = yPlane.pixelStride
        val output = ByteArray(width * height)

        if (pixelStride == 1 && rowStride == width) {
            buffer.get(output, 0, output.size)
            return output
        }

        val row = ByteArray(rowStride)
        var outputOffset = 0
        for (rowIndex in 0 until height) {
            buffer.position(rowIndex * rowStride)
            val bytesToRead = minOf(rowStride, buffer.remaining())
            buffer.get(row, 0, bytesToRead)

            var inputOffset = 0
            for (columnIndex in 0 until width) {
                output[outputOffset++] = row[inputOffset]
                inputOffset += pixelStride
            }
        }

        return output
    }
}
