package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.DepositViewModel

@Composable
fun Step1Screen(viewModel: DepositViewModel, onNext: () -> Unit, onCancel: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Этап 1: Основные параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.initialAmount,
            onValueChange = { viewModel.initialAmount = it },
            label = { Text("Стартовый взнос *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = viewModel.periodMonths,
            onValueChange = { viewModel.periodMonths = it },
            label = { Text("Срок вклада (в месяцах) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = {
                viewModel.resetData()
                onCancel()
            }) { Text("В начало") }
            Button(
                onClick = onNext,
                enabled = viewModel.initialAmount.toDoubleOrNull() != null && viewModel.periodMonths.toIntOrNull() != null
            ) { Text("Далее") }
        }
    }
}