package com.example.depositcalculator.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Long,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val calculation by viewModel.calculation.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    LaunchedEffect(id) {
        viewModel.loadCalculation(id)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Детали расчёта") }) }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            if (calculation == null) {
                CircularProgressIndicator(Modifier.fillMaxSize())
            } else {
                val calc = calculation!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Дата: ${dateFormat.format(Date(calc.calculationDate))}")
                        Text("Стартовый взнос: ${calc.initialAmount} ₽")
                        Text("Срок: ${calc.periodMonths} мес.")
                        Text("Процентная ставка: ${calc.interestRate}%")
                        if (calc.monthlyTopUp != null && calc.monthlyTopUp > 0)
                            Text("Ежемесячное пополнение: ${calc.monthlyTopUp} ₽")
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                        Text("Итоговая сумма: ${String.format("%.2f", calc.finalAmount)} ₽")
                        Text("Начисленные проценты: ${String.format("%.2f", calc.interestEarned)} ₽")
                    }
                }
            }
        }
    }
}