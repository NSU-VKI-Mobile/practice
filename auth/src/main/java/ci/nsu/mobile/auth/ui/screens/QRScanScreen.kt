// :auth/src/main/java/ci/nsu/mobile/auth/ui/screens/QrScanScreen.kt
package ci.nsu.mobile.auth.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.auth.viewModels.code.CodeEvents
import ci.nsu.mobile.auth.viewModels.code.CodeViewModel
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScanScreen(
    viewModel: CodeViewModel,
    onBackClick: (login: String, password: String) -> Unit  // ← Изменили сигнатуру
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onEvent(CodeEvents.CameraPermissionResult(granted))
    }

    // Отслеживаем успешное сканирование
    LaunchedEffect(state.scannedData) {
        if (state.scannedData != null && state.login.isNotEmpty()) {
            // Небольшая задержка, чтобы пользователь увидел результат
            kotlinx.coroutines.delay(500)
            viewModel.onEvent(CodeEvents.StopScan)
            onBackClick(state.login, state.password)
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onEvent(CodeEvents.CameraPermissionResult(true))
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Диалог при отказе в разрешении
    if (state.showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CodeEvents.DismissPermissionDeniedDialog) },
            title = { Text("Требуется разрешение") },
            text = { Text("Для сканирования QR-кода необходимо разрешение на использование камеры") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(CodeEvents.DismissPermissionDeniedDialog)
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Разрешить")
                }
            },
            dismissButton = {
                TextButton(onClick = { onBackClick("", "") }) {
                    Text("Отмена")
                }
            }
        )
    }

    // Диалог ошибки
    state.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CodeEvents.DismissError) },
            title = { Text("Сканирование завершено") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(CodeEvents.DismissError)
                    onBackClick("", "")
                }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сканирование QR-кода") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(CodeEvents.StopScan)
                        onBackClick("", "")
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            if (state.isScanning && state.hasCameraPermission) {
                AndroidView(
                    factory = { ctx ->
                        DecoratedBarcodeView(ctx).apply {
                            val barcodeCallback = object : BarcodeCallback {
                                override fun barcodeResult(result: BarcodeResult?) {
                                    result?.text?.let { data ->
                                        viewModel.onEvent(CodeEvents.QrScanned(data))
                                    }
                                }

                                override fun possibleResultPoints(
                                    resultPoints: List<com.google.zxing.ResultPoint>?
                                ) {}
                            }
                            decodeContinuous(barcodeCallback)
                            resume()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Таймер
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                        .zIndex(1f),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Осталось: ${state.timerSeconds} с",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = if (state.timerSeconds <= 5) Color.Red else Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Рамка сканирования
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(250.dp)
                        .zIndex(1f)
                        .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                )

                // Уведомление об успешном сканировании
                if (state.scannedData != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                            .zIndex(2f),
                        color = Color(0xFF4CAF50).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "✓ QR-код распознан!",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}