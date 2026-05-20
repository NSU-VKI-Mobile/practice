package ci.nsu.mobile.main.ui.step1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun Step1Screen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var isWarningVisible by remember { mutableStateOf(false) };
    val regex = Regex("^\\d*(\\.\\d{0,2})?$")

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Шаг 1")

        Spacer(Modifier.height(12.dp))

        TextField(
            value = amount,
            onValueChange = {
                if ((it.isEmpty() || it.matches(regex)) && it.length <= 7) {
                    amount = it
                }
            },
            label = { Text("Стартовый взнос") }
        )

        TextField(
            value = months,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() } && it.length <= 2) {
                    months = it
                }
            },
            label = { Text("Срок (мес)") }
        )

        Spacer(Modifier.height(16.dp))

        if (isWarningVisible) {
            Text(
                text = "Введите срок!",
                color = Color.Red,
                fontSize = 42.sp
            )
        }
        else {
            Text(
                text = "",
                color = Color.Red,
                fontSize = 42.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (months != "") {
                if (amount.isNotBlank() && months.isNotBlank()) {
                    viewModel.startAmount = amount
                    viewModel.months = months
                    navController.navigate("step2")
                }
            }
            else {
                isWarningVisible = true;
            }
        }) {
            Text("Далее")
        }
    }
}