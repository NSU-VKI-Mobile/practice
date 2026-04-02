package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onCalculateClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExitClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Закрыть приложение")
        }
    }
}