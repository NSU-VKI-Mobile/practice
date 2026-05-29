package ci.nsu.moble.main.api

import android.content.Context
import androidx.core.content.edit
import ci.nsu.moble.main.data.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Manages token within system storage
 */
class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // 1. Создаем реактивные потоки (StateFlow)
    // В качестве стартового значения сразу читаем данные с диска (из SharedPreferences)
    private val _token = MutableStateFlow<String?>(prefs.getString("jwt_token", null))
    val token: StateFlow<String?> = _token.asStateFlow()

    private val _currentUser = MutableStateFlow<UserDto?>(loadUserFromPrefs())
    val currentUser: StateFlow<UserDto?> = _currentUser.asStateFlow()

    // 2. Логика для токена
    fun saveToken(value: String?) {
        prefs.edit {
            if (value != null) putString("jwt_token", value) else remove("jwt_token")
        }
        _token.value = value // Мгновенно уведомляем Compose UI
    }

    // 3. Логика для пользователя
    fun saveCurrentUser(value: UserDto?) {
        prefs.edit {
            if (value != null) {
                putString("current_user", Json.encodeToString(value))
            } else {
                remove("current_user")
            }
        }
        _currentUser.value = value // Мгновенно уведомляем Compose UI
    }

    // 4. Метод полной очистки (Логаут)
    fun clear() {
        prefs.edit {
            remove("jwt_token")
            remove("current_user")
        }
        // Сбрасываем потоки в null, чтобы весь интерфейс сразу перерисовался
        _token.value = null
        _currentUser.value = null
    }

    // Вспомогательный приватный метод для первой инициализации пользователя
    private fun loadUserFromPrefs(): UserDto? {
        val jsonString = prefs.getString("current_user", null) ?: return null
        return try {
            Json.decodeFromString<UserDto>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
}