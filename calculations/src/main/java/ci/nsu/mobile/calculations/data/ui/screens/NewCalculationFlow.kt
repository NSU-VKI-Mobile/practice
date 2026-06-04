package ci.nsu.mobile.calculations.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.calculations.ui.DepositViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewCalculationFlow(vm: DepositViewModel) {
    when (vm.currentStep) {
        0 -> CalcStepOne(vm)
        1 -> CalcStepTwo(vm)
        2 -> CalcResult(vm)
    }
}

@Composable
private fun CalcStepOne(vm: DepositViewModel) {
    Column(
        Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Шаг 1: Параметры вклада", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = vm.startAmount,
            onValueChange = { vm.startAmount = it },
            label = { Text("Стартовый взнос (₽)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = vm.months,
            onValueChange = { vm.months = it },
            label = { Text("Срок (месяцы)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { vm.goToStep(1) },
            enabled = vm.startAmount.isNotBlank() && vm.months.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Далее") }
    }
}

@Composable
private fun CalcStepTwo(vm: DepositViewModel) {
    val rates = vm.resolveRate()

    Column(
        Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Шаг 2: Дополнительно", style = MaterialTheme.typography.titleMedium)

        if (rates.isEmpty()) {
            Text(
                "Введите корректный срок на шаге 1", color = MaterialTheme.colorScheme.error
            )
        } else {
            Text("Выберите процентную ставку:")
            rates.forEach { r ->
                OutlinedButton(
                    onClick = { vm.rate = r },
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (vm.rate == r) ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    else ButtonDefaults.outlinedButtonColors()
                ) { Text("$r %") }
            }
        }

        OutlinedTextField(
            value = vm.monthlyTopUp,
            onValueChange = { vm.monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение (₽)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { vm.goToStep(0) }, modifier = Modifier.weight(1f)
            ) { Text("Назад") }

            Button(
                onClick = { vm.calculateDeposit() }, enabled = vm.rate > 0, modifier = Modifier.weight(1f)
            ) { Text("Рассчитать") }
        }
    }
}

@Composable
private fun CalcResult(vm: DepositViewModel) {
    val res = vm.result ?: return
    val fmt = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    Column(
        Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Результат расчёта", style = MaterialTheme.typography.titleMedium)

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Начальный взнос: ${res.startAmount} ₽")
                Text("Срок: ${res.months} мес.")
                Text("Ставка: ${res.rate} %")
                Text("Пополнение: ${res.monthlyTopUp ?: 0.0} ₽/мес.")
                HorizontalDivider()
                Text(
                    "Итоговая сумма: ${String.format("%.2f", res.finalAmount)} ₽",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Прибыль: ${String.format("%.2f", res.profit)} ₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Дата: ${fmt.format(Date(res.date))}", style = MaterialTheme.typography.labelSmall
                )
            }
        }

        vm.savedMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { vm.saveDeposit() }, enabled = vm.savedMessage == null, modifier = Modifier.fillMaxWidth()
        ) { Text("Сохранить") }

        OutlinedButton(
            onClick = { vm.resetCalculation() }, modifier = Modifier.fillMaxWidth()
        ) { Text("Новый расчёт") }
    }
}