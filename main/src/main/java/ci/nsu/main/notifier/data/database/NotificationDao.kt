package ci.nsu.main.notifier.data.database

import androidx.room.*
import ci.nsu.main.notifier.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY timestamp ASC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Long): NotificationItem?

    @Query("SELECT COUNT(*) FROM notifications WHERE timestamp = :timestamp")
    suspend fun getCountByTimestamp(timestamp: Long): Int

    @Insert
    suspend fun insert(notification: NotificationItem): Long

    @Update
    suspend fun update(notification: NotificationItem)

    @Delete
    suspend fun delete(notification: NotificationItem)

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM notifications WHERE timestamp > :currentTime AND isEnabled = 1")
    suspend fun getActiveNotifications(currentTime: Long): List<NotificationItem>
}