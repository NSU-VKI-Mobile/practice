package ci.nsu.moble.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ci.nsu.moble.main.DepositViewModel
import ci.nsu.moble.main.Navigation
import java.text.DecimalFormat

// Форматирование чисел: 12345.6 → 12 345.60
private val fmt = DecimalFormat("#,##0.00")

@Composable
fun ResultScreen(nav: NavController, vm: DepositViewModel) {
    val calc = vm.result ?: return
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Результат расчёта", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultRow("Стартовый взнос",      "${fmt.format(calc.initialAmount)} ₽")
                ResultRow("Срок вклада",          "${calc.periodMonths} мес.")
                ResultRow("Процентная ставка",    "${calc.interestRate}%")
                if (calc.monthlyTopUp > 0) {
                    ResultRow("Ежемес. пополнение", "${fmt.format(calc.monthlyTopUp)} ₽")
                }
                HorizontalDivider()
                ResultRow("Итоговая сумма",       "${fmt.format(calc.finalAmount)} ₽",    bold = true)
                ResultRow("Начисленные проценты", "${fmt.format(calc.interestEarned)} ₽", bold = true)
            }
        }
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { vm.save(); saved = true },
            enabled = !saved,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (saved) "Сохранено ✓" else "Сохранить")
        }
        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { nav.navigate(Navigation.MAIN) { popUpTo(Navigation.MAIN) { inclusive = true } } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

// Строка "Название — Значение"
@Composable
private fun ResultRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}