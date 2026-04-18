@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.Data.Database.AppDatabase
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import ci.nsu.mobile.main.Data.Repository.CalculationRepository
import ci.nsu.mobile.main.ViewModels.HistoryViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                HistoryScreen()
            }
        }
    }
}

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val repository = remember {
        CalculationRepository(AppDatabase.getInstance(context.applicationContext).calculationDao())
    }
    val viewModel: HistoryViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
    })
    val calculations by viewModel.calculations.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет сохранённых расчётов")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations) { calculation ->
                    CalculationItem(
                        calculation = calculation,
                        onClick = {
                            val intent = android.content.Intent(context, DetailActivity::class.java).apply {
                                putExtra("calculation_id", calculation.id)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculationItem(calculation: CalculationEntity, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val dateStr = dateFormat.format(Date(calculation.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = dateStr, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "Стартовый взнос: ${String.format("%.2f", calculation.startAmount)} ${getCurrencySymbol(calculation.currency)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Итоговая сумма: ${String.format("%.2f", calculation.total)} ${getCurrencySymbol(calculation.currency)}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

private fun getCurrencySymbol(currency: String): String = when (currency) {
    "Рубли (RUB)" -> "₽"
    "Доллары (USD)" -> "$"
    "Евро (EUR)" -> "€"
    else -> ""
}