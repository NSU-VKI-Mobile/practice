package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val lazy: Any
    get() {
        return lazy
    }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DepositApp()
                }
            }
        }
    }
}

@Composable
fun DepositApp(viewModel: DepositViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "main") {

        // --- ГЛАВНЫЙ ЭКРАН ---
        composable("main") {
            val context = LocalContext.current
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Расчёт вкладов", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.reset()
                        navController.navigate("step1")
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("Рассчитать") }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("История расчётов") }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { (context as? Activity)?.finish() },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) { Text("Закрыть приложение") }
            }
        }

        // --- ЭТАП 1: ОСНОВНЫЕ ПАРАМЕТРЫ ---
        composable("step1") {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Шаг 1: Основные параметры", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.initialAmount,
                    onValueChange = { viewModel.updateState(uiState.copy(initialAmount = it)) },
                    label = { Text("Стартовый взнос") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.periodMonths,
                    onValueChange = { viewModel.updateState(uiState.copy(periodMonths = it)) },
                    label = { Text("Срок вклада (в месяцах)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.popBackStack("main", false) }) {
                        Text("В начало")
                    }
                    Button(
                        onClick = { navController.navigate("step2") },
                        enabled = uiState.initialAmount.isNotBlank() && uiState.periodMonths.isNotBlank()
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

                // выпадающий список
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (uiState.interestRate.isNotBlank()) "Выбрана ставка: ${uiState.interestRate}%" else "Нажмите, чтобы выбрать ставку"
                        )
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
                    onValueChange = { viewModel.updateState(uiState.copy(monthlyTopUp = it)) },
                    label = { Text("Ежемесячное пополнение (опционально)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.popBackStack() }) { Text("Назад") }
                    Button(
                        onClick = {
                            if (uiState.interestRate.isBlank()) {
                                Toast.makeText(context, "Выберите процентную ставку!", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.calculate()
                                navController.navigate("result")
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
                        navController.popBackStack("main", false)
                    }) { Text("Сохранить") }

                    Button(onClick = { navController.popBackStack("main", false) }) { Text("В начало") }
                }
            }
        }

        // --- ИСТОРИЯ РАСЧЕТОВ ---
        composable("history") {
            val history by viewModel.history.collectAsState(initial = emptyList())
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("История расчётов", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                if (history.isEmpty()) {
                    Text("История пуста")
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(history.size) { index ->
                            val item = history[index]
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Дата: ${dateFormat.format(Date(item.calculationDate))}", style = MaterialTheme.typography.labelMedium)
                                    Text("Взнос: ${item.initialAmount} | Ставка: ${item.interestRate}%")
                                    Text("Итог: ${String.format("%.2f", item.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Назад") }
            }
        }
    }
}