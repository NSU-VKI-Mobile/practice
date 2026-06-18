package ci.nsu.mobile.main.ui.main

import android.media.metrics.Event
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

// UiState - простой data class
data class MainUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

class MainViewModel : ViewModel() {
    // StateFlow для UiState
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    companion object {
        const val historyLength = 50
    }

    // Методы для изменения состояния
    fun increment() {
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(historyLength - 1)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(historyLength - 1)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val newCount = 0
            val newHistory = emptyList<String>()
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun clearHistory() {
        _uiState.update { currentState ->
            val newHistory = emptyList<String>()
            currentState.copy(
                history = newHistory
            )
        }
    }
}