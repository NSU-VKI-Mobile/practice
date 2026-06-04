package ci.nsu.mobile.auth.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.qr.NotificationHelper
import ci.nsu.mobile.auth.qr.QrGenerator
import ci.nsu.mobile.auth.qr.QrSaver
import ci.nsu.mobile.auth.qr.SoundManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrViewModel : ViewModel() {

    var generatedQr by mutableStateOf<Bitmap?>(null)
        private set
    var showQrDialog by mutableStateOf(false)
        private set
    var savedMessage by mutableStateOf<String?>(null)
        private set

    fun generateQr(login: String, password: String) {
        val content = "$login:$password"   // формат login:password
        generatedQr = QrGenerator.generate(content)
        showQrDialog = true
        savedMessage = null
    }

    fun dismissQrDialog() {
        showQrDialog = false
    }

    fun saveQrToGallery(context: Context) {
        val bmp = generatedQr ?: return
        viewModelScope.launch {
            val ok = withContext(Dispatchers.IO) {
                QrSaver.saveToGallery(context, bmp)
            }
            savedMessage = if (ok) "Сохранено в галерею" else "Ошибка сохранения"
        }
    }

    var timerSeconds by mutableIntStateOf(30)
        private set
    var scanFinished by mutableStateOf(false)
        private set
    var scannedLogin by mutableStateOf<String?>(null)
        private set
    var scannedPassword by mutableStateOf<String?>(null)
        private set

    private var timerJob: Job? = null
    private var soundManager: SoundManager? = null

    fun initSound(context: Context) {
        if (soundManager == null) soundManager = SoundManager(context)
        NotificationHelper.createChannel(context)
    }

    fun startTimer() {
        timerSeconds = 30
        scanFinished = false
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerSeconds > 0) {
                delay(1_000)
                timerSeconds--
            }
            if (!scanFinished) onScanTimeout()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun onQrScanned(context: Context, raw: String) {
        if (scanFinished) return
        scanFinished = true
        timerJob?.cancel()

        val parts = raw.split(":")
        if (parts.size >= 2) {
            scannedLogin = parts[0]
            scannedPassword = parts.drop(1).joinToString(":")
            soundManager?.playSuccess()
            NotificationHelper.notifySuccess(context)
        } else {
            soundManager?.playFailure()
            NotificationHelper.notifyFailure(context)
        }
    }

    private fun onScanTimeout() {
        scanFinished = true
        soundManager?.playFailure()
    }

    fun resetScan() {
        timerSeconds = 30
        scanFinished = false
        scannedLogin = null
        scannedPassword = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager?.release()
    }
}