package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Экран входа - Логика входа в систему
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Состояние UI (что видит пользователь)
    private val _uiState = MutableStateFlow(LoginUiState())
    //MutableStateFlow - изменяемый поток
    val uiState: StateFlow<LoginUiState> = _uiState
    //StateFlow - поток данных, на который может подписаться UI

    fun onLoginChange(login: String) { // Вызывается когда пользователь вводит логин
        // Обновляем состояние: копируем текущее состояние, но меняем поле login
        _uiState.value = _uiState.value.copy(login = login)
    }

    fun onPasswordChange(password: String) { // Вызывается когда пользователь вводит пароль
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login(onSuccess: () -> Unit) { //Вызывается когда пользователь нажал кнопку "Войти"
        viewModelScope.launch {  // Запускаем корутину (асинхронная операция, не блокирует UI)
            //Показываем прогресс-бар и убираем старую ошибку
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = authRepository.login( // Вызываем репозиторий для входа
                _uiState.value.login, // Логин из состояния
                _uiState.value.password
            )

            result.onSuccess { //Обрабатываем результат
                // Успех: убираем прогресс-бар и переходим на главный экран
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()  // Переход на главный экран
            }.onFailure { exception -> //// Ошибка: убираем прогресс-бар и показываем сообщение об ошибке
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Ошибка входа"
                )
            }
        }
    }

    fun clearError() { //Убирает сообщение об ошибке (вызывается когда пользователь начинает печатать)
        _uiState.value = _uiState.value.copy(error = null)
    }
}

//Это неизменяемый (immutable) объект, который содержит ВСЕ данные, необходимые для отображения экрана входа.
data class LoginUiState(
    val login: String = "",           // Текст в поле "Логин"
    val password: String = "",        // Текст в поле "Пароль"
    val isLoading: Boolean = false,   // Показывать ли прогресс-бар?
    val error: String? = null         // Текст ошибки (null = нет ошибки)
)