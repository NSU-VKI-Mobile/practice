package ci.nsu.mobile.auth.qr

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

class QrCodeAnalyzer(private val onResult: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val reader = MultiFormatReader()
    private var lastScanned = 0L
    private val scanInterval = 500L

    override fun analyze(image: ImageProxy) {
        val now = System.currentTimeMillis()
        if (now - lastScanned < scanInterval) {
            image.close(); return
        }
        lastScanned = now

        val buffer: ByteBuffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining()).also { buffer.get(it) }

        val source = PlanarYUVLuminanceSource(
            bytes,
            image.width, image.height,
            0, 0,
            image.width, image.height,
            false
        )
        runCatching {
            reader.decode(BinaryBitmap(HybridBinarizer(source)))
        }.onSuccess { result ->
            onResult(result.text)
        }

        image.close()
    }
}