package ci.nsu.mobile.main.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodels.StepTwoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoScreen(
    periodMonths: Int,
    initialAmount: Double,
    viewModel: StepTwoViewModel = viewModel(),
    onBack: () -> Unit,
    onCalculate: (Double, Double?, Double, Double) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showWarning by remember { mutableStateOf(false) }

    LaunchedEffect(periodMonths) {
        viewModel.initializeRates(periodMonths)
        if (viewModel.state.value.availableRates.isEmpty()) {
            showWarning = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Шаг 2: Дополнительные параметры") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (showWarning) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Срок вклада не указан. Вернитесь и укажите корректный срок.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            ExposedDropdownMenuBox(
                expanded = state.availableRates.isNotEmpty(),
                onExpandedChange = {}
            ) {
                TextField(
                    value = state.selectedInterestRate?.let { "$it%" } ?: "Выберите ставку",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Процентная ставка") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.availableRates.isNotEmpty()
                )

                DropdownMenu(
                    expanded = false,
                    onDismissRequest = {}
                ) {
                    state.availableRates.forEach { rate ->
                        DropdownMenuItem(
                            text = { Text("$rate%") },
                            onClick = { viewModel.updateSelectedRate(rate) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.monthlyTopUp,
                onValueChange = viewModel::updateMonthlyTopUp,
                label = { Text("Ежемесячное пополнение") },
                isError = state.monthlyTopUpError != null,
                supportingText = state.monthlyTopUpError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        val rate = viewModel.getSelectedRate()
                        if (rate != null && viewModel.isValid()) {
                            onCalculate(
                                initialAmount,
                                viewModel.getMonthlyTopUp(),
                                rate,
                                periodMonths.toDouble()
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = state.selectedInterestRate != null && viewModel.isValid()
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}