package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.LocalDepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val viewModel = LocalDepositViewModel.current   // <-- общая ViewModel

    var amount by remember { mutableStateOf(viewModel.initialAmount?.toString() ?: "") }
    var months by remember { mutableStateOf(viewModel.periodMonths?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Этап 1: Основные параметры") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("В начало") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    errorMessage = null
                },
                label = { Text("Стартовый взнос (обязательно)") },
                isError = errorMessage != null,
                supportingText = { errorMessage?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = months,
                onValueChange = {
                    months = it
                    errorMessage = null
                },
                label = { Text("Срок в месяцах (обязательно)") },
                isError = errorMessage != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val a = amount.toDoubleOrNull()
                    val m = months.toIntOrNull()
                    if (a == null || a <= 0 || m == null || m <= 0) {
                        errorMessage = "Введите корректные положительные числа"
                        return@Button
                    }
                    // Сохраняем в общую ViewModel
                    viewModel.initialAmount = a
                    viewModel.periodMonths = m
                    onNext()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Далее")
            }
        }
    }
}