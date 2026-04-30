package ci.nsu.mobile.main

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
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(9)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            if (currentState.count > 0) {
                val newCount = currentState.count - 1
                val historyEntry = " -1 (итого: $newCount)"

                val newHistory = (listOf(historyEntry) + currentState.history).take(10)
                currentState.copy(
                    count = newCount,
                    history = newHistory
                )
            } else {
                currentState
            }
        }
    }


    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                count = 0,
                history = listOf("Сброс") + currentState.history.take(10)
            )
        }
    }
}