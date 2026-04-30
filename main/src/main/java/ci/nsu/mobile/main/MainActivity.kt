package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                MyScreen()
            }
        }
    }
}

@Composable
fun MyScreen(viewModel: CounterViewModel = viewModel()) {
    val uiState by viewModel.uiState .collectAsStateWithLifecycle()

    Column {
        Text(text = "Счет: ${uiState.count}")

        Button(onClick = { viewModel.increment() }) {
            Text("+")
        }
        Button(onClick = { viewModel.decrement() }) {
            Text("-")
        }
        Button(onClick = { viewModel.reset() }) {
            Text("Сброс")
        }
        Text(
            text = "История:\n${uiState.history.take(10).joinToString(separator = "\n")}"
        )
    }
}