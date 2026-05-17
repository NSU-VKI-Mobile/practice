package ci.nsu.mobile.main.calculations.ui.additional

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionalScreen(
    onNavigateBack: () -> Unit,
    onCalculate: (amount: Double, term: Int, rate: Double, monthlyAddition: Double) -> Unit
) {
    val viewModel: AdditionalViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дополнительные параметры") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("← Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Карточка с параметрами
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Параметры вклада",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Сумма: ${uiState.depositAmount} ₽")
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = uiState.depositTerm,
                        onValueChange = viewModel::updateDepositTerm,
                        label = { Text("Срок вклада (месяцев)") },
                        placeholder = { Text("Например: 12") },
                        isError = uiState.depositTerm.isNotBlank() && !uiState.isTermValid,
                        supportingText = {
                            if (uiState.depositTerm.isNotBlank() && !uiState.isTermValid) {
                                Text("Введите целое положительное число")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            if (uiState.isTermValid && uiState.depositTerm.isNotBlank()) {
                val availableRates = uiState.ratesWithAvailability.filter { it.isAvailable }

                if (availableRates.isNotEmpty()) {
                    OutlinedTextField(
                        value = uiState.selectedRate?.let {
                            "${it.rate}% - ${it.description}"
                        } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Выберите процентную ставку") },
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Text(if (expanded) "▲" else "▼")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        availableRates.forEach { rateItem ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = "${rateItem.rule.rate}% годовых",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = rateItem.rule.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.selectRate(rateItem.rule)
                                    expanded = false
                                },
                                trailingIcon = {
                                    if (uiState.selectedRate == rateItem.rule) {
                                        Text("✓")
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "⚠️ Для срока ${uiState.depositTerm} месяцев нет доступных ставок",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else if (uiState.depositTerm.isNotBlank() && !uiState.isTermValid) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⚠️ Некорректный срок вклада")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Укажите корректный срок в месяцах (целое положительное число)")
                    }
                }
            } else if (uiState.depositTerm.isBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⚠️ Срок не указан")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Укажите срок вклада, чтобы увидеть доступные ставки")
                    }
                }
            }

            // Ежемесячное пополнение
            OutlinedTextField(
                value = uiState.monthlyAddition,
                onValueChange = viewModel::updateMonthlyAddition,
                label = { Text("Ежемесячное пополнение (необязательно)") },
                placeholder = { Text("Например: 5000") },
                isError = uiState.monthlyAddition.isNotBlank() && !uiState.isAdditionValid,
                supportingText = {
                    if (uiState.monthlyAddition.isNotBlank() && !uiState.isAdditionValid) {
                        Text("Введите корректную сумму")
                    } else {
                        Text("Оставьте пустым, если пополнение не планируется")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Ошибка
            if (uiState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.calculate()
                    val amount = uiState.depositAmount.toDoubleOrNull() ?: 0.0
                    val term = uiState.depositTerm.toIntOrNull() ?: 0
                    val rate = uiState.selectedRate?.rate ?: 0.0
                    val monthlyAddition = uiState.monthlyAddition.toDoubleOrNull() ?: 0.0
                    onCalculate(amount, term, rate, monthlyAddition)
                },
                enabled = uiState.canProceed,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Рассчитать")
            }
        }
    }
}