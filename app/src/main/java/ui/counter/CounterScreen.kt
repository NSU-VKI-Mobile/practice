package ci.nsu.mobile.main.ui.counter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Текущий счетчик
        Text(
            text = "Счетчик: ${uiState.count}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопи
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.increment() }) {
                Text("+")
            }

            utton(onClick = { viewModel.decrement() }) {
                Text("-")
            }

            Button(onClick = { viewModel.reset() }) {
                Text("Сброс")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // История
        Text(
            text = "История:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(uiState.history) { item ->
                Text(
                    text = item,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}