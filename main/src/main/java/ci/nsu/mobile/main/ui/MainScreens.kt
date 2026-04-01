package ci.nsu.mobile.main.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ================= ВКЛАДКА 1: ПОЛЬЗОВАТЕЛИ =================
@Composable
fun UsersScreen(viewModel: AuthViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchUsers() }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.users.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Список пользователей пуст")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.users.size) { index ->
                    val user = uiState.users[index]
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Логин: ${user.login}", style = MaterialTheme.typography.titleMedium)
                            user.email?.let { Text("Email: $it", style = MaterialTheme.typography.bodyMedium) }
                        }
                    }
                }
            }
        }
    }
}

// ================= ВКЛАДКА 2: НОВЫЙ РАСЧЁТ (Мини-навигатор) =================
@Composable
fun DepositCalculatorScreen(viewModel: DepositViewModel = koinViewModel()) {
    // Внутренний навигатор только для шагов калькулятора
    val calculatorNavController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = calculatorNavController, startDestination = "step1") {

        // --- ЭТАП 1: ОСНОВНЫЕ ПАРАМЕТРЫ ---
        composable("step1") {
            val isInitialValid = (uiState.initialAmount.toDoubleOrNull() ?: 0.0) > 0.0
            val isPeriodValid = (uiState.periodMonths.toIntOrNull() ?: 0) > 0

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Шаг 1: Основные параметры", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.initialAmount,
                    onValueChange = { newValue ->
                        var filtered = newValue.replace(",", ".").replace(Regex("[^0-9.]"), "")
                        if (filtered.length > 1 && filtered.startsWith("0") && filtered[1] != '.') {
                            filtered = filtered.substring(1)
                        }
                        if (filtered.count { it == '.' } <= 1) {
                            viewModel.updateState(uiState.copy(initialAmount = filtered))
                        }
                    },
                    label = { Text("Стартовый взнос (> 0)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.initialAmount.isNotEmpty() && !isInitialValid
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.periodMonths,
                    onValueChange = { newValue ->
                        val filtered = newValue.replace(Regex("[^0-9]"), "").trimStart('0')
                        viewModel.updateState(uiState.copy(periodMonths = filtered))
                    },
                    label = { Text("Срок вклада (от 1 мес)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.periodMonths.isNotEmpty() && !isPeriodValid
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Кнопка очистки вместо "В начало"
                    Button(onClick = { viewModel.reset() }) {
                        Text("Очистить")
                    }
                    Button(
                        onClick = { calculatorNavController.navigate("step2") },
                        enabled = isInitialValid && isPeriodValid
                    ) {
                        Text("Далее")
                    }
                }
            }
        }

        // --- ЭТАП 2: ДОПОЛНИТЕЛЬНЫЕ ПАРАМЕТРЫ ---
        composable("step2") {
            val availableRates = viewModel.getAvailableRates()
            var expanded by remember { mutableStateOf(false) }
            val context = LocalContext.current

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Шаг 2: Ставка и пополнения", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.interestRate.isNotBlank()) "Выбрана ставка: ${uiState.interestRate}%" else "Нажмите, чтобы выбрать ставку")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (availableRates.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Укажите корректный срок на прошлом шаге") },
                                onClick = { expanded = false }
                            )
                        } else {
                            availableRates.forEach { rate ->
                                DropdownMenuItem(
                                    text = { Text("$rate%") },
                                    onClick = {
                                        viewModel.updateState(uiState.copy(interestRate = rate))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.monthlyTopUp,
                    onValueChange = { newValue ->
                        var filtered = newValue.replace(",", ".").replace(Regex("[^0-9.]"), "")
                        if (filtered.length > 1 && filtered.startsWith("0") && filtered[1] != '.') {
                            filtered = filtered.substring(1)
                        }
                        if (filtered.count { it == '.' } <= 1) {
                            viewModel.updateState(uiState.copy(monthlyTopUp = filtered))
                        }
                    },
                    label = { Text("Ежемесячное пополнение (опционально)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { calculatorNavController.popBackStack() }) { Text("Назад") }
                    Button(
                        onClick = {
                            if (uiState.interestRate.isBlank()) {
                                Toast.makeText(context, "Выберите процентную ставку!", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.calculate()
                                calculatorNavController.navigate("result")
                            }
                        }
                    ) { Text("Рассчитать") }
                }
            }
        }

        // --- ЭКРАН РЕЗУЛЬТАТА ---
        composable("result") {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Результаты расчёта", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Стартовый взнос: ${uiState.initialAmount}")
                        Text("Срок вклада (мес): ${uiState.periodMonths}")
                        Text("Процентная ставка: ${uiState.interestRate}%")
                        if (uiState.monthlyTopUp.isNotBlank()) {
                            Text("Ежемесячное пополнение: ${uiState.monthlyTopUp}")
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Итоговая сумма: ${String.format("%.2f", uiState.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                        Text("Начисленные проценты: ${String.format("%.2f", uiState.interestEarned)}", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        viewModel.saveCalculation()
                        // После сохранения скидываем стейт и возвращаемся на первый шаг
                        viewModel.reset()
                        calculatorNavController.popBackStack("step1", false)
                    }) { Text("Сохранить") }

                    Button(onClick = {
                        viewModel.reset()
                        calculatorNavController.popBackStack("step1", false)
                    }) { Text("Сбросить") }
                }
            }
        }
    }
}

// ================= ВКЛАДКА 3: ИСТОРИЯ РАСЧЁТОВ =================
@Composable
fun HistoryScreen(viewModel: DepositViewModel = koinViewModel()) {
    val history by viewModel.history.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Мои расчёты", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("У вас пока нет сохраненных расчётов")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(history.size) { index ->
                    val item = history[index]
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Дата: ${dateFormat.format(Date(item.calculationDate))}", style = MaterialTheme.typography.labelMedium)
                            Text("Взнос: ${item.initialAmount} | Ставка: ${item.interestRate}%")
                            Text("Итог: ${String.format("%.2f", item.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}