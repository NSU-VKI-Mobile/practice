package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Главный экран - Загрузка и отображение пользователей
class UsersViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Состояние UI (что видит пользователь)
    private val _uiState = MutableStateFlow(UsersUiState())
    //MutableStateFlow - изменяемый поток
    val uiState: StateFlow<UsersUiState> = _uiState
    //StateFlow - поток данных, на который может подписаться UI

    init {
        loadUsers()  // Загружаем пользователей при создании
    }

    //Загружает список пользователей с сервера
    fun loadUsers() {
        viewModelScope.launch {
            // Показываем прогресс и убираем ошибку
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            //Запрашиваем список пользователей
            val result = authRepository.getUsers()

            // Обрабатываем результат
            result.onSuccess { users ->
                // Успех: сохраняем список и убираем прогресс
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    users = users
                )
            }.onFailure { exception ->
                // Ошибка: убираем прогресс и показываем сообщение
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Ошибка загрузки пользователей"
                )
            }
        }
    }

    //Выход из аккаунта
    fun logout(onSuccess: () -> Unit) {
        authRepository.logout()  // Удаляем токен
        onSuccess()              // Переходим на экран входа
    }
}

//UsersUiState - состояние главного экрана (содержит список пользователей и служебные поля)
data class UsersUiState(
    val users: List<UserDto> = emptyList(),  // Список пользователей
    val isLoading: Boolean = false,          // Показывать прогресс?
    val error: String? = null                // Текст ошибки
)