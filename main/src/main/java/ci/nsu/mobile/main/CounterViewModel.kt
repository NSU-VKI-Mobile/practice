package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {

    // Максимальное количество записей в истории
    companion object {
        private const val MAX_HISTORY_SIZE = 8
    }

    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val action = "+1 (итого: $newCount)"
            currentState.copy(
                count = newCount,
                history = buildHistory(currentState.history, action)
            )
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val action = "-1 (итого: $newCount)"
            currentState.copy(
                count = newCount,
                history = buildHistory(currentState.history, action)
            )
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val action = "Сброс (было: ${currentState.count})"
            currentState.copy(
                count = 0,
                history = buildHistory(currentState.history, action)
            )
        }
    }


    private fun buildHistory(currentHistory: List<String>, action: String): List<String> {
        return listOf(action) + currentHistory.take(MAX_HISTORY_SIZE - 1)
    }
}