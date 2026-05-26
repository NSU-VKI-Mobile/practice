package ci.nsu.moble.calculations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ci.nsu.moble.domain.models.DepositCalculation

@Composable
fun Step1Screen(
    state: DepositUiState,
    onAmountChange: (String) -> Unit,
    onMonthsChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToStep2: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Этап 1: Ввод данных", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.amount,
            onValueChange = { value ->
                if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
                    onAmountChange(value)
                }
            },
            label = { Text("Сумма") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.months,
            onValueChange = { value ->
                if (value.isEmpty() || value.matches(Regex("^\\d*$"))) {
                    onMonthsChange(value)
                }
            },
            label = { Text("Срок (мес)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateBack) { Text("Назад") }
            Button(
                onClick = onNavigateToStep2,
                enabled = state.amount.isNotBlank() && state.months.isNotBlank()
            ) { Text("Далее") }
        }
    }
}

@Composable
fun Step2Screen(
    state: DepositUiState,
    onTopUpChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onCalculate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Этап 2: Доп. параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Ставка: ${state.selectedRate}%", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.monthTopUp,
            onValueChange = { value ->
                if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
                    onTopUpChange(value)
                }
            },
            label = { Text("Пополнение (мес)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onNavigateBack) { Text("Назад") }
            Button(onClick = onCalculate) { Text("Рассчитать") }
        }
    }
}

@Composable
fun ResultScreen(
    result: DepositCalculation?,
    onSaveAndExit: () -> Unit
) {
    if (result == null) return

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Итог: ${String.format("%.2f", result.finalAmount)} ₽", style = MaterialTheme.typography.headlineSmall)
                Text("Прибыль: ${String.format("%.2f", result.profit)} ₽")
                Text("Ставка: ${result.rate}% на ${result.months} мес.")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSaveAndExit, modifier = Modifier.fillMaxWidth()) {
            Text("Сохранить и выйти")
        }
    }
}

@Composable
fun HistoryScreen(
    history: List<DepositCalculation>,
    onDelete: (DepositCalculation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(history) { item ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(item.calculationDate.toString(), style = MaterialTheme.typography.labelSmall)
                    Text("Итого: ${String.format("%.2f", item.finalAmount)} (из ${item.initialAmount})")
                    TextButton(onClick = { onDelete(item) }) {
                        Text("Удалить", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}