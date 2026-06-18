package ci.nsu.main.notifier.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import ci.nsu.main.notifier.data.model.NotificationItem
import ci.nsu.main.notifier.data.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationScheduler(
    private val context: Context,
    private val repository: NotificationRepository
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(notification: NotificationItem) {
        if (!notification.isEnabled) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("notification_id", notification.id)
            putExtra("title", notification.title)
            putExtra("description", notification.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, notification.timestamp, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, notification.timestamp, pendingIntent)
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notification.timestamp, pendingIntent)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun cancelNotification(notificationId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun rescheduleAllNotifications() {
        CoroutineScope(Dispatchers.IO).launch {
            val currentTime = System.currentTimeMillis()
            val activeNotifications = repository.getActiveNotifications(currentTime)
            activeNotifications.forEach { notification ->
                scheduleNotification(notification)
            }
        }
    }
}
