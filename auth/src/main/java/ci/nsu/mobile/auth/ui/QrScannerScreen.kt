package ci.nsu.mobile.auth.ui

import android.graphics.Bitmap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import java.nio.ByteBuffer
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onQrScanned: (login: String, password: String) -> Unit,
    onTimeout: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var timeLeft by remember { mutableStateOf(30) }
    var scannerEnabled by remember { mutableStateOf(true) }
    var isScanning by remember { mutableStateOf(false) }
    var isCameraReady by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Таймер
    LaunchedEffect(Unit) {
        while (timeLeft > 0 && scannerEnabled) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0 && scannerEnabled) {
            scannerEnabled = false
            onTimeout()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сканирование QR-кода") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ComposeColor.Black.copy(alpha = 0.8f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (errorMessage != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onClose) {
                        Text("Закрыть")
                    }
                }
            } else if (!isCameraReady) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Инициализация камеры...")
                    }
                }
            } else {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    lifecycleOwner = lifecycleOwner,
                    onQrScanned = { data ->
                        if (scannerEnabled && !isScanning) {
                            isScanning = true
                            val parts = data.split(":")
                            if (parts.size == 2) {
                                onQrScanned(parts[0], parts[1])
                            }
                        }
                    },
                    isEnabled = scannerEnabled,
                    onCameraReady = { isCameraReady = true },
                    onError = { errorMessage = it }
                )

                ScannerOverlay(timeLeft = timeLeft)
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onQrScanned: (String) -> Unit,
    isEnabled: Boolean,
    onCameraReady: () -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    LaunchedEffect(Unit) {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build()

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        if (isEnabled) {
                            val bitmap = imageProxyToBitmap(imageProxy)
                            if (bitmap != null) {
                                val inputImage = InputImage.fromBitmap(bitmap, 0)

                                val options = BarcodeScannerOptions.Builder()
                                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                    .build()

                                val scanner = BarcodeScanning.getClient(options)

                                scanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            val rawValue = barcode.rawValue
                                            if (!rawValue.isNullOrEmpty()) {
                                                onQrScanned(rawValue)
                                                scanner.close()
                                                break
                                            }
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                        bitmap.recycle()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        } else {
                            imageProxy.close()
                        }
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                    previewView?.let {
                        preview.setSurfaceProvider(it.surfaceProvider)
                    }

                    onCameraReady()

                } catch (e: Exception) {
                    e.printStackTrace()
                    onError("Ошибка камеры: ${e.message}")
                }
            }, ContextCompat.getMainExecutor(context))
        } catch (e: Exception) {
            e.printStackTrace()
            onError("Ошибка инициализации: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                previewView = this
                this.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = modifier
    )
}

private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
    try {
        val buffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val bitmap = Bitmap.createBitmap(
            imageProxy.width,
            imageProxy.height,
            Bitmap.Config.ARGB_8888
        )

        val byteBuffer = ByteBuffer.wrap(bytes)
        byteBuffer.rewind()
        bitmap.copyPixelsFromBuffer(byteBuffer)

        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

@Composable
fun ScannerOverlay(timeLeft: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = ComposeColor.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Осталось: ${timeLeft}с",
                color = ComposeColor.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, ComposeColor.Green, shape = RoundedCornerShape(16.dp))
            )

            CornerMarker(modifier = Modifier.align(Alignment.TopStart))
            CornerMarker(modifier = Modifier.align(Alignment.TopEnd))
            CornerMarker(modifier = Modifier.align(Alignment.BottomStart))
            CornerMarker(modifier = Modifier.align(Alignment.BottomEnd))
        }

        Surface(
            color = ComposeColor.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Text(
                text = "Наведите камеру на QR-код",
                color = ComposeColor.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun CornerMarker(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(30.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(3.dp)
                .background(ComposeColor.Green)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight(0.5f)
                .background(ComposeColor.Green)
                .align(Alignment.TopStart)
        )
    }
}