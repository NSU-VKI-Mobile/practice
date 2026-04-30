package ci.nsu.mobile.main.viewmodel

// Запечатанный (sealed) класс гарантирует, что состояние может быть только одним из трех вариантов
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    // Состояние загрузки (показываем прогресс-бар)
    object Loading : UiState<Nothing>()

    // Успешное получение данных
    data class Success<T>(val data: T) : UiState<T>()

    // Ошибка (сеть, валидация сервера и т.д.)
    data class Error(val message: String) : UiState<Nothing>()
}