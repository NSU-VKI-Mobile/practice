@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.Data.Database.AppDatabase
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import ci.nsu.mobile.main.Data.Repository.CalculationRepository
import ci.nsu.mobile.main.ViewModels.DetailViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val calculationId = intent.getLongExtra("calculation_id", -1)
        setContent {
            PracticeTheme {
                DetailScreen(calculationId)
            }
        }
    }
}

@Composable
fun DetailScreen(calculationId: Long) {
    val context = LocalContext.current
    val repository = remember {
        CalculationRepository(AppDatabase.getInstance(context.applicationContext).calculationDao())
    }
    val viewModel: DetailViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return DetailViewModel(repository, calculationId) as T
        }
    })
    val calculation by viewModel.calculation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали расчёта") }, navigationIcon = {
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
        calculation?.let { calc ->
            DetailCard(
                calculation = calc,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Расчёт не найден")
            }
        }
    }
}

@Composable
fun DetailCard(calculation: CalculationEntity, modifier: Modifier = Modifier) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val dateStr = dateFormat.format(Date(calculation.timestamp))

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Дата расчёта",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = dateStr, style = MaterialTheme.typography.bodyLarge)

            HorizontalDivider()

            InfoRow("Стартовый взнос:", formatMoney(calculation.startAmount, calculation.currency))
            InfoRow("Срок вклада:", "${calculation.termMonths} месяцев")
            InfoRow("Процентная ставка:", "${String.format("%.2f", calculation.rate)}%")
            InfoRow(
                "Начисленные проценты:",
                formatMoney(calculation.interest, calculation.currency)
            )
            HorizontalDivider()
            InfoRow(
                "Итоговая сумма:",
                formatMoney(calculation.total, calculation.currency),
                textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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

private fun formatMoney(amount: Double, currency: String): String {
    val symbol = when (currency) {
        "Рубли (RUB)" -> "₽"
        "Доллары (USD)" -> "$"
        "Евро (EUR)" -> "€"
        else -> ""
    }
    return String.format("%.2f %s", amount, symbol)
}