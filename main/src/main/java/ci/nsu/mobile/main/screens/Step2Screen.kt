package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val monthlyTopUp by viewModel::monthlyTopUp
    val selectedRate by viewModel::selectedRate
    val availableRates by viewModel::availableRates
    val validationError by viewModel::validationError

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Шаг 2: Дополнительные параметры") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Выберите процентную ставку:")

            if (availableRates.isEmpty()) {
                Text(
                    text = "Сначала укажите срок на первом шаге",
                    color = androidx.compose.ui.graphics.Color.Red
                )
            } else {
                availableRates.forEach { rate ->
                    Button(
                        onClick = { viewModel.selectedRate = rate },
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            if (selectedRate == rate) "✓ $rate%" else "$rate%"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = monthlyTopUp,
                onValueChange = { viewModel.monthlyTopUp = it },
                label = { Text("Ежемесячное пополнение (₽) (необязательно)") },
                modifier = Modifier.fillMaxSize()
            )

            if (validationError != null) {
                Text(
                    text = validationError!!,
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (viewModel.validateStep2()) {
                        viewModel.calculateResult()
                        navController.navigate("result")
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Рассчитать")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Назад")
            }
        }
    }
}