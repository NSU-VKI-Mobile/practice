package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(
    onItemClick: (Long) -> Unit,
    onBackToMain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История расчётов",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Временная заглушка для списка
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Здесь будет список сохранённых расчётов")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackToMain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}