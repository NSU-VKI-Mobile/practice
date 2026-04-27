package ci.nsu.moble.main.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// UiState - простой data class
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class MainViewModel : ViewModel() {
    // StateFlow для UiState
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    // Методы для изменения состояния
    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
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
            val newCount = 0
            val newHistory = listOf("Сброс (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }
}