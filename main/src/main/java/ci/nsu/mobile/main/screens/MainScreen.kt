package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Расчёт вкладов") })
        }
    ) {
        Column {
            Button(onClick = { navController.navigate("step1") }) {
                Text("Рассчитать")
            }

            Button(onClick = { navController.navigate("history") }) {
                Text("История расчётов")
            }

            Button(onClick = { /* TODO */ }) {
                Text("Закрыть приложение")
            }
        }
    }
}