package ci.nsu.mobile.auth.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import ci.nsu.mobile.auth.R

object NotificationHelper {

    const val QR_SCAN_CHANNEL_ID = "qr_scan_channel"
    private const val QR_SCAN_CHANNEL_NAME = "Сканирование QR-кода"
    private const val QR_SCAN_NOTIFICATION_ID = 1001

    private var successSound: MediaPlayer? = null
    private var failureSound: MediaPlayer? = null

    fun initSounds(context: Context) {
        // Используем системные звуки вместо кастомных файлов
        successSound = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI)
        failureSound = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI)
    }

    fun playSuccessSound() {
        try {
            successSound?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playFailureSound() {
        try {
            failureSound?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun releaseSounds() {
        successSound?.release()
        failureSound?.release()
        successSound = null
        failureSound = null
    }

    fun showSuccessNotification(context: Context) {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Авторизация готова")
            .setContentText("Данные из QR-кода загружены. Перейдите к авторизации.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(QR_SCAN_NOTIFICATION_ID, notification)
    }

    fun showFailureNotification(context: Context, message: String = "QR-код не распознан или время истекло.") {
        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Сканирование не удалось")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(QR_SCAN_NOTIFICATION_ID + 1, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                QR_SCAN_CHANNEL_ID,
                QR_SCAN_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о результатах сканирования QR-кода авторизации"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}