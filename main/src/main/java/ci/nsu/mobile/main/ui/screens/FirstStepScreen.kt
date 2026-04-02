package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun FirstStepScreen(
    viewModel: DepositViewModel,
    onBackToMain: () -> Unit,
    onNext: () -> Unit
) {
    val state by viewModel.firstStepState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Основные параметры вклада",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Поле "Стартовый взнос"
        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = { viewModel.updateInitialAmount(it) },
            label = { Text("Стартовый взнос (руб)") },
            placeholder = { Text("Введите сумму") },
            isError = state.initialAmountError != null,
            supportingText = {
                if (state.initialAmountError != null) {
                    Text(state.initialAmountError!!)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле "Срок вклада"
        OutlinedTextField(
            value = state.periodMonths,
            onValueChange = { viewModel.updatePeriodMonths(it) },
            label = { Text("Срок вклада (месяцев)") },
            placeholder = { Text("Введите количество месяцев") },
            isError = state.periodMonthsError != null,
            supportingText = {
                if (state.periodMonthsError != null) {
                    Text(state.periodMonthsError!!)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onBackToMain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("В начало")
            }

            Button(
                onClick = {
                    viewModel.goToSecondStep()
                    onNext()
                },
                enabled = state.isNextEnabled
            ) {
                Text("Далее")
            }
        }
    }
}