package ci.nsu.mobile.main.ui.additional

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle


//todo проверить все потому что это нейроночное :(
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionalScreen(
    initialAmount: String,
    initialTerm: String,
    viewModel: AdditionalViewModel = AdditionalViewModel(),
    onNavigateBack: () -> Unit,
    onCalculate: (amount: Double, term: Int, rate: Double, monthlyAddition: Double) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(initialAmount, initialTerm) {
        viewModel.initialize(initialAmount, initialTerm)
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Карточка с параметрами
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

            // Выбор ставки
            if (uiState.isTermValid && uiState.depositTerm.isNotBlank()) {
                val availableRates = uiState.ratesWithAvailability.filter { it.isAvailable }

                if (availableRates.isNotEmpty()) {
                    Text(
                        text = "Выберите процентную ставку:",
                        style = MaterialTheme.typography.titleMedium
                    )

                    availableRates.forEach { rateItem ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.selectRate(rateItem.rule)
                                expanded = false
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (uiState.selectedRate == rateItem.rule)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column {
                                    Text(
                                        text = "${rateItem.rule.rate}% годовых",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = rateItem.rule.description,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                if (uiState.selectedRate == rateItem.rule) {
                                    Text("✓", style = MaterialTheme.typography.titleLarge)
                                }
                            }
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

            // Кнопка Рассчитать
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Рассчитать")
            }
        }
    }
}