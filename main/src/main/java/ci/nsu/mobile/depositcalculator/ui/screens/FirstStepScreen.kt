package ci.nsu.mobile.depositcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstStepScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSecondStep: (Double, Int) -> Unit
) {
    var initialAmount by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Параметры вклада - Шаг 1") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = initialAmount,
                onValueChange = { initialAmount = it },
                label = { Text("Стартовый взнос (₽)") },
                placeholder = { Text("Введите сумму") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = initialAmount.isNotEmpty() && initialAmount.toDoubleOrNull() == null,
                supportingText = {
                    if (initialAmount.isNotEmpty() && initialAmount.toDoubleOrNull() == null) {
                        Text("Введите корректное число")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = periodMonths,
                onValueChange = { periodMonths = it },
                label = { Text("Срок вклада (месяцы)") },
                placeholder = { Text("Введите срок") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = periodMonths.isNotEmpty() && periodMonths.toIntOrNull() == null,
                supportingText = {
                    if (periodMonths.isNotEmpty() && periodMonths.toIntOrNull() == null) {
                        Text("Введите целое число")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = {
                        val amount = initialAmount.toDoubleOrNull()
                        val months = periodMonths.toIntOrNull()

                        when {
                            amount == null -> {
                                errorMessage = "Введите корректную сумму вклада"
                                showErrorDialog = true
                            }
                            amount <= 0 -> {
                                errorMessage = "Сумма вклада должна быть больше 0"
                                showErrorDialog = true
                            }
                            months == null -> {
                                errorMessage = "Введите корректный срок вклада"
                                showErrorDialog = true
                            }
                            months <= 0 -> {
                                errorMessage = "Срок вклада должен быть больше 0"
                                showErrorDialog = true
                            }
                            else -> {
                                onNavigateToSecondStep(amount, months)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Далее")
                }
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Ошибка ввода") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
