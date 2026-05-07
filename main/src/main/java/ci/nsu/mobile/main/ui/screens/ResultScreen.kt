package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.Deposit
import ci.nsu.mobile.main.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(
    startAmount: Double,
    months: Int,
    rate: Double,
    monthlyAddition: Double,
    viewModel: MainViewModel,
    onHomeClick: () -> Unit
) {
    val monthlyRate = rate / 100.0 / 12.0
    val totalAmount = if (monthlyRate == 0.0) {
        startAmount + monthlyAddition * months
    } else {
        val mainPart = startAmount * (1 + monthlyRate).pow(months.toDouble())
        val additionPart = if (monthlyAddition > 0) {
            monthlyAddition * ((1 + monthlyRate).pow(months.toDouble()) - 1) / monthlyRate
        } else 0.0
        mainPart + additionPart
    }
    val earnedInterest = totalAmount - startAmount - (monthlyAddition * months)

    var isSaved by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()   // ← для запуска корутин из onClick

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Text(
                text = "Результат расчёта",
                fontSize = 22.sp,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SimpleRow("Стартовый взнос:", "${fmt(startAmount)} руб.")
                    SimpleRow("Срок вклада:", "$months мес.")
                    SimpleRow("Процентная ставка:", "$rate%")
                    SimpleRow(
                        "Ежемесячное пополнение:",
                        if (monthlyAddition > 0) "${fmt(monthlyAddition)} руб." else "нет"
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                    Text(
                        text = "Итоговая сумма: ${fmt(totalAmount)} руб.",
                        fontSize = 18.sp,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = "Начислено процентов: ${fmt(earnedInterest)} руб.",
                        fontSize = 15.sp,
                        color = Color(0xFF388E3C)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (!isSaved) {
                            val df = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                            val deposit = Deposit(
                                startAmount = startAmount,
                                months = months,
                                rate = rate,
                                monthlyAddition = monthlyAddition,
                                totalAmount = totalAmount,
                                earnedInterest = earnedInterest,
                                dateTime = df.format(Date())
                            )
                            viewModel.saveDeposit(deposit)
                            isSaved = true            // после этого кнопка станет неактивной
                        }

                        val msg = if (isSaved) "Расчёт сохранён!" else "Ошибка сохранения"
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(msg)
                        }
                    },
                    enabled = !isSaved,               // ← ключевая строка
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF388E3C),
                        disabledContainerColor = Color(0xFF9E9E9E), // цвет, когда выключена
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Сохранить")
                }

                Button(
                    onClick = onHomeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text("В начало")
                }
            }
        }
    }
}

@Composable
private fun SimpleRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontSize = 14.sp)
    }
}

private fun fmt(v: Double) = String.format(Locale.getDefault(), "%.2f", v)