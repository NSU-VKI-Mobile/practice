@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.ViewModels.ResultViewModel
import ci.nsu.mobile.main.Views.ui.theme.PracticeTheme

class ResultActivity : ComponentActivity() {

    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Получаем данные из Intent
        val startAmount = intent.getDoubleExtra("START_AMOUNT", 0.0)
        val term = intent.getIntExtra("TERM", 0)
        val rate = intent.getDoubleExtra("RATE", 0.0)
        val currency = intent.getStringExtra("CURRENCY") ?: "Рубли (RUB)"

        // Создаём ViewModel с фабрикой
        viewModel = ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this)
        )[ResultViewModel::class.java]

        // Инициализируем ViewModel (только при первом запуске)
        viewModel.initializeFromIntent(startAmount, term, rate, currency)

        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Результат расчёта") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    // Подписываемся на состояния
                    val startAmount by viewModel.startAmount.collectAsState()
                    val term by viewModel.term.collectAsState()
                    val rate by viewModel.rate.collectAsState()
                    val interest by viewModel.interest.collectAsState()
                    val total by viewModel.total.collectAsState()
                    val currencySymbol = viewModel.getCurrencySymbol()

                    ResultCard(
                        modifier = Modifier.padding(innerPadding),
                        startAmount = startAmount,
                        term = term,
                        rate = rate,
                        interest = interest,
                        total = total,
                        currencySymbol = currencySymbol
                    )
                }
            }
        }
    }

    @Composable
    fun ResultCard(
        modifier: Modifier = Modifier,
        startAmount: Double,
        term: Int,
        rate: Double,
        interest: Double,
        total: Double,
        currencySymbol: String
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
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
                Divider()
                InfoRow("Стартовый взнос:", formatMoney(startAmount, currencySymbol))
                InfoRow("Срок вклада:", "$term месяцев")
                InfoRow("Процентная ставка:", "${String.format("%.2f", rate)}%")
                InfoRow("Начисленные проценты:", formatMoney(interest, currencySymbol))
                Divider()
                InfoRow(
                    "Итоговая сумма:",
                    formatMoney(total, currencySymbol),
                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }

    @Composable
    fun InfoRow(label: String, value: String, textStyle: TextStyle = MaterialTheme.typography.bodyLarge) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(text = value, style = textStyle)
        }
    }

    private fun formatMoney(amount: Double, symbol: String): String {
        return String.format("%.2f %s", amount, symbol)
    }
}