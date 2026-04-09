package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.presentation.viewmodel.AdditionalParamsViewModel
import ci.nsu.mobile.main.presentation.viewmodel.MainParamsViewModel

@Composable
fun AdditionalParamsScreen(
    mainParamsData: MainParamsViewModel.MainParamsResult.Success,
    onNavigateBack: () -> Unit,
    onNavigateResult: (Double, Double) -> Unit,
    monthlyTopUp: String = "",
    interestRate: Double = 0.0,
    onTopUpChange: (String) -> Unit = {}
) {
    val viewModel: AdditionalParamsViewModel = viewModel()

    LaunchedEffect(mainParamsData) {
        viewModel.mainParams = mainParamsData
    }

    var topUpValue by remember { mutableStateOf(monthlyTopUp) }
    var expanded by remember { mutableStateOf(false) }

    // Автоматически выбираем ставку на основе срока
    val months = mainParamsData.months
    val availableRates = when {
        months < 6 -> listOf(15.0)
        months < 12 -> listOf(10.0)
        else -> listOf(5.0)
    }

    // По умолчанию выбираем первую доступную ставку
    var selectedRate by remember { mutableStateOf(availableRates.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Дополнительные параметры",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Показываем информацию о сроке
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Срок вклада: ${months} мес.", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Доступная ставка: ${availableRates.first()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (availableRates.size == 1) {
                    Text(
                        text = when {
                            months < 6 -> "Для сроков до 6 месяцев"
                            months < 12 -> "Для сроков 6-11 месяцев"
                            else -> "Для сроков от 12 месяцев"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Выпадающий список (если вдруг ставок несколько)
        if (availableRates.size > 1) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${selectedRate}%")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("${rate}%") },
                            onClick = {
                                selectedRate = rate
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        TextField(
            value = topUpValue,
            onValueChange = {
                topUpValue = it
                onTopUpChange(it)
            },
            label = { Text("Ежемесячное пополнение (₽)") },
            placeholder = { Text("Например: 5000 (необязательно)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Button(onClick = onNavigateBack) { Text("Назад") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val topUpNum = topUpValue.toDoubleOrNull() ?: 0.0
                    onNavigateResult(topUpNum, selectedRate)
                }
            ) {
                Text("Рассчитать")
            }
        }
    }
}