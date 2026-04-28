package ci.nsu.moble.main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.moble.main.domain.DepositUiState
import ci.nsu.moble.main.presentation.viewmodel.CalculationViewModel


@Composable
fun Step1Screen(
    navController: NavController,
    viewModel: CalculationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Этап 1: Основные параметры",
            style = MaterialTheme.typography.titleLarge
        )

        // Поле "Стартовый взнос"
        TextField(
            value = uiState.initialAmount,
            onValueChange = viewModel::onInitialAmountChanged,
            label = { Text("Стартовый взнос *") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.error?.contains("сумму") == true
        )

        // Поле "Срок в месяцах"
        TextField(
            value = uiState.periodMonths,
            onValueChange = viewModel::onPeriodMonthsChanged,
            label = { Text("Срок в месяцах *") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.error?.contains("срок") == true
        )

        // Отображение ошибки
        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопки навигации
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
            Button(
                onClick = { navController.navigate("step2") },
                modifier = Modifier.weight(1f),
                enabled = uiState.error == null
            ) {
                Text("Далее")
            }
        }
    }
}