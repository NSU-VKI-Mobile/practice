package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.NotificationDao
import ci.nsu.mobile.main.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepository(private val notificationDao: NotificationDao) {

    fun getAllNotifications(): Flow<List<NotificationItem>> {
        println("📦 Repository: getAllNotifications called")
        return notificationDao.getAllNotifications()
    }

    suspend fun getNotificationById(id: Long): NotificationItem? {
        println("📦 Repository: getNotificationById called for id=$id")
        return notificationDao.getNotificationById(id)
    }

    suspend fun insert(notification: NotificationItem): Long {
        println("📦 Repository: insert called - ${notification.title}")
        return notificationDao.insert(notification)
    }

    suspend fun update(notification: NotificationItem) {
        println("📦 Repository: update called - ${notification.title}")
        notificationDao.update(notification)
    }

    suspend fun delete(notification: NotificationItem) {
        println("📦 Repository: delete called - ${notification.title}")
        notificationDao.delete(notification)
    }

    suspend fun deleteById(id: Long) {
        println("📦 Repository: deleteById called for id=$id")
        notificationDao.deleteById(id)
    }

    suspend fun existsAtTimestamp(timestamp: Long): Boolean {
        return notificationDao.getCountByTimestamp(timestamp) > 0
    }

    suspend fun getActiveNotifications(currentTime: Long): List<NotificationItem> {
        println("📦 Repository: getActiveNotifications called, currentTime=$currentTime")
        return notificationDao.getActiveNotifications(currentTime)
    }
}