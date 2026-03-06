package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.vm.CounterViewModel

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        // Отображение uiState
        Text(text = uiState.count.toString())

        // Вызов методов ViewModel
        Button(onClick = { viewModel.increment() }) {
            Text("+1")
        }
        Button(onClick = { viewModel.decrement() }) {
            Text("-1")
        }
        Button(onClick = { viewModel.reset() }) {
            Text("Сброс")
        }
    }
    LazyColumn {
    }
}