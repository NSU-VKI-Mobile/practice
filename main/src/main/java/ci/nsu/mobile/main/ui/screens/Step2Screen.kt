package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun Step2Screen(navController: NavController, viewModel: DepositViewModel) {
    val months = viewModel.months.toIntOrNull() ?: 0
    val calculatedRate = when {
        months < 6 -> 15.0
        months < 12 -> 10.0
        else -> 5.0
    }
    LaunchedEffect(Unit) { viewModel.rate = calculatedRate }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Этап 2: Дополнительно", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Выбранная ставка: $calculatedRate%", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.updateMonthlyTopUp(it) },
            label = { Text("Пополнение (₽/мес)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { navController.popBackStack() }) { Text("Назад") }
            Button(onClick = { viewModel.calculate(); navController.navigate("result") }) {
                Text("Рассчитать")
            }
        }
    }
}