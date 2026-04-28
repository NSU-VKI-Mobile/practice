package ci.nsu.moble.main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import android.app.Activity

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        // Заголовок приложения
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineLarge
        )

        // Кнопка "Рассчитать"
        Button(
            onClick = { navController.navigate("step1") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        // Кнопка "История расчётов"
        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История расчётов")
        }

        // Кнопка "Закрыть приложение"
        Button(
            onClick = {
                // Получаем Activity и закрываем приложение
                (context as? Activity)?.finishAndRemoveTask()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Закрыть приложение")
        }
    }
}