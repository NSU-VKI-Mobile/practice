package ci.nsu.mobile.auth.qr

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import ci.nsu.mobile.auth.R

object NotificationHelper {

    private const val CHANNEL_ID  = "qr_scan_channel"
    private const val NOTIF_ID_OK  = 1001
    private const val NOTIF_ID_ERR = 1002

    fun createChannel(context: Context) {
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                "Сканирование QR-кода",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомле ния о результатах сканирования QR-кода авторизации"
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun notifySuccess(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Авторизация готова")
            .setContentText("Данные из QR-кода загружены!!! Перейдите к авторизации!")
            .setAutoCancel(true)
            .build()
        nm.notify(NOTIF_ID_OK, notification)
        Handler(Looper.getMainLooper()).postDelayed(
            { nm.cancel(NOTIF_ID_OK) }, 5_000L
        )
    }

    fun notifyFailure(context: Context) {
        val nm = context.getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Сканирование не удалось")
            .setContentText("QR-код не распознан или время истекло...")
            .setAutoCancel(true)
            .build()
        nm.notify(NOTIF_ID_ERR, notification)
    }
}