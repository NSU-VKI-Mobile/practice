package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun Step1Screen(navController: NavController, viewModel: DepositViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Этап 1: Основные параметры", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.initialAmount,
            onValueChange = { viewModel.updateInitialAmount(it) },
            label = { Text("Стартовый взнос (₽)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.months,
            onValueChange = { viewModel.updateMonths(it) },
            label = { Text("Срок (месяцев)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { navController.popBackStack() }) { Text("В начало") }
            Button(onClick = { navController.navigate("step2") }, enabled = viewModel.initialAmount.isNotEmpty() && viewModel.months.isNotEmpty()) {
                Text("Далее")
            }
        }
    }
}