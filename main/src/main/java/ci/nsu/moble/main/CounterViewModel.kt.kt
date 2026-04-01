package ci.nsu.moble.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Состояние экрана
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)

// Действия пользователя (для чистоты архитектуры)
sealed class CounterAction {
    object Increment : CounterAction()
    object Decrement : CounterAction()
    object Reset : CounterAction()
}

class CounterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    // Единый метод обработки действий
    fun onAction(action: CounterAction) {
        _uiState.update { currentState ->
            when (action) {
                is CounterAction.Increment -> {
                    val newCount = currentState.count + 1
                    currentState.copy(
                        count = newCount,
                        history = addToHistory(currentState.history, "+1 → $newCount")
                    )
                }
                is CounterAction.Decrement -> {
                    val newCount = currentState.count - 1
                    currentState.copy(
                        count = newCount,
                        history = addToHistory(currentState.history, "-1 → $newCount")
                    )
                }
                is CounterAction.Reset -> {
                    currentState.copy(
                        count = 0,
                        history = addToHistory(currentState.history, "Сброс → 0")
                    )
                }
            }
        }
    }

    // Вспомогательный метод: добавление в историю (макс. 5 записей)
    private fun addToHistory(history: List<String>, action: String): List<String> {
        return (listOf(action) + history).take(5)
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PracticeTheme {
        Greeting("Android")
    }
}