package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.navigation.Screen
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun ResultScreen(navController: NavController, vm: DepositViewModel) {

    val result = vm.result ?: return

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card {
            Column {
                Text("Сумма: ${result.amount}")
                Text("Срок: ${result.months}")
                Text("Ставка: ${result.rate}")
                Text("Итог: ${result.finalAmount}")
                Text("Прибыль: ${result.profit}")
            }
        }

        Button(onClick = { vm.save() }) {
            Text("Сохранить")
        }

        Button(onClick = {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Main.route) { inclusive = true }
            }
            vm.reset()
        }) {
            Text("В начало")
        }
    }
}