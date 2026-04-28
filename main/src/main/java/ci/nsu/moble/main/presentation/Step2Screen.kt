package ci.nsu.moble.main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.moble.main.presentation.viewmodel.CalculationViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

@Composable
fun Step2Screen(
    navController: NavController,
    viewModel: CalculationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Определяем текст ставки для отображения
    // Надёжное определение текста ставки (без сравнения Double)
    val rateText = when {
        uiState.interestRate >= 0.149 -> "15%"  // ~15%
        uiState.interestRate >= 0.099 -> "10%"  // ~10%
        uiState.interestRate >= 0.049 -> "5%"   // ~5%
        else -> "Не выбрана"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Этап 2: Дополнительные параметры",
            style = MaterialTheme.typography.titleLarge
        )

        // Процентная ставка (только для просмотра)
        OutlinedTextField(
            value = rateText,
            onValueChange = {},
            label = { Text("Процентная ставка") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Info,
                    contentDescription = "Информация"
                )
            }
        )

        // Пояснение по ставке
        Text(
            text = when (uiState.periodMonths.toIntOrNull()) {
                in 1..5 -> "Ставка 15% применяется для сроков менее 6 месяцев"
                in 6..11 -> "Ставка 10% применяется для сроков от 6 до 12 месяцев"
                else -> "Ставка 5% применяется для сроков от 12 месяцев"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Поле "Ежемесячное пополнение"
        TextField(
            value = uiState.monthlyTopUp,
            onValueChange = viewModel::onMonthlyTopUpChanged,
            label = { Text("Ежемесячное пополнение (необязательно)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        // Кнопки навигации
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }
            Button(
                onClick = {
                    viewModel.calculate()
                    navController.navigate("result")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Рассчитать")
            }
        }
    }
}