package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// UiState - простой data class
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class CounterViewModel : ViewModel() {
    // StateFlow для UiState
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    // Метод для увеличения счетчика
    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date())
            val newHistory = listOf("+1 → $newCount ($timestamp)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    // Метод для уменьшения счетчика
    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date())
            val newHistory = listOf("-1 → $newCount ($timestamp)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    // Метод для сброса счетчика
    fun reset() {
        _uiState.update { currentState ->
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date())
            val newHistory = listOf("Сброс → 0 ($timestamp)") + currentState.history.take(4)
            currentState.copy(
                count = 0,
                history = newHistory
            )
        }
    }
}