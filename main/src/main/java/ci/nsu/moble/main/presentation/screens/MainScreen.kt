package ci.nsu.moble.main.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCalculate: () -> Unit,
    onHistory: () -> Unit,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Расчёт вкладов") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onCalculate, modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)) {
                Text("Рассчитать")
            }
            Button(onClick = onHistory, modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)) {
                Text("История расчётов")
            }
            Button(onClick = onClose, modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)) {
                Text("Закрыть приложение")
            }
        }
    }
}