package ci.nsu.mobile.auth.qr

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ci.nsu.mobile.auth.R

class QrScanNotifier(private val context: Context) {
    private val notificationManager = NotificationManagerCompat.from(context)

    fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            QR_SCAN_CHANNEL_ID,
            "Сканирование QR-кода",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Уведомления о результатах сканирования QR-кода авторизации"
        }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun showSuccess() {
        showNotification(
            id = SUCCESS_NOTIFICATION_ID,
            icon = R.drawable.ic_qr_success,
            title = "Авторизация готова",
            text = "Данные из QR-кода загружены. Перейдите к авторизации.",
            timeoutAfterMs = 5_000L
        )
    }

    fun showFailure() {
        showNotification(
            id = FAILURE_NOTIFICATION_ID,
            icon = R.drawable.ic_qr_failure,
            title = "Сканирование не удалось",
            text = "QR-код не распознан или время истекло.",
            timeoutAfterMs = 0L
        )
    }

    private fun showNotification(
        id: Int,
        icon: Int,
        title: String,
        text: String,
        timeoutAfterMs: Long
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, QR_SCAN_CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .apply {
                if (timeoutAfterMs > 0L) {
                    setTimeoutAfter(timeoutAfterMs)
                }
            }
            .build()

        notificationManager.notify(id, notification)
    }

    companion object {
        const val QR_SCAN_CHANNEL_ID = "qr_scan_channel"
        private const val SUCCESS_NOTIFICATION_ID = 9001
        private const val FAILURE_NOTIFICATION_ID = 9002
    }
}
