package com.example.auth.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.R
import com.example.auth.util.QRCodeAnalyzer
import com.example.auth.vm.LoginAndRegViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay

private const val QR_SCAN_CHANNEL_ID = "qr_scan_channel"
@Composable
fun ScannerScreen(
    onBack : () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            QR_SCAN_CHANNEL_ID,
            "Сканирование QR-кода",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Уведомления о результатах сканирования QR-кода авторизации"
        }
        notificationManager.createNotificationChannel(channel)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    if (hasCameraPermission) {
        ScannerPreview(onBack,viewModel)
    }
    else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.why_need_camera_access)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                Text(stringResource(R.string.get_camera_access))
            }
            Button(onClick = onBack) {
                Text(stringResource(R.string.text_back))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScannerPreview(
    onBack : () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
){
    val context = LocalContext.current
    var curSec by remember { mutableIntStateOf(30) }
    var isScanRun by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (isScanRun && curSec > 0) {
            delay(1000)
            curSec--
        }
        if (curSec == 0 && isScanRun) {
            showFailureNotification(context)
            viewModel.failureSound.start()
            onBack()
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.scan_qr_code))
                Text(stringResource(R.string.left) + ": " + curSec + stringResource(R.string.short_seconds))
            }
        } ) {innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(innerPadding)
        )
        {
            CameraPreview { str ->
                isScanRun = false
                viewModel.setLoginAndPasswordWithQrCode(str)
                showSuccessNotification(context)
                viewModel.successSound.start()
                onBack()
            }
            Canvas(
                modifier = Modifier.size(175.dp, 175.dp)
            ) {
                drawRect(
                    color = Color.White,
                    style = Stroke(width = 4.dp.toPx()),
                    size = size,
                    topLeft = Offset.Zero
                )
            }
        }
    }
}


@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraPreview(
    onQrCodeDetected: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = surfaceProvider
                    }
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        QRCodeAnalyzer(onQrCodeDetected)
                    )

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                }, ContextCompat.getMainExecutor(ctx))
            }
        },
    )
}

private fun showSuccessNotification(
    context: Context
) {
    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    val builder = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_menu_save)
        .setContentTitle("Авторизация готова")
        .setContentText("Данные из QR-кода загружены. Перейдите к авторизации.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.setTimeoutAfter(5000L)
    }

    notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
}

private fun showFailureNotification(
    context: Context
) {
    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    val builder = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_menu_close_clear_cancel)
        .setContentTitle("Сканирование не удалось")
        .setContentText("QR-код не распознан или время истекло.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
}
