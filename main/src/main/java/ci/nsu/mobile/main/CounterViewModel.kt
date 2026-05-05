package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {

    // Приватный изменяемый поток
    private val _uiState = MutableStateFlow(CounterUiState())
    // Публичный, только для чтения
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val action = "+1 (итого: $newCount)"
            val newHistory = listOf(action) + currentState.history.take(4)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val action = "-1 (итого: $newCount)"
            val newHistory = listOf(action) + currentState.history.take(4)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val action = "Сброс (было: ${currentState.count})"
            val newHistory = listOf(action) + currentState.history.take(4)
            currentState.copy(count = 0, history = newHistory)
        }
    }
}