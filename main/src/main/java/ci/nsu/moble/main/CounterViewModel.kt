package ci.nsu.moble.main

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
        _uiState.update { state ->
            val newCount = state.count + 1
            state.copy(
                count = newCount,
                history = listOf("+1 → итого: $newCount") + state.history.take(4)
            )
        }
    }

    fun decrement() {
        _uiState.update { state ->
            val newCount = state.count - 1
            state.copy(
                count = newCount,
                history = listOf("-1 → итого: $newCount") + state.history.take(4)
            )
        }
    }

    fun reset() {
        _uiState.update { state ->
            state.copy(
                count = 0,
                history = listOf("Сброс → итого: 0") + state.history.take(4)
            )
        }
    }

    fun clear() {
        _uiState.update { state ->
            state.copy(
                history = emptyList()
            )
        }
    }
}