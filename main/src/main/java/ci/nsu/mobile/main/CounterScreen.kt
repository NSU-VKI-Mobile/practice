package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    // Инициализируем ViewModel. Она переживет поворот экрана.
    viewModel: CounterViewModel = viewModel()
) {
    // Подписываемся на StateFlow. Когда данные во ViewModel изменятся,
    // эта переменная обновится, и UI перерисуется (произойдет рекомпозиция)
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Text для отображения uiState.count
        Text(
            text = "Текущее значение: ${uiState.count}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Column с 3 кнопками (как требуется в задании)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) // отступы между кнопками
        ) {
            // 4. Связываем кнопки с методами ViewModel
            Button(onClick = { viewModel.increment() }) {
                Text("Плюс (+)")
            }
            Button(onClick = { viewModel.decrement() }) {
                Text("Минус (-)")
            }
            Button(onClick = { viewModel.reset() }) {
                Text("Сброс")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "История последних действий:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // 3. LazyColumn для отображения uiState.history
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(uiState.history) { historyItem ->
                Text(
                    text = historyItem,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}