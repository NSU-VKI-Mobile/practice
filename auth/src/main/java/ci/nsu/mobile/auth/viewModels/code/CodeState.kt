package ci.nsu.mobile.auth.viewModels.code

import android.graphics.Bitmap

data class CodeState(
    val qrCode: Bitmap? = null,
    val isGenerating: Boolean = false,
    val isScanning: Boolean = false,
    val scannedData: String? = null,
    val login: String = "",
    val password: String = "",
    val timerSeconds: Int = 30,
    val isTimerRunning: Boolean = false,
    val errorMessage: String? = null,
    val showSaveDialog: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val showPermissionDeniedDialog: Boolean = false
)

sealed class CodeEvents {
    object GenerateQr : CodeEvents()
    object StartScan : CodeEvents()
    object StopScan : CodeEvents()
    object SaveToGallery : CodeEvents()
    object DismissSaveDialog : CodeEvents()
    object DismissError : CodeEvents()
    object DismissPermissionDeniedDialog : CodeEvents()
    data class UpdateLogin(val login: String) : CodeEvents()
    data class UpdatePassword(val password: String) : CodeEvents()
    data class QrScanned(val data: String) : CodeEvents()
    data class TimerTick(val seconds: Int) : CodeEvents()
    object TimerFinished : CodeEvents()
    object RequestCameraPermission : CodeEvents()
    data class CameraPermissionResult(val granted: Boolean) : CodeEvents()
}

