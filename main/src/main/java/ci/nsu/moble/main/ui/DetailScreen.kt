package ci.nsu.moble.main.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ci.nsu.moble.main.DepositViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val fmt     = DecimalFormat("#,##0.00")
private val dateFmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

@Composable
fun DetailScreen(nav: NavController, vm: DepositViewModel, id: Long) {
    // LaunchedEffect — запускается один раз при открытии экрана
    LaunchedEffect(id) { vm.loadById(id) }

    val calc = vm.selectedDeposit ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Детали расчёта", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ResultRow("Дата",                 dateFmt.format(Date(calc.date)))
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
            onClick = {
                vm.deleteById(id)
                nav.navigateUp()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Удалить")
        }

        OutlinedButton(
            onClick = { nav.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }


    }
}

// Строка "Название — Значение" (дублируем здесь, чтобы файлы были независимы)
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