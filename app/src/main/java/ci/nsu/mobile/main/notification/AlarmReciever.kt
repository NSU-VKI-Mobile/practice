package ci.nsu.mobile.main.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("⏰ AlarmReceiver: onReceive called!")

        val notificationId = intent.getLongExtra("notification_id", 0).toInt()
        val title = intent.getStringExtra("title") ?: "Напоминание"
        val description = intent.getStringExtra("description") ?: ""

        println("⏰ Notification ID: $notificationId, Title: $title")

        NotificationHelper.showNotification(
            context = context,
            notificationId = notificationId,
            title = title,
            content = description
        )
    }
}