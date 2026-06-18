package ci.nsu.main.notifier

import android.app.Application
import ci.nsu.main.notifier.data.database.AppDatabase
import ci.nsu.main.notifier.data.repository.NotificationRepository
import ci.nsu.main.notifier.notification.NotificationScheduler

class NotifierApplication : Application() {

    lateinit var repository: NotificationRepository
        private set

    lateinit var notificationScheduler: NotificationScheduler
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        val database = AppDatabase.getDatabase(this)
        repository = NotificationRepository(database.notificationDao())
        notificationScheduler = NotificationScheduler(this, repository)

        notificationScheduler.rescheduleAllNotifications()
    }

    companion object {
        private lateinit var instance: NotifierApplication

        fun getInstance(): NotifierApplication = instance
    }
}