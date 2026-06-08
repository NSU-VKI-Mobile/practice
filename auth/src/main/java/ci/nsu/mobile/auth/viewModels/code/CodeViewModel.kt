package ci.nsu.mobile.auth.viewModels.code

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.models.QrCodeData
import ci.nsu.mobile.domain.models.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.EnumMap
import javax.inject.Inject

@HiltViewModel
class CodeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authManager: AuthManager
) : ViewModel() {
    private val _state = MutableStateFlow(CodeState())
    val state: StateFlow<CodeState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var successSound: MediaPlayer? = null
    private var failureSound: MediaPlayer? = null

    companion object {
        private const val QR_SCAN_CHANNEL_ID = "qr_scan_channel"
        private const val NOTIFICATION_ID = 1001
    }

    init {
        initSounds()
        createNotificationChannel()
    }

    fun onEvent(event: CodeEvents) {
        when (event) {
            is CodeEvents.GenerateQr -> generateQr()
            is CodeEvents.StartScan -> startScan()
            is CodeEvents.StopScan -> stopScan()
            is CodeEvents.SaveToGallery -> saveToGallery()
            is CodeEvents.DismissSaveDialog -> _state.update { it.copy(showSaveDialog = false) }
            is CodeEvents.UpdateLogin -> _state.update { it.copy(login = event.login) }
            is CodeEvents.UpdatePassword -> _state.update { it.copy(password = event.password) }
            is CodeEvents.QrScanned -> onQrScanned(event.data)
            is CodeEvents.TimerTick -> _state.update { it.copy(timerSeconds = event.seconds) }
            is CodeEvents.TimerFinished -> onTimerFinished()
        }
    }

    private fun generateQr() {
        viewModelScope.launch {
            _state.update { it.copy(isGenerating = true) }

            val user = authManager.getCurrentUser()
            if (user == null) {
                _state.update { it.copy(isGenerating = false, errorMessage = "Пользователь не авторизован") }
                return@launch
            }

            try {
                val qrData = QrCodeData(
                    login = user.login,
                    password = ""
                )
                val qrCode = generateQrCode(qrData)
                _state.update {
                    it.copy(
                        qrCode = qrCode,
                        isGenerating = false,
                        showSaveDialog = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isGenerating = false, errorMessage = "Ошибка генерации QR-кода") }
            }
        }
    }

    private fun generateQrCode(data: QrCodeData): Bitmap {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
            put(EncodeHintType.MARGIN, 2)
        }

        val qrWriter = QRCodeWriter()
        val bitMatrix = qrWriter.encode(
            "${data.login}:${data.password}",
            BarcodeFormat.QR_CODE,
            512,
            512,
            hints
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }

    private fun startScan() {
        _state.update { it.copy(isScanning = true, timerSeconds = 30, isTimerRunning = true) }
        startTimer()
    }

    private fun stopScan() {
        timerJob?.cancel()
        _state.update { it.copy(isScanning = false, isTimerRunning = false) }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 30 downTo 0) {
                if (i == 0) {
                    onEvent(CodeEvents.TimerFinished)
                    break
                }
                onEvent(CodeEvents.TimerTick(i))
                delay(1000)
            }
        }
    }

    private fun onQrScanned(data: String) {
        stopScan()
        playSound(successSound)
        showSuccessNotification()

        val parts = data.split(":")
        if (parts.size == 2) {
            _state.update {
                it.copy(
                    login = parts[0],
                    password = parts[1],
                    scannedData = data,
                    isScanning = false
                )
            }
        } else {
            _state.update { it.copy(errorMessage = "Неверный формат QR-кода") }
        }
    }

    private fun onTimerFinished() {
        stopScan()
        playSound(failureSound)
        showFailureNotification()
        _state.update { it.copy(errorMessage = "Время сканирования истекло") }
    }

    private fun saveToGallery() {
        val bitmap = _state.value.qrCode ?: return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveToMediaStore(bitmap)
            } else {
                saveToExternalStorage(bitmap)
            }
            _state.update { it.copy(showSaveDialog = false) }
        } catch (e: Exception) {
            _state.update { it.copy(errorMessage = "Ошибка сохранения: ${e.message}") }
        }
    }

    private fun saveToMediaStore(bitmap: Bitmap) {
        val contentValues = android.content.ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "QR_Code_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QR_Codes")
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
        }
    }

    private fun saveToExternalStorage(bitmap: Bitmap) {
        val directory = android.os.Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val appDirectory = java.io.File(directory, "QR_Codes")
        if (!appDirectory.exists()) appDirectory.mkdirs()

        val file = java.io.File(appDirectory, "QR_Code_${System.currentTimeMillis()}.png")
        val outputStream = java.io.FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    private fun initSounds() {
        try {
            successSound = MediaPlayer.create(context, R.raw.qr_success)
            failureSound = MediaPlayer.create(context, R.raw.qr_failure)
        } catch (e: Exception) {
            // Звуки не найдены
        }
    }

    private fun playSound(mediaPlayer: MediaPlayer?) {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                QR_SCAN_CHANNEL_ID,
                "Сканирование QR-кода",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о результатах сканирования QR-кода авторизации"
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showSuccessNotification() {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Авторизация готова")
            .setContentText("Данные из QR-кода загружены. Перейдите к авторизации.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setTimeoutAfter(5000)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun showFailureNotification() {
        val notification = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Сканирование не удалось")
            .setContentText("QR-код не распознан или время истекло.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        successSound?.release()
        failureSound?.release()
    }
}