package ci.nsu.main.notifier.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getLongExtra("notification_id", 0).toInt()
        val title = intent.getStringExtra("title") ?: "Напоминание"
        val description = intent.getStringExtra("description") ?: ""

        NotificationHelper.showNotification(
            context = context,
            notificationId = notificationId,
            title = title,
            content = description
        )
    }
}