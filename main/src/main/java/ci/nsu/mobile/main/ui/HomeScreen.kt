package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Расчёт вкладов", fontSize = 28.sp, modifier = Modifier.padding(bottom = 32.dp))

        Button(onClick = onCalculateClick, modifier = Modifier.fillMaxWidth(0.7f).padding(8.dp)) { Text("Рассчитать") }
        Button(onClick = onHistoryClick, modifier = Modifier.fillMaxWidth(0.7f).padding(8.dp)) { Text("История расчётов") }
        Button(onClick = onExitClick, modifier = Modifier.fillMaxWidth(0.7f).padding(8.dp)) { Text("Закрыть приложение") }
    }
}