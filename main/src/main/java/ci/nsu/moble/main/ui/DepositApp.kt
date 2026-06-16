package ci.nsu.mobile.main.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DepositApp() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("step_one") { StepOneScreen(navController, viewModel) }
        composable("step_two") { StepTwoScreen(navController, viewModel) }
        composable("result") { ResultScreen(navController, viewModel) }
        composable("history") { HistoryScreen(navController, viewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current as Activity

    Scaffold(
        topBar = { TopAppBar(title = { Text("Расчёт вкладов") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigate("step_one") }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Text("Рассчитать")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("history") }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Text("История расчётов")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { context.finish() }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Text("Закрыть приложение")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOneScreen(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    Scaffold(topBar = { TopAppBar(title = { Text("Шаг 1: Основные параметры") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = viewModel.initialAmount,
                onValueChange = { viewModel.initialAmount = it },
                label = { Text("Стартовый взнос") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.periodMonths,
                onValueChange = { viewModel.periodMonths = it },
                label = { Text("Срок вклада (в месяцах)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { navController.popBackStack("home", false) }) {
                    Text("В начало")
                }
                Button(onClick = {
                    if (viewModel.initialAmount.isBlank() || viewModel.periodMonths.isBlank()) {
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.interestRate = viewModel.determineInterestRate()
                        navController.navigate("step_two")
                    }
                }) {
                    Text("Далее")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoScreen(navController: NavController, viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }

    val currentRate = viewModel.interestRate.toString()

    Scaffold(topBar = { TopAppBar(title = { Text("Шаг 2: Доп. параметры") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = "$currentRate%",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Доступная процентная ставка") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("$currentRate%") },
                        onClick = { expanded = false }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.monthlyTopUp,
                onValueChange = { viewModel.monthlyTopUp = it },
                label = { Text("Ежемесячное пополнение (необязательно)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("Назад")
                }
                Button(onClick = {
                    viewModel.monthlyTopUp = viewModel.monthlyTopUp.ifBlank { "0" }
                    viewModel.calculateResult()
                    navController.navigate("result")
                }) {
                    Text("Рассчитать")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    Scaffold(topBar = { TopAppBar(title = { Text("Результат") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Стартовый взнос: ${viewModel.initialAmount}")
                    Text("Срок: ${viewModel.periodMonths} мес.")
                    Text("Ставка: ${viewModel.interestRate}%")
                    Text("Пополнение: ${viewModel.monthlyTopUp.ifBlank { "0" }}/мес.")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Начисленные проценты: ${String.format("%.2f", viewModel.interestEarned)}")
                    Text("Итоговая сумма: ${String.format("%.2f", viewModel.finalAmount)}", style = MaterialTheme.typography.titleLarge)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    viewModel.saveCalculation()
                    Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
                    viewModel.clearData()
                    navController.popBackStack("home", false)
                }) {
                    Text("Сохранить")
                }
                OutlinedButton(onClick = {
                    viewModel.clearData()
                    navController.popBackStack("home", false)
                }) {
                    Text("В начало")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: MainViewModel) {
    val historyList by viewModel.history.observeAsState(emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(historyList) { item ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { isExpanded = !isExpanded }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Дата: ${dateFormat.format(Date(item.calculationDate))}", style = MaterialTheme.typography.labelSmall)
                            Text("Стартовый взнос: ${item.initialAmount}")
                            Text("Итог: ${String.format(Locale.US, "%.2f", item.finalAmount)}", style = MaterialTheme.typography.titleMedium)

                            if (isExpanded) {
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                Text("Срок вклада: ${item.periodMonths} мес.")
                                Text("Ставка: ${item.interestRate}%")
                                Text("Пополнение: ${item.monthlyTopUp}/мес.")
                                Text("Начисленные проценты: ${String.format(Locale.US, "%.2f", item.interestEarned)}")
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { navController.popBackStack("home", inclusive = false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Вернуться на главную")
            }
        }
    }
}