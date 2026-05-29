package ci.nsu.mobile.main.ui

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositApp(viewModel: DepositViewModel) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- ГЛАВНЫЙ ЭКРАН ---
            composable("main") {
                val context = LocalContext.current
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { navController.navigate("step1") }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Рассчитать")
                    }
                    Button(onClick = { navController.navigate("history") }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("История расчётов")
                    }
                    OutlinedButton(onClick = { (context as? Activity)?.finish() }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Закрыть приложение")
                    }
                }
            }

            // --- ЭТАП 1: ОСНОВНЫЕ ПАРАМЕТРЫ ---
            composable("step1") {
                var showError by remember { mutableStateOf(false) }

                Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Этап 1: Основные параметры", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

                    OutlinedTextField(
                        value = viewModel.initialAmount,
                        onValueChange = { viewModel.initialAmount = it },
                        label = { Text("Стартовый взнос (руб.)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = viewModel.periodMonths,
                        onValueChange = {
                            viewModel.periodMonths = it
                            viewModel.updateRateBasedOnPeriod()
                        },
                        label = { Text("Срок вклада (в месяцах)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (showError) {
                        Text("Заполните все обязательные поля!", color = MaterialTheme.colorScheme.error)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = {
                            viewModel.resetInputs()
                            navController.navigate("main") { popUpTo("main") { inclusive = true } }
                        }) { Text("В начало") }

                        Button(onClick = {
                            if (viewModel.initialAmount.isBlank() || viewModel.periodMonths.isBlank()) {
                                showError = true
                            } else {
                                showError = false
                                navController.navigate("step2")
                            }
                        }) { Text("Далее") }
                    }
                }
            }

            // --- ЭТАП 2: ДОПОЛНИТЕЛЬНЫЕ ПАРАМЕТРЫ ---
            composable("step2") {
                val periodCheck = viewModel.periodMonths.toIntOrNull()

                Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Этап 2: Дополнительные параметры", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

                    if (periodCheck == null) {
                        Text("Предупреждение: Не указан корректный срок вклада на прошлом шаге!", color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Доступная процентная ставка для вашего срока ($periodCheck мес.):", fontWeight = FontWeight.Medium)
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                            Text("${viewModel.selectedRate}% годовых", modifier = Modifier.padding(16.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedTextField(
                        value = viewModel.monthlyTopUp,
                        onValueChange = { viewModel.monthlyTopUp = it },
                        label = { Text("Ежемесячное пополнение (необязательно)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { navController.popBackStack() }) { Text("Назад") }

                        Button(
                            enabled = periodCheck != null,
                            onClick = {
                                viewModel.calculateDeposit()
                                navController.navigate("result")
                            }
                        ) { Text("Рассчитать") }
                    }
                }
            }

            // --- ЭКРАН РЕЗУЛЬТАТА ---
            composable("result") {
                val res = viewModel.currentResult
                Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Результат расчёта", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                    if (res != null) {
                        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Стартовый взнос: ${res.initialAmount} руб.")
                                Text("Срок вклада: ${res.periodMonths} мес.")
                                Text("Процентная ставка: ${res.interestRate}%")
                                if (res.monthlyTopUp > 0) Text("Ежемесячное пополнение: ${res.monthlyTopUp} руб.")
                                HorizontalDivider()
                                Text("Начисленные проценты: ${res.interestEarned} руб.", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text("Итоговая сумма: ${res.finalAmount} руб.", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    var isSaved by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            viewModel.saveResultToDb()
                            isSaved = true
                        },
                        enabled = !isSaved,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isSaved) "Сохранено" else "Сохранить")
                    }

                    OutlinedButton(onClick = {
                        viewModel.resetInputs()
                        navController.navigate("main") { popUpTo("main") { inclusive = true } }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("В начало")
                    }
                }
            }

            // --- ЭКРАН ИСТОРИИ РАСЧЁТОВ ---
            composable("history") {
                val historyList by viewModel.historyState.collectAsState()

                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("История расчётов", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

                    // Box с весом занимает всё доступное пространство, сдвигая кнопку вниз
                    Box(modifier = Modifier.weight(1f)) {
                        if (historyList.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("История пуста")
                            }
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
                                items(historyList) { item ->
                                    val dateStr = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(item.calculationDate))
                                    Card(
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            viewModel.selectedHistoryItem = item
                                            navController.navigate("detail")
                                        },
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Дата: $dateStr", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                                            Text("Взнос: ${item.initialAmount} руб.", fontWeight = FontWeight.Medium)
                                            Text("Итог: ${item.finalAmount} руб.", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Добавленная кнопка возврата на главный экран
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Назад в меню")
                    }
                }
            }

            // --- ДЕТАЛЬНЫЙ ЭКРАН ИСТОРИИ ---
            composable("detail") {
                val item = viewModel.selectedHistoryItem
                Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Детали расчёта", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                    if (item != null) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Стартовый взнос: ${item.initialAmount} руб.")
                                Text("Срок вклада: ${item.periodMonths} мес.")
                                Text("Ставка: ${item.interestRate}%")
                                Text("Ежемесячный долив: ${item.monthlyTopUp} руб.")
                                Text("Заработано на %: ${item.interestEarned} руб.")
                                Text("Итоговый баланс: ${item.finalAmount} руб.", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                    Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Назад к списку")
                    }
                }
            }
        }
    }
}
