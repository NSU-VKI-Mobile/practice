package ci.nsu.mobile.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Surface as AndroidSurface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import ci.nsu.mobile.auth.qr.QrAuthPayload
import ci.nsu.mobile.auth.qr.QrCodeImageAnalyzer
import ci.nsu.mobile.auth.qr.QrFeedbackPlayer
import ci.nsu.mobile.auth.qr.QrScanNotifier
import ci.nsu.mobile.auth.qr.QrVibrator
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class QrScannerActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private val finished = AtomicBoolean(false)
    private lateinit var notifier: QrScanNotifier

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        // Notification permission is optional for the auth flow itself.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        notifier = QrScanNotifier(this).apply { ensureChannel() }
        requestNotificationPermissionIfNeeded()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (hasCameraPermission()) {
                        QrScannerScreen(
                            onPreviewReady = ::startCamera,
                            onTimeout = ::finishWithFailure,
                            onCancel = { finish() }
                        )
                    } else {
                        CameraPermissionMissingScreen(onClose = ::finishWithFailure)
                    }
                }
            }
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val rotation = previewView.display?.rotation ?: AndroidSurface.ROTATION_0
            val preview = Preview.Builder()
                .setTargetRotation(rotation)
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val analysis = ImageAnalysis.Builder()
                .setTargetRotation(rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeImageAnalyzer(::finishWithSuccess))
                }

            runCatching {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis
                )
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun finishWithSuccess(payload: QrAuthPayload) {
        if (!finished.compareAndSet(false, true)) {
            return
        }

        runOnUiThread {
            QrFeedbackPlayer.playSuccess(this)
            QrVibrator.success(this)
            notifier.showSuccess()
            setResult(
                Activity.RESULT_OK,
                Intent()
                    .putExtra(EXTRA_LOGIN, payload.login)
                    .putExtra(EXTRA_PASSWORD, payload.password)
            )
            finish()
        }
    }

    private fun finishWithFailure() {
        if (!finished.compareAndSet(false, true)) {
            return
        }

        QrFeedbackPlayer.playFailure(this)
        QrVibrator.failure(this)
        notifier.showFailure()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val EXTRA_LOGIN = "ci.nsu.mobile.auth.QR_LOGIN"
        const val EXTRA_PASSWORD = "ci.nsu.mobile.auth.QR_PASSWORD"
    }
}

@Composable
private fun QrScannerScreen(
    onPreviewReady: (PreviewView) -> Unit,
    onTimeout: () -> Unit,
    onCancel: () -> Unit
) {
    var secondsRemaining by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1_000L)
            secondsRemaining -= 1
        }
        onTimeout()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    onPreviewReady(this)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        ScannerOverlay(secondsRemaining = secondsRemaining, onCancel = onCancel)
    }
}

@Composable
private fun ScannerOverlay(secondsRemaining: Int, onCancel: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.62f))
                .padding(20.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Сканирование QR-кода авторизации",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Осталось: $secondsRemaining с",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val frameSize = size.width * 0.66f
            val left = (size.width - frameSize) / 2f
            val top = (size.height - frameSize) / 2f
            drawRect(
                color = Color.White,
                topLeft = Offset(left, top),
                size = Size(frameSize, frameSize),
                style = Stroke(width = 5f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.62f))
                .padding(20.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Наведите камеру на QR-код с данными авторизации",
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCancel) {
                Text("Отмена")
            }
        }
    }
}

@Composable
private fun CameraPermissionMissingScreen(onClose: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Для сканирования QR-кода нужен доступ к камере. Разрешение не выдано.",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            QrFeedbackPlayer.playFailure(context)
            QrVibrator.failure(context)
            onClose()
        }) {
            Text("Закрыть")
        }
    }
}
