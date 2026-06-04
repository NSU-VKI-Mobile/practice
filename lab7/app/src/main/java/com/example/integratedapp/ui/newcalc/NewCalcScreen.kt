package com.example.integratedapp.ui.newcalc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integratedapp.di.ServiceLocator
import java.text.NumberFormat
import java.util.Locale

private fun formatMoney(amount: Double): String {
    val fmt = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    fmt.minimumFractionDigits = 2; fmt.maximumFractionDigits = 2
    return "${fmt.format(amount)} ₽"
}

@Composable
fun NewCalcScreen(
    viewModel: NewCalcViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // В зависимости от текущего шага показываем нужный экран
    when (uiState.currentStep) {
        CalcStep.STEP1 -> Step1(uiState, viewModel)
        CalcStep.STEP2 -> Step2(uiState, viewModel)
        CalcStep.RESULT -> Result(uiState, viewModel)
    }
}

@Composable
private fun Step1(uiState: NewCalcUiState, viewModel: NewCalcViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Шаг 1: основные параметры", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = uiState.initialAmountText,
            onValueChange = viewModel::onInitialAmountChanged,
            label = { Text("Стартовый взнос (₽)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.periodMonthsText,
            onValueChange = viewModel::onPeriodChanged,
            label = { Text("Срок (месяцев)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.goToStep2() },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Далее") }
    }
}

@Composable
private fun Step2(uiState: NewCalcUiState, viewModel: NewCalcViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Шаг 2: дополнительные параметры", style = MaterialTheme.typography.titleMedium)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Процентная ставка", style = MaterialTheme.typography.labelLarge)
                Text("${uiState.selectedRate}% годовых",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
            }
        }

        OutlinedTextField(
            value = uiState.monthlyTopUpText,
            onValueChange = viewModel::onMonthlyTopUpChanged,
            label = { Text("Ежем. пополнение (необязательно)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { viewModel.goBackToStep1() },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Назад") }
            Button(
                onClick = { viewModel.calculate() },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Рассчитать") }
        }
    }
}

@Composable
private fun Result(uiState: NewCalcUiState, viewModel: NewCalcViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Результат", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                ResultRow("Стартовый взнос", formatMoney(uiState.initialAmountText.toDoubleOrNull() ?: 0.0))
                ResultRow("Срок", "${uiState.periodMonthsText} мес.")
                ResultRow("Ставка", "${uiState.selectedRate}%")
                uiState.monthlyTopUpText.toDoubleOrNull()?.let { if (it > 0)
                    ResultRow("Ежем. пополнение", formatMoney(it)) }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ResultRow("Итог", formatMoney(uiState.finalAmount), highlight = true)
                ResultRow("Начислено %", formatMoney(uiState.interestEarned))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isSaved) {
            Text("✓ Расчёт сохранён", color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { viewModel.goBackToStep2() },
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text("Назад") }
            Button(
                onClick = { viewModel.save() },
                enabled = !uiState.isSaved,
                modifier = Modifier.weight(1f).height(52.dp)
            ) { Text(if (uiState.isSaved) "Сохранено" else "Сохранить") }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { viewModel.reset() }, modifier = Modifier.fillMaxWidth()) {
            Text("Новый расчёт")
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, highlight: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal)
        Text(value, fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal)
    }
}
