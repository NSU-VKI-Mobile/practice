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

class CounterVM : ViewModel()
{
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment()
    {
        _uiState.update{ currentState ->
            val newCount = currentState.count + 1
            updateState(newCount, "+1 (всего: $newCount)", currentState)
        }
    }

    fun decrement()
    {
        _uiState.update{ currentState ->
            val newCount = currentState.count - 1
            updateState(newCount, "-1 (всего: $newCount)", currentState)
        }
    }

    fun reset()
    {
        _uiState.update{ currentState ->
            CounterUiState(count = 0, history = (listOf("Сброс") + currentState.history).take(5))
        }
    }
    private fun updateState (newCount: Int, action: String, currentState: CounterUiState) : CounterUiState
    {
        return currentState.copy(count = newCount, history = (listOf(action) + currentState.history).take(5))
    }
}