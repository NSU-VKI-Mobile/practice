package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    val initialAmount by viewModel::initialAmount
    val periodMonths by viewModel::periodMonths
    val validationError by viewModel::validationError

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Шаг 1: Основные параметры") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = initialAmount,
                onValueChange = {
                    // Только цифры и точка/запятая
                    if (it.isEmpty() || it.matches(Regex("^\\d*[.,]?\\d*$"))) {
                        viewModel.initialAmount = it.replace(',', '.')
                    }
                },
                label = { Text("Стартовый взнос (₽)") },
                isError = validationError != null && validationError!!.contains("взнос"),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = periodMonths,
                onValueChange = {
                    // Только цифры
                    if (it.isEmpty() || it.matches(Regex("^\\d*$"))) {
                        viewModel.periodMonths = it
                        viewModel.updateAvailableRates(it)
                    }
                },
                label = { Text("Срок вклада (месяцы)") },
                isError = validationError != null && validationError!!.contains("срок"),
                modifier = Modifier.fillMaxWidth()
            )

            if (validationError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = validationError!!,
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (viewModel.validateStep1()) {
                        navController.navigate("step2")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.reset()
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В начало")
            }
        }
    }
}