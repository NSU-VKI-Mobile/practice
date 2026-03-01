package ci.nsu.moble.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineMedium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCalculateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }

            Button(
                onClick = onHistoryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("История расчётов")
            }

            Button(
                onClick = onExitClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Закрыть приложение")
            }
        }
    }
}