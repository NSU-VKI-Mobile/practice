package ci.nsu.mobile.main.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(onNavigateToStep1: () -> Unit, onNavigateToHistory: () -> Unit) {
    val activity = (LocalContext.current as? Activity)
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Расчёт вкладов", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateToStep1, modifier = Modifier.fillMaxWidth()) {
            Text("Рассчитать")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToHistory, modifier = Modifier.fillMaxWidth()) {
            Text("История расчётов")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = { activity?.finish() }, modifier = Modifier.fillMaxWidth()) {
            Text("Закрыть приложение")
        }
    }
}