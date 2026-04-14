package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HistoryScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val viewModel: CalculationViewModel = viewModel()
    val calculations by viewModel.calculations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedCalculation by remember { mutableStateOf<Calculation?>(null) }

    // Загружаем данные при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadCalculations()
    }

    // Показываем ошибки
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Если выбран расчёт - показываем детали
    if (selectedCalculation != null) {
        CalculationDetailScreen(
            calculation = selectedCalculation!!,
            onBack = { selectedCalculation = null },
            onDelete = {
                viewModel.deleteCalculation(it)
                selectedCalculation = null
            }
        )
    } else {
        // Иначе показываем список
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("История расчётов", fontSize = 18.sp, color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                    navigationIcon = {
                        IconButton(onClick = { (context as? HistoryActivity)?.finish() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    calculations.isEmpty() -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📭", fontSize = 64.sp)
                            Text("Нет сохранённых расчётов", fontSize = 18.sp, color = Color.Gray)
                            Text("Сделайте расчёт и нажмите 'Сохранить'", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(calculations) { calculation ->
                                CalculationCard(
                                    calculation = calculation,
                                    onClick = { selectedCalculation = calculation }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculationCard(calculation: Calculation, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(calculation.date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(dateFormat.format(date), fontSize = 14.sp, color = Color.Gray)
                Text("${calculation.interestRate}%", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Стартовый взнос:")
                Text(String.format("%.2f ₽", calculation.startAmount))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Итоговая сумма:", color = MaterialTheme.colorScheme.primary)
                Text(
                    String.format("%.2f ₽", calculation.totalAmount),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationDetailScreen(
    calculation: Calculation,
    onBack: () -> Unit,
    onDelete: (Calculation) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val date = Date(calculation.date)
    val context = LocalContext.current
    val viewModel: CalculationViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта", fontSize = 18.sp, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onDelete(calculation)
                            Toast.makeText(context, "Расчёт удалён", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Информация о расчёте", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailRow("Дата расчёта:", dateFormat.format(date))
                    HorizontalDivider()
                    DetailRow("Стартовый взнос:", String.format("%.2f ₽", calculation.startAmount))
                    DetailRow("Срок вклада:", "${calculation.termMonths} месяцев")
                    DetailRow("Процентная ставка:", "${calculation.interestRate}%")
                    if (calculation.monthlyDeposit > 0) {
                        DetailRow("Ежемесячное пополнение:", String.format("%.2f ₽", calculation.monthlyDeposit))
                    }
                    HorizontalDivider()
                    DetailRow("Итоговая сумма:", String.format("%.2f ₽", calculation.totalAmount), isTotal = true)
                    DetailRow("Общий доход:", String.format("%.2f ₽", calculation.totalProfit), isTotal = true)
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Назад к списку")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = if (isTotal) 18.sp else 16.sp,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Text(
            value,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}