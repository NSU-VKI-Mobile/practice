package ci.nsu.moble.main.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val COUNT_KEY = "count"
        private const val HISTORY_KEY = "history"
    }

    val uiState: StateFlow<CounterUiState> = savedStateHandle.getStateFlow(
        "ui_state",
        CounterUiState(
            count = savedStateHandle[COUNT_KEY] ?: 0,
            history = savedStateHandle[HISTORY_KEY] ?: emptyList()
        )
    )

    fun increment() {
        val current = uiState.value
        val newCount = current.count + 1
        val newHistory = (listOf("+1 (итого: $newCount)") + current.history).take(5)
        updateState(newCount, newHistory)
    }

    fun decrement() {
        val current = uiState.value
        val newCount = current.count - 1
        val newHistory = (listOf("-1 (итого: $newCount)") + current.history).take(5)
        updateState(newCount, newHistory)
    }

    fun reset() {
        val newCount = 0
        val newHistory = (listOf("Сброс (итого: $newCount)") + uiState.value.history).take(5)
        updateState(newCount, newHistory)
    }

    fun clearHistory() {
        val newHistory = emptyList<String>()
        updateState(uiState.value.count, newHistory)
    }

    private fun updateState(count: Int, history: List<String>) {
        val newState = CounterUiState(count, history)
        savedStateHandle["ui_state"] = newState
        savedStateHandle[COUNT_KEY] = count
        savedStateHandle[HISTORY_KEY] = ArrayList(history)
    }
}

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
) : java.io.Serializable
