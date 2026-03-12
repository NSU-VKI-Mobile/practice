package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()
// метод для увелечения счетчика
    fun increment() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newCount = currentState.count + 1
                val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
                CounterUiState(
                    count = newCount,
                    history = newHistory
                )
            }
        }
    }
//метод для уменьшения счетчика
    fun decrement() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newCount = currentState.count - 1
                val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(4)
                CounterUiState(
                    count = newCount,
                    history = newHistory
                )
            }
        }
    }
//метод для сброса счетчика
    fun reset() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val newHistory = listOf("Сброс (итого: 0)") + currentState.history.take(4)// берем первые 4 элемента + 5
                CounterUiState(
                    count = 0,
                    history = newHistory
                )
            }
        }
    }
}