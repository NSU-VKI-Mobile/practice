package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.LocalDepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    onBack: () -> Unit,
    onCalculate: () -> Unit
) {
    val viewModel = LocalDepositViewModel.current   // <-- общая ViewModel

    val availableRates = viewModel.getAvailableRates()
    var selectedRate by remember {
        mutableStateOf(if (viewModel.interestRate > 0) viewModel.interestRate.toString() else "")
    }
    var topUp by remember { mutableStateOf(viewModel.monthlyTopUp?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Этап 2: Доп. параметры") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        if (availableRates.isEmpty()) {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text("Срок не указан. Вернитесь на первый этап и укажите корректный срок.")
                Button(onClick = onBack) { Text("Назад") }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedRate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Процентная ставка") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        isError = errorMessage != null,
                        supportingText = { errorMessage?.let { Text(it) } },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        availableRates.forEach { rate ->
                            DropdownMenuItem(
                                text = { Text("$rate%") },
                                onClick = {
                                    selectedRate = rate.toString()
                                    expanded = false
                                    errorMessage = null
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = topUp,
                    onValueChange = { topUp = it },
                    label = { Text("Ежемесячное пополнение (необязательно)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val rate = selectedRate.toDoubleOrNull()
                        if (rate == null || rate !in availableRates) {
                            errorMessage = "Выберите ставку из списка"
                            return@Button
                        }
                        viewModel.interestRate = rate
                        viewModel.monthlyTopUp = topUp.toDoubleOrNull()
                        viewModel.calculate()
                        onCalculate()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}