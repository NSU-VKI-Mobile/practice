package ci.nsu.moble.main.presentation.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.presentation.viewmodels.DetailViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Long, onBack: () -> Unit) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val detailViewModel: DetailViewModel = viewModel(factory = DetailViewModel.factory(application))
    val calculation by detailViewModel.calculation.collectAsState()

    LaunchedEffect(id) {
        if (id > 0) detailViewModel.loadCalculation(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        calculation?.let { calc ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text("Дата: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(calc.calculationDate))}")
                Text("Взнос: ${"%.2f".format(calc.initialAmount)}")
                Text("Срок: ${calc.periodMonths} мес.")
                Text("Ставка: ${calc.interestRate} %")
                calc.monthlyTopUp?.let { Text("Пополнение: ${"%.2f".format(it)}") }
                Text("Итог: ${"%.2f".format(calc.finalAmount)}")
                Text("Проценты: ${"%.2f".format(calc.interestEarned)}")
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}