package ci.nsu.mobile.auth.ui.screens

import android.Manifest
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import ci.nsu.mobile.auth.qr.QrCodeAnalyzer
import ci.nsu.mobile.auth.ui.QrViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrScanScreen(
    vm: QrViewModel, onScanned: (login: String, password: String) -> Unit, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    // Звук и уведомления
    LaunchedEffect(Unit) {
        vm.initSound(context)
        vm.startTimer()
    }

    // Наблюдение за результатом сканирования
    LaunchedEffect(vm.scannedLogin, vm.scannedPassword) {
        val login = vm.scannedLogin
        val pass = vm.scannedPassword
        if (login != null && pass != null) {
            onScanned(login, pass)
        }
    }

    // Таймер истек
    LaunchedEffect(vm.timerSeconds) {
        if (vm.timerSeconds == 0 && !vm.scanFinished) {
            onDismiss()
        }
    }

    DisposableEffect(Unit) {
        onDispose { vm.stopTimer() }
    }

    when {
        cameraPermission.status.isGranted -> {
            CameraPreviewContent(vm = vm, context = context)
        }

        cameraPermission.status.shouldShowRationale -> {
            PermissionRationaleContent(
                onRequest = { cameraPermission.launchPermissionRequest() }, onDismiss = onDismiss
            )
        }

        else -> {
            LaunchedEffect(Unit) { cameraPermission.launchPermissionRequest() }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun CameraPreviewContent(vm: QrViewModel, context: Context) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(Modifier.fillMaxSize().background(Color.Black)) {

        // Превью камеры
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = Executors.newSingleThreadExecutor()
                val future = ProcessCameraProvider.getInstance(ctx)

                future.addListener({
                    val provider = future.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val analysis =
                        ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                            .also { ia ->
                                ia.setAnalyzer(executor, QrCodeAnalyzer { raw ->
                                    Handler(Looper.getMainLooper()).post {
                                        vm.onQrScanned(context, raw)
                                    }
                                })
                            }

                    runCatching {
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis
                        )
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }, modifier = Modifier.fillMaxSize()
        )

        ScannerOverlay()

        Column(
            Modifier.align(Alignment.TopCenter).padding(top = 24.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp), color = Color.Black.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "Осталось: ${vm.timerSeconds} с",
                    color = if (vm.timerSeconds <= 10) Color.Red else Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Text(
            text = "Наведите камеру на QR-код\nс данными авторизации",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 48.dp).background(
                Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp)
            ).padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun ScannerOverlay() {
    Canvas(
        Modifier.fillMaxSize().graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }) {
        val frameSize = size.minDimension * 0.6f
        val left = (size.width - frameSize) / 2f
        val top = (size.height - frameSize) / 2f
        val stroke = 4.dp.toPx()
        val corner = 20.dp.toPx()
        drawRect(Color.Black.copy(alpha = 0.55f))

        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(frameSize, frameSize),
            cornerRadius = CornerRadius(corner),
            blendMode = androidx.compose.ui.graphics.BlendMode.Clear
        )
        drawRoundRect(
            color = Color(0xFF4CAF50),
            topLeft = Offset(left, top),
            size = Size(frameSize, frameSize),
            cornerRadius = CornerRadius(corner),
            style = Stroke(width = stroke)
        )
    }
}

@Composable
private fun PermissionRationaleContent(onRequest: () -> Unit, onDismiss: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Для сканирования QR-кода необходим доступ к камере!!!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRequest, modifier = Modifier.fillMaxWidth()) {
            Text("Предоставить доступ")
        }
        OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("Отмена")
        }
    }
}