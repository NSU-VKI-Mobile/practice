package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.navigation.AppRoutes
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun ResultScreen(navController: NavController, vm: DepositViewModel) {

    val (total, interest) = vm.calculateResult()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Старт: ${vm.initialAmount.value}")
                Text("Срок: ${vm.months.value}")
                Text("Ставка: ${vm.rate.value}%")
                Text("Итог: ${String.format("%.2f", total)}")
                Text("Проценты: ${String.format("%.2f", interest)}")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(onClick = { navController.popBackStack() }) {
                Text("Назад")
            }

            Button(onClick = {
                vm.save()
                navController.navigate(AppRoutes.Main.Deposit.Step1)
            }) {
                Text("В начало")
            }
        }
    }
}