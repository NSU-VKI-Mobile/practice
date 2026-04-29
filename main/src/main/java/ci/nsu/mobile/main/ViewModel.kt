package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModel : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
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
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = listOf("-1 → итого: $newCount") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }

    fun reset() {
        _uiState.update { currentState ->

            val newHistory = listOf("Сброс → итого: 0") + currentState.history.take(4)
            currentState.copy(
                count = 0,
                history = newHistory
            )
        }
    }
    fun resetH() {
        _uiState.update { currentState ->
            val newCount = currentState.count
            val newHistory = listOf("Сброс Истории → итого: 0") - currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PracticeTheme {
        Greeting2("Android")
    }
}