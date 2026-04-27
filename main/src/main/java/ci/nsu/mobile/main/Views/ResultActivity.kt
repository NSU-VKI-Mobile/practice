@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.Data.Database.AppDatabase
import ci.nsu.mobile.main.Data.Repository.CalculationRepository
import ci.nsu.mobile.main.ViewModels.ResultViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startAmount = intent.getDoubleExtra("START_AMOUNT", 0.0)
        val term = intent.getIntExtra("TERM", 0)
        val rate = intent.getDoubleExtra("RATE", 0.0)
        val currency = intent.getStringExtra("CURRENCY") ?: "Рубли (RUB)"

        setContent {
            PracticeTheme {
                ResultScreen(startAmount, term, rate, currency)
            }
        }
    }
}

@Composable
fun ResultScreen(startAmount: Double, term: Int, rate: Double, currency: String) {
    val context = LocalContext.current

    // Вернуться в самое начало
    fun navigateToMain() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        (context as? Activity)?.finish()
    }

    // Получаем ViewModel с репозиторием
    val repository = remember {
        CalculationRepository(
            AppDatabase.getInstance(context).calculationDao()
        )
    }
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return ResultViewModel(SavedStateHandle(), repository) as T
        }
    }
    val viewModel: ResultViewModel = viewModel(factory = factory)
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    // Инициализация данными (только при первом запуске)
    LaunchedEffect(Unit) {
        viewModel.initializeFromIntent(startAmount, term, rate, currency)
    }

    // Наблюдение за успешным сохранением
    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            // Можно показать Snackbar или тост, затем сбросить флаг
            viewModel.resetSaveSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text("Результат расчёта") }, navigationIcon = {
                IconButton(onClick = { (context as? Activity)?.finish() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
            )
        }) { innerPadding ->
        val startAmountState by viewModel.startAmount.collectAsState()
        val termState by viewModel.term.collectAsState()
        val rateState by viewModel.rate.collectAsState()
        val interestState by viewModel.interest.collectAsState()
        val totalState by viewModel.total.collectAsState()
        val currencySymbol = viewModel.getCurrencySymbol()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Карточка с результатами
            ResultCard(
                startAmount = startAmountState,
                term = termState,
                rate = rateState,
                interest = interestState,
                total = totalState,
                currencySymbol = currencySymbol,
                modifier = Modifier.weight(1f)
            )

            // Кнопки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navigateToMain() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    Text("Вернуться")
                }
                Button(
                    onClick = {
                        viewModel.saveCalculation()
                        navigateToMain()
                    }, modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    startAmount: Double,
    term: Int,
    rate: Double,
    interest: Double,
    total: Double,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Детали вклада",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()
            InfoRow("Стартовый взнос:", formatMoney(startAmount, currencySymbol))
            InfoRow("Срок вклада:", "$term месяцев")
            InfoRow("Процентная ставка:", "${String.format("%.2f", rate)}%")
            InfoRow("Начисленные проценты:", formatMoney(interest, currencySymbol))
            HorizontalDivider()
            InfoRow(
                "Итоговая сумма:",
                formatMoney(total, currencySymbol),
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = textStyle)
    }
}

private fun formatMoney(amount: Double, symbol: String): String =
    String.format("%.2f %s", amount, symbol)