package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@Composable
fun Step2Screen(navController: NavController, vm: DepositViewModel) {

    val months by vm.months.collectAsState()
    val topUp by vm.topUp.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedRate by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val rates = when (months.toIntOrNull()) {
        null -> emptyList()
        in 0..5 -> listOf("15%")
        in 6..11 -> listOf("10%")
        else -> listOf("5%")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Выберите ставку")

        Box {
            Button(onClick = { expanded = true }) {
                Text(if (selectedRate.isEmpty()) "Выбрать" else selectedRate)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 0.dp, y = (-150).dp),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                rates.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selectedRate = it
                            vm.setRate(it.replace("%", "").toDouble())
                            expanded = false
                            error = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = topUp,
            onValueChange = {
                vm.setTopUp(it)
                error = ""
            },
            label = { Text("Ежемесячное пополнение") }
        )

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
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
                when {
                    rates.isEmpty() -> error = "Некорректный срок"
                    selectedRate.isEmpty() -> error = "Выберите ставку"
                    topUp.isNotEmpty() && topUp.toDoubleOrNull() == null ->
                        error = "Пополнение число"
                    else -> navController.navigate("result")
                }
            }) {
                Text("Рассчитать")
            }
        }
    }
}