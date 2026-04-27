package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ViewModel.DepositViewModel

@Composable
fun Step1Screen(navController: NavController, viewModel: DepositViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Этап 1: Основные параметры", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = viewModel.initialAmount,
            onValueChange = { viewModel.initialAmount = it },
            label = { Text("Стартовый взнос") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.periodMonths,
            onValueChange = { viewModel.periodMonths = it },
            label = { Text("Срок (в месяцах)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            TextButton(onClick = { navController.navigate("main") }) { Text("В начало") }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("step2") },
                enabled = viewModel.initialAmount.isNotEmpty() && viewModel.periodMonths.isNotEmpty()
            ) { Text("Далее") }
        }
    }
}