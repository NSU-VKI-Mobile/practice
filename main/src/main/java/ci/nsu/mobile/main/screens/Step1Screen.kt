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
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = initialAmount,
                onValueChange = { viewModel.initialAmount = it },
                label = { Text("Стартовый взнос (₽)") },
                isError = validationError != null && validationError!!.contains("взнос"),
                modifier = Modifier.fillMaxSize()
            )

            OutlinedTextField(
                value = periodMonths,
                onValueChange = {
                    viewModel.periodMonths = it
                    viewModel.updateAvailableRates(it)
                },
                label = { Text("Срок вклада (месяцы)") },
                isError = validationError != null && validationError!!.contains("срок"),
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
                    if (viewModel.validateStep1()) {
                        navController.navigate("step2")
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Далее")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.reset()
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Text("В начало")
            }
        }
    }
}