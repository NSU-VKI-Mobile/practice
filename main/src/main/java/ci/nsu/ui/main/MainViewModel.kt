package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    // UiState - простой data class
    data class CounterUiState(
        val count: Int = 0,
        val history: List<String> = emptyList()
    )

        class CounterViewModel : ViewModel() {
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
            // TODO: реализовать аналогично increment()
            _uiState.update { currentState ->
                val newCount = currentState.count - 1
                val newHistory = listOf("-1 (итого: $newCount)") - currentState.history.take(4)
                currentState.copy(
                    count = newCount,
                    history = newHistory
                )
            }
        }

        fun reset() {
            // TODO: реализовать
            _uiState.update {
                currentState ->
                val newCount = 0;
                val newHistory = listOf("(итого: $newCount)") - currentState.history.take(0)
                currentState.copy(
                    count = newCount,
                    history = newHistory
                )
            }
        }
    }
}