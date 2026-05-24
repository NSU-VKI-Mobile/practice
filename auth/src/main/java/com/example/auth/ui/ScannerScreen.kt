package com.example.auth.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
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
fun ScannerPreview(
    onBack : () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
){
    var curSec by remember { mutableIntStateOf(30) }
    var isScanRun by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        while (isScanRun && curSec > 0) {
            delay(1000)
            curSec--
        }
        if (curSec == 0 && isScanRun) {
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
fun CameraPreview(
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
