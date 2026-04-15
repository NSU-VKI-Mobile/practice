package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class CounterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { current ->
            val newCount = current.count + 1
            val newHistory = listOf("+1 (итого: $newCount)") + current.history.take(4)
            current.copy(count = newCount, history = newHistory)
        }
    }

    fun decrement() {
        _uiState.update { current ->
            val newCount = current.count - 1
            val newHistory = listOf("-1 (итого: $newCount)") + current.history.take(4)
            current.copy(count = newCount, history = newHistory)
        }
    }

    fun reset() {
        _uiState.update { current ->
            val newHistory = listOf("Сброс (было: ${current.count})") + current.history.take(4)
            current.copy(count = 0, history = newHistory)
        }
    }
}