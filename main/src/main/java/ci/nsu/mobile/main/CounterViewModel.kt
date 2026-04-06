package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// UiState — хранит всё состояние экрана
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class CounterViewModel : ViewModel() {

    // _uiState — приватный, менять может только ViewModel
    private val _uiState = MutableStateFlow(CounterUiState())

    // uiState — публичный, UI только читает
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val newHistory = listOf("Сброс (итого: 0)") + currentState.history.take(4)
            currentState.copy(count = 0, history = newHistory)
        }
    }
}
