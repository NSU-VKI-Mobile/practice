package ci.nsu.mobile.main.ui

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material3.ExperimentalMaterial3Api
import java.util.Locale
import java.util.Date
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.compose.ui.text.style.TextAlign


// Экраны навигации
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Step1 : Screen("step1")
    object Step2 : Screen("step2")
    object Result : Screen("result")
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail/{id}") {
        fun createRoute(id: Long) = "history_detail/$id"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) { MainScreen(navController) }
        composable(Screen.Step1.route) { Step1Screen(navController) }
        composable(Screen.Step2.route) { Step2Screen(navController) }
        composable(Screen.Result.route) { ResultScreen(navController) }
        composable(Screen.History.route) { HistoryScreen(navController) }
        composable(
            route = Screen.HistoryDetail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: return@composable
            HistoryDetailScreen(navController, id)
        }
    }
}

// ===== ГЛАВНЫЙ ЭКРАН =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    Scaffold(
        topBar = { TopAppBar(title = { Text("Расчёт вкладов") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Step1.route) },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Рассчитать") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.History.route) },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("История расчётов") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { (context as? Activity)?.finish() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Закрыть приложение") }
        }
    }
}

// ===== ЭТАП 1: Основные параметры =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1Screen(navController: NavHostController, viewModel: MainViewModel = viewModel()) {
    val input by viewModel.input.collectAsState()
    val error by viewModel.error.collectAsState()

    // ✅ Выносим в локальные переменные для стабильного доступа
    val initialAmount = input.initialAmount
    val periodMonths = input.periodMonths

    Scaffold(
        topBar = { TopAppBar(title = { Text("Параметры вклада (1/2)") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = input.initialAmount?.toString() ?: "",
                onValueChange = { viewModel.updateInitialAmount(it.toDoubleOrNull()) },
                label = { Text("Стартовый взнос (₽)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = error != null && input.initialAmount != null
            )

            OutlinedTextField(
                value = input.periodMonths?.toString() ?: "",
                onValueChange = { viewModel.updatePeriodMonths(it.toIntOrNull()) },
                label = { Text("Срок вклада (месяцев)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = error != null && input.periodMonths != null
            )

            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        viewModel.clearInput()
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("В начало") }

                Button(
                    onClick = {
                        navController.navigate(Screen.Step2.route)
                    },
                    enabled = (initialAmount != null && initialAmount > 0) &&
                            (periodMonths != null && periodMonths > 0),
                    modifier = Modifier.weight(1f)
                ) { Text("Далее") }
            }
        }
    }
}
// ===== ЭТАП 2: Дополнительные параметры =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(navController: NavHostController, viewModel: MainViewModel = viewModel()) {
    val input by viewModel.input.collectAsState()
    val period = input.periodMonths ?: 0
    val availableRate = viewModel.getInterestRate(period)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Параметры вклада (2/2)") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Выбор процентной ставки (только отображение + пояснение)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Процентная ставка:", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = when {
                            period <= 0 -> "⚠️ Укажите срок вклада"
                            period < 6 -> "✓ 15% годовых (срок < 6 мес.)"
                            period < 12 -> "✓ 10% годовых (6–12 мес.)"
                            else -> "✓ 5% годовых (≥ 12 мес.)"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            OutlinedTextField(
                value = input.monthlyTopUp?.toString() ?: "",
                onValueChange = { viewModel.updateMonthlyTopUp(it.toDoubleOrNull()) },
                label = { Text("Ежемесячное пополнение (₽, необязательно)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) { Text("Назад") }

                Button(
                    onClick = {
                        if (viewModel.calculateResult()) {
                            navController.navigate(Screen.Result.route)
                        }
                    },
                    enabled = period > 0,
                    modifier = Modifier.weight(1f)
                ) { Text("Рассчитать") }
            }
        }
    }
}

// ===== ЭКРАН РЕЗУЛЬТАТА =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavHostController, viewModel: MainViewModel = viewModel()) {
    val result by viewModel.result.collectAsState()
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    var showSaveDialog by remember { mutableStateOf(false) }

    result?.let { res ->
        Scaffold(
            topBar = { TopAppBar(title = { Text("Результат расчёта") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📊 Детали расчёта", style = MaterialTheme.typography.titleLarge)
                        Divider()
                        ResultRow("Стартовый взнос:", "${"%.2f".format(res.initialAmount)} ₽")
                        ResultRow("Срок вклада:", "${res.periodMonths} мес.")
                        ResultRow("Процентная ставка:", "${"%.0f".format(res.interestRate * 100)}%")
                        res.monthlyTopUp?.let {
                            ResultRow("Ежемесячное пополнение:", "${"%.2f".format(it)} ₽")
                        }
                        Divider()
                        ResultRow("💰 Итоговая сумма:", "${"%.2f".format(res.finalAmount)} ₽", bold = true)
                        ResultRow("📈 Начисленные проценты:", "${"%.2f".format(res.interestEarned)} ₽", bold = true)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            viewModel.saveCalculation()
                            showSaveDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Сохранить") }

                    Button(
                        onClick = {
                            viewModel.clearInput()
                            navController.popBackStack(Screen.Main.route, inclusive = false)
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("В начало") }
                }
            }
        }

        // Диалог сохранения
        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text("Успешно!") },
                text = { Text("Расчёт сохранён в истории") },
                confirmButton = {
                    Button(onClick = { showSaveDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = if (bold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium)
        Text(value, style = if (bold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium)
    }
}

// ===== ИСТОРИЯ РАСЧЁТОВ =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavHostController, viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val repository = remember {
        DepositRepository(
            ci.nsu.mobile.main.data.AppDatabase.getDatabase(
                context.applicationContext
            ).depositDao()
        )
    }

    val calculations by repository.allCalculations.collectAsState(initial = emptyList())
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("История пуста", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations) { calc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(Screen.HistoryDetail.createRoute(calc.id)) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = dateFormat.format(Date(calc.calculationDate)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Взнос: ${"%.0f".format(calc.initialAmount)} ₽", style = MaterialTheme.typography.bodyLarge)
                            Text("Итого: ${"%.0f".format(calc.finalAmount)} ₽", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
// ===== ДЕТАЛИ ИСТОРИИ =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(navController: NavHostController, calculationId: Long) {
    val context = LocalContext.current
    val repository = remember {
        DepositRepository(
            ci.nsu.mobile.main.data.AppDatabase.getDatabase(context.applicationContext).depositDao()
        )
    }

    var calculation by remember { mutableStateOf<DepositCalculation?>(null) }
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    LaunchedEffect(calculationId) {
        calculation = repository.getCalculationById(calculationId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { Text("←") }
                }
            )
        }
    ) { padding ->
        calculation?.let { calc ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("📅 Дата: ${dateFormat.format(Date(calc.calculationDate))}", style = MaterialTheme.typography.bodyMedium)
                Divider()
                ResultRow("Стартовый взнос:", "${"%.2f".format(calc.initialAmount)} ₽")
                ResultRow("Срок вклада:", "${calc.periodMonths} мес.")
                ResultRow("Процентная ставка:", "${"%.0f".format(calc.interestRate * 100)}%")
                calc.monthlyTopUp?.let {
                    ResultRow("Ежемесячное пополнение:", "${"%.2f".format(it)} ₽")
                }
                Divider()
                ResultRow("💰 Итоговая сумма:", "${"%.2f".format(calc.finalAmount)} ₽", bold = true)
                ResultRow("📈 Начисленные проценты:", "${"%.2f".format(calc.interestEarned)} ₽", bold = true)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Закрыть") }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}