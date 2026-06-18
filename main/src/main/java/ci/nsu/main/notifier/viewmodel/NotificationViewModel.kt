package ci.nsu.main.notifier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.main.notifier.NotifierApplication
import ci.nsu.main.notifier.data.model.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repository = NotifierApplication.getInstance().repository
    private val notificationScheduler = NotifierApplication.getInstance().notificationScheduler

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllNotifications().collect { notifications ->
                    _notifications.value = notifications
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun checkExistsAtTimestamp(timestamp: Long): Boolean {
        return repository.existsAtTimestamp(timestamp)
    }

    fun addNotification(notification: NotificationItem, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val id = repository.insert(notification)
                val newNotification = notification.copy(id = id)
                if (newNotification.isEnabled) {
                    notificationScheduler.scheduleNotification(newNotification)
                }
                onSuccess()
                loadNotifications()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления: ${e.message}"
            }
        }
    }

    fun updateNotification(notification: NotificationItem, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val old = repository.getNotificationById(notification.id)
                repository.update(notification)

                if (old?.timestamp != notification.timestamp || old?.isEnabled != notification.isEnabled) {
                    notificationScheduler.cancelNotification(notification.id)
                    if (notification.isEnabled) {
                        notificationScheduler.scheduleNotification(notification)
                    }
                }
                onSuccess()
                loadNotifications()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка обновления: ${e.message}"
            }
        }
    }

    fun deleteNotification(notification: NotificationItem) {
        viewModelScope.launch {
            try {
                notificationScheduler.cancelNotification(notification.id)
                repository.delete(notification)
                loadNotifications()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления: ${e.message}"
            }
        }
    }

    fun toggleEnabled(notification: NotificationItem) {
        viewModelScope.launch {
            try {
                val updated = notification.copy(isEnabled = !notification.isEnabled)
                repository.update(updated)

                if (updated.isEnabled) {
                    notificationScheduler.scheduleNotification(updated)
                } else {
                    notificationScheduler.cancelNotification(updated.id)
                }
                loadNotifications()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка изменения статуса: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}