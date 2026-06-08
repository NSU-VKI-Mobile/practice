package ci.nsu.mobile.auth.ui.screens

import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.auth.viewModels.code.CodeEvents
import ci.nsu.mobile.auth.viewModels.code.CodeViewModel
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeScreen(
    viewModel: CodeViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onEvent(CodeEvents.StartScan)
        } else {
            Toast.makeText(context, "Разрешение на камеру необходимо для сканирования", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сканирование QR-кода авторизации") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(CodeEvents.StopScan)
                        onBackClick()
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
        ) {
            if (state.isScanning) {
                Text(
                    text = "Осталось: ${state.timerSeconds} с",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                AndroidView(
                    factory = { ctx ->
                        DecoratedBarcodeView(ctx).apply {
                            val barcodeCallback = object : BarcodeCallback {
                                override fun barcodeResult(result: BarcodeResult?) {
                                    result?.text?.let { data ->
                                        viewModel.onEvent(CodeEvents.QrScanned(data))
                                    }
                                }

                                override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>?) {}
                            }
                            decodeContinuous(barcodeCallback)
                            resume()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(250.dp)
                        .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                )

                Text(
                    text = "Наведите камеру на QR-код с данными авторизации",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}