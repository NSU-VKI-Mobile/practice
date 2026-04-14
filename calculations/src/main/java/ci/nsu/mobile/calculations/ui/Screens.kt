package ci.nsu.mobile.calculations.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: DepositViewModel) {
    val historyList by viewModel.history.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(historyList) { item ->
                var isExpanded by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { isExpanded = !isExpanded }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Дата: ${dateFormat.format(Date(item.calculationDate))}", style = MaterialTheme.typography.labelSmall)
                        Text("Стартовый взнос: ${item.initialAmount}")
                        Text("Итог: ${String.format(Locale.US, "%.2f", item.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                        if (isExpanded) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("Срок вклада: ${item.periodMonths} мес.")
                            Text("Ставка: ${item.interestRate}%")
                            Text("Пополнение: ${item.monthlyTopUp}/мес.")
                            Text("Начисленные проценты: ${String.format(Locale.US, "%.2f", item.interestEarned)}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepOneScreen(navController: NavController, viewModel: DepositViewModel) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Шаг 1: Основные параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.initialAmount, onValueChange = { viewModel.initialAmount = it }, label = { Text("Стартовый взнос") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.periodMonths, onValueChange = { viewModel.periodMonths = it }, label = { Text("Срок вклада (в месяцах)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            if (viewModel.initialAmount.isBlank() || viewModel.periodMonths.isBlank()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.interestRate = viewModel.determineInterestRate()
                navController.navigate("step_two")
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Далее") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoScreen(navController: NavController, viewModel: DepositViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val currentRate = viewModel.interestRate.toString()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Шаг 2: Доп. параметры", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(value = "$currentRate%", onValueChange = {}, readOnly = true, label = { Text("Доступная процентная ставка") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("$currentRate%") }, onClick = { expanded = false })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = viewModel.monthlyTopUp, onValueChange = { viewModel.monthlyTopUp = it }, label = { Text("Ежемесячное пополнение") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Назад") }
            Button(onClick = {
                viewModel.monthlyTopUp = viewModel.monthlyTopUp.ifBlank { "0" }
                viewModel.calculateResult()
                navController.navigate("result")
            }) { Text("Рассчитать") }
        }
    }
}

@Composable
fun ResultScreen(navController: NavController, viewModel: DepositViewModel) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Результат", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Стартовый взнос: ${viewModel.initialAmount}")
                Text("Срок: ${viewModel.periodMonths} мес.")
                Text("Ставка: ${viewModel.interestRate}%")
                Text("Пополнение: ${viewModel.monthlyTopUp.ifBlank { "0" }}/мес.")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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
                navController.popBackStack("step_one", inclusive = false)
            }) { Text("Сохранить") }
            OutlinedButton(onClick = {
                viewModel.clearData()
                navController.popBackStack("step_one", inclusive = false)
            }) { Text("Сбросить") }
        }
    }
}