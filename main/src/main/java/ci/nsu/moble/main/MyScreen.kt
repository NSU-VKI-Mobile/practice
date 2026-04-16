package ci.nsu.moble.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.theme.PracticeTheme

@Composable
fun MyScreen() {
    val viewModel: MyViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Отображение счетчика
            Text(
                text = "Счетчик: ${uiState.count}",
                fontSize = 32.sp
            )

            // Кнопки
            Button(onClick = { viewModel.increment() }) {
                Text("Кнопка +1")
            }

            Button(onClick = { viewModel.decrement() }) {
                Text("Кнопка -1")
            }

            Button(onClick = { viewModel.reset() }) {
                Text("Кнопка Сброс")
            }

            // Заголовок истории
            Text(
                text = "История действий:",
                modifier = Modifier.padding(top = 16.dp)
            )

            // Список истории
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.history) { historyItem ->
                    Text(text = historyItem)
                }
            }
        }
    }
}
