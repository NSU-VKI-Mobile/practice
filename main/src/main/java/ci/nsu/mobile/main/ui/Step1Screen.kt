package ci.nsu.mobile.main.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Step1Screen(navController: NavController) {

    var amount by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Шаг 1: Основные данные")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Стартовый взнос") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = months,
            onValueChange = { months = it },
            label = { Text("Срок (месяцы)") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {

            Button(onClick = { navController.popBackStack() }) {
                Text("В начало")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = {
                if (amount.isNotEmpty() && months.isNotEmpty()) {
                    navController.navigate("step2/$amount/$months")
                }
            }) {
                Text("Далее")
            }
        }
    }
}