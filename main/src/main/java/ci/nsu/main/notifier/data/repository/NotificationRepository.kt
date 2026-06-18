package ci.nsu.main.notifier.data.repository

import ci.nsu.main.notifier.data.database.NotificationDao
import ci.nsu.main.notifier.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {

    fun getAllNotifications(): Flow<List<NotificationItem>> =
        notificationDao.getAllNotifications()

    suspend fun getNotificationById(id: Long): NotificationItem? =
        notificationDao.getNotificationById(id)

    suspend fun insert(notification: NotificationItem): Long =
        notificationDao.insert(notification)

    suspend fun update(notification: NotificationItem) =
        notificationDao.update(notification)

    suspend fun delete(notification: NotificationItem) =
        notificationDao.delete(notification)

    suspend fun deleteById(id: Long) =
        notificationDao.deleteById(id)

    suspend fun existsAtTimestamp(timestamp: Long): Boolean =
        notificationDao.getCountByTimestamp(timestamp) > 0

    suspend fun getActiveNotifications(currentTime: Long): List<NotificationItem> =
        notificationDao.getActiveNotifications(currentTime)
}