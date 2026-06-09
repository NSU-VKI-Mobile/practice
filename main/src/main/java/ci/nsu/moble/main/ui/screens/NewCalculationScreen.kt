package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.data.repository.DepositRepository
import kotlinx.coroutines.launch
import kotlin.math.pow

// функция для расчёта итоговой суммы и процентов
private fun calculateFinal(
    amount: Double,
    months: Int,
    rate: Double,
    topUp: Double?
): Pair<Double, Double> {
    val monthlyRate = rate / 100 / 12
    val final = if (topUp != null && topUp > 0) {
        var total = amount
        repeat(months) {
            total = total * (1 + monthlyRate) + topUp
        }
        total
    } else {
        amount * (1 + monthlyRate).pow(months.toDouble())
    }
    val totalTopUp = (topUp ?: 0.0) * months
    val earned = final - amount - totalTopUp
    return final to earned
}

@Composable
fun NewCalculationScreen(
    userId: Long,
    repository: DepositRepository,
    onSaveSuccess: () -> Unit
) {
    // генерируем уникальный ключ для принудительного пересоздания
    val key = remember { System.currentTimeMillis() }

    androidx.compose.runtime.key(key) {
        NewCalculationScreenContent(
            userId = userId,
            repository = repository,
            onSaveSuccess = onSaveSuccess
        )
    }
}

@Composable
private fun NewCalculationScreenContent(
    userId: Long,
    repository: DepositRepository,
    onSaveSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var initialAmount by remember { mutableStateOf("") }
    var periodMonths by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var monthlyTopUp by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var finalAmount by remember { mutableStateOf<Double?>(null) }
    var interestEarned by remember { mutableStateOf<Double?>(null) }

    // функция сброса всех полей
    fun resetForm() {
        initialAmount = ""
        periodMonths = ""
        interestRate = ""
        monthlyTopUp = ""
        finalAmount = null
        interestEarned = null
        errorMessage = null
    }

    // сброс при первом входе на экран
    LaunchedEffect(Unit) {
        resetForm()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Новый расчёт", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = initialAmount,
            onValueChange = { initialAmount = it },
            label = { Text("Стартовый взнос (руб)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = periodMonths,
            onValueChange = { periodMonths = it },
            label = { Text("Срок (месяцев)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text("Процентная ставка (%)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = monthlyTopUp,
            onValueChange = { monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение (руб) (необязательно)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amount = initialAmount.toDoubleOrNull()
                val months = periodMonths.toIntOrNull()
                val rate = interestRate.toDoubleOrNull()
                val topUp = monthlyTopUp.toDoubleOrNull()

                if (amount == null || months == null || rate == null || amount <= 0 || months <= 0 || rate <= 0) {
                    errorMessage = "Заполните все обязательные поля корректно"
                    finalAmount = null
                    interestEarned = null
                    return@Button
                }

                val (final, earned) = calculateFinal(amount, months, rate, topUp)
                finalAmount = final
                interestEarned = earned
                errorMessage = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (finalAmount != null && interestEarned != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💰 Итоговая сумма: ${String.format("%.2f", finalAmount)} руб",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "📊 Начисленные проценты: ${String.format("%.2f", interestEarned)} руб",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                val amount = initialAmount.toDoubleOrNull()
                val months = periodMonths.toIntOrNull()
                val rate = interestRate.toDoubleOrNull()
                val topUp = monthlyTopUp.toDoubleOrNull()
                val final = finalAmount
                val earned = interestEarned

                if (amount != null && months != null && rate != null && final != null && earned != null) {
                    isLoading = true
                    scope.launch {
                        try {
                            val calculation = ci.nsu.moble.main.data.models.DepositCalculation(
                                userId = userId,
                                initialAmount = amount,
                                periodMonths = months,
                                interestRate = rate,
                                monthlyTopUp = topUp,
                                finalAmount = final,
                                interestEarned = earned
                            )
                            repository.saveCalculation(calculation)

                            resetForm()

                            onSaveSuccess()
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Ошибка сохранения"
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = "Сначала выполните расчёт"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && finalAmount != null && interestEarned != null
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Сохранить")
            }
        }

        // Кнопка "Очистить" - для принудительного сброса
        Button(
            onClick = {
                resetForm()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Очистить")
        }
    }
}