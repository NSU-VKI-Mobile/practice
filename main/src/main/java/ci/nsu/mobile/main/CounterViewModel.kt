package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {
    // Внутреннее изменяемое состояние. Доступно только внутри ViewModel
    private val _uiState = MutableStateFlow(CounterUiState())

    // Публичное неизменяемое состояние для UI (интерфейс только читает)
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            // Берем текущую историю, оставляем только первые 4 элемента (take(4))
            // и добавляем новое действие в начало, чтобы всего было не больше 5
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val newHistory = listOf("Сброс (итого: 0)") + currentState.history.take(4)
            currentState.copy(
                count = 0,
                history = newHistory
            )
        }
    }
}