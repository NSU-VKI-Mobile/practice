package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.repository.NotificationRepository
import ci.nsu.mobile.main.notification.NotificationScheduler

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