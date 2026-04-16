package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.CounterUiState
import ci.nsu.mobile.main.viewmodel.CounterViewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Счётчик",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "${uiState.count}",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { viewModel.decrement() }) {
                Text("-")
            }
            Button(onClick = { viewModel.reset() }) {
                Text("Сброс")
            }
            Button(onClick = { viewModel.increment() }) {
                Text("+")
            }
            Button(onClick = { viewModel.ResetHistory() }) {
                Text("-История")
            }
        }

        Text(
            text = "История действий:",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.history) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (uiState.history.isEmpty()) {
                item {
                    Text(
                        text = "История пока пуста",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}