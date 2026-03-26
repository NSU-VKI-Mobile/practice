package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.*
import ci.nsu.mobile.main.ui.CounterUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyScreen()
        }
    }
}

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
            val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(
                count = newCount,
                history = newHistory
            )
        }
    }
    fun reset() {
        _uiState.update { currentState ->
            val resetCount = 0
            val resetHistory: List<String> = emptyList()
            currentState.copy(
                count = resetCount,
                history = resetHistory
            )
        }
    }
}

        @Composable
        fun MyScreen(viewModel: CounterViewModel = viewModel()
        ) {
              val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Отображение uiState
                Text(text = "123")

                // Вызов методов ViewModel
                Button(
                    modifier = Modifier
                        .size(200.dp, 100.dp)
                        .padding(16.dp),
                    onClick = { viewModel.increment() }) {
                    Text("+")
                }
                Button(
                    modifier = Modifier
                        .size(200.dp, 100.dp)
                        .padding(16.dp),
                    onClick = { viewModel.decrement() }) {
                    Text("-")
                }
                Button(
                    modifier = Modifier
                        .size(200.dp, 100.dp)
                        .padding(16.dp),
                    onClick = { viewModel.reset() }) {
                    Text("Reset")
                }
            }
        }