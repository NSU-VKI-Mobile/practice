package ci.nsu.moble.main.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun MainScreen(navController: NavController) {
    val activity = (LocalContext.current as? Activity)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { navController.navigate("step1") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Рассчитать")
        }

        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("История расчётов")
        }

        OutlinedButton(
            onClick = { activity?.finish() },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Закрыть приложение")
        }
    }
}