package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import androidx.compose.material.icons.filled.ArrowDropDown
@Composable
fun SecondStepScreen(
    viewModel: DepositViewModel,
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    val state by viewModel.secondStepState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Дополнительные параметры",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Информация о введённых данных
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("💰 Стартовый взнос: ${String.format("%.2f", state.initialAmount)} руб")
                Text("📅 Исходный срок: ${state.periodMonths} месяцев")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Выбор процентной ставки
        Text(
            text = "Выберите процентную ставку:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Выпадающий список (DropdownMenu)
        var expanded by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = "${state.selectedRate}% (срок ${state.selectedPeriodMonths} мес)",
            onValueChange = {},
            readOnly = true,
            label = { Text("Процентная ставка") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                        contentDescription = "Выбрать"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            state.availableRates.forEach { (rate, period) ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text("${String.format("%.0f", rate)}% годовых")
                            Text(
                                text = "Срок: $period месяцев",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        viewModel.selectRate(rate)
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для ежемесячного пополнения
        OutlinedTextField(
            value = state.monthlyTopUp,
            onValueChange = { viewModel.updateMonthlyTopUp(it) },
            label = { Text("Ежемесячное пополнение (руб)") },
            placeholder = { Text("Необязательное поле") },
            isError = state.monthlyTopUpError != null,
            supportingText = {
                if (state.monthlyTopUpError != null) {
                    Text(state.monthlyTopUpError!!)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    viewModel.calculateResult()
                    onCalculate()
                },
                enabled = state.isCalculateEnabled
            ) {
                Text("Рассчитать")
            }
        }
    }
}