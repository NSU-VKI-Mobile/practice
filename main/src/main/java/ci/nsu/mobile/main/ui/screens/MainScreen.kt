package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.navigation.Screen
import kotlin.system.exitProcess

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Расчёт вкладов")

        Button(onClick = { navController.navigate(Screen.Step1.route) }) {
            Text("Рассчитать")
        }

        Button(onClick = { navController.navigate(Screen.History.route) }) {
            Text("История расчётов")
        }

        Button(onClick = {
            exitProcess(0)
        }) {
            Text("Закрыть приложение")
        }
    }
}