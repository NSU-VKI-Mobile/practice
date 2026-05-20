package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(nav: NavController, vm: DepositViewModel) {

    val s by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Результат") }) }
    ) { padding ->

        Column(
            Modifier.padding(padding).padding(16.dp)
        ) {

            Card (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Column(Modifier.padding(16.dp)) {

                    Text("Старт: ${s.initialAmount}")
                    Text("Срок: ${s.months}")
                    Text("Ставка: ${s.rate}%")
                    Text("Пополнение: ${s.monthly}")
                    Text("Итог: ${"%.2f".format(s.finalAmount)}")
                    Text("Доход: ${"%.2f".format(s.interest)}")
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { vm.save();
                    nav.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                          vm.clear()},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    nav.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}