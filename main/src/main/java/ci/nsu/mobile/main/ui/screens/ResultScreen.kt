package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.navigation.Screen
import ci.nsu.mobile.main.viewModel.DepositViewModel

@Composable
fun ResultScreen(navController: NavController, vm: DepositViewModel) {

    val result = vm.result ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Результат")

                Spacer(modifier = Modifier.height(12.dp))

                Text("Сумма: ${vm.formatDouble(result.amount)}")
                Text("Срок: ${result.months} мес.")
                Text("Ставка: ${result.rate}%")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Итог: ${vm.formatDouble(result.finalAmount)}")
                Text("Прибыль: ${vm.formatDouble(result.profit)}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { vm.save()
                navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Main.route) { inclusive = true }
            }
                vm.reset() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }


    }
}