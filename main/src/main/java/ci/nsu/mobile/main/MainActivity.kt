package ci.nsu.mobile.main

import ci.nsu.mobile.main.data.DepositCalculation

import androidx.activity.viewModels
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositRepository
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModelFactory

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {

    private val depositViewModel: DepositViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())
        DepositViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                DepositApp(depositViewModel)
            }
        }
    }
}

@Composable
fun DepositApp(viewModel: DepositViewModel) {

    val calculations by viewModel.calculations.collectAsState(initial = emptyList())

    var currentScreen by rememberSaveable {
        mutableStateOf("home")
    }

    var initialAmount by rememberSaveable {
        mutableStateOf("")
    }

    var periodMonths by rememberSaveable {
        mutableStateOf("")
    }

    var interestRate by rememberSaveable {
        mutableStateOf("")
    }

    var monthlyDeposit by rememberSaveable {
        mutableStateOf("")
    }

    var finalAmount by rememberSaveable {
        mutableStateOf(0.0)
    }

    var earnedInterest by rememberSaveable {
        mutableStateOf(0.0)
    }

    when (currentScreen) {
        "home" -> {
            MainScreen(
                onCalculateClick = {
                    currentScreen = "step1"
                },

                onHistoryClick = {
                    currentScreen = "history"
                }
            )
        }

        "step1" -> {
            DepositStepOneScreen(
                initialAmount = initialAmount,
                periodMonths = periodMonths,

                onInitialAmountChange = {
                    initialAmount = it
                },

                onPeriodMonthsChange = {
                    periodMonths = it
                },

                onHomeClick = {
                    currentScreen = "home"
                },

                onNextClick = {
                    if (initialAmount.isNotEmpty() && periodMonths.isNotEmpty()) {
                        currentScreen = "step2"
                    }
                }
            )
        }

        "step2" -> {
            DepositStepTwoScreen(
                interestRate = interestRate,
                monthlyDeposit = monthlyDeposit,

                onInterestRateChange = {
                    interestRate = it
                },

                onMonthlyDepositChange = {
                    monthlyDeposit = it
                },

                onBackClick = {
                    currentScreen = "step1"
                },

                onCalculateClick = {
                    val amount = initialAmount.toDoubleOrNull() ?: 0.0
                    val months = periodMonths.toIntOrNull() ?: 0
                    val rate = interestRate.toDoubleOrNull() ?: 0.0
                    val monthly = monthlyDeposit.toDoubleOrNull() ?: 0.0

                    val result = calculateDeposit(
                        initialAmount = amount,
                        months = months,
                        annualRate = rate,
                        monthlyDeposit = monthly
                    )

                    finalAmount = result.first
                    earnedInterest = result.second

                    currentScreen = "result"
                }
            )
        }

        "result" -> {
            DepositResultScreen(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyDeposit = monthlyDeposit,
                finalAmount = finalAmount,
                earnedInterest = earnedInterest,

                onSaveClick = {
                    val calculation = DepositCalculation(
                        initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
                        periodMonths = periodMonths.toIntOrNull() ?: 0,
                        interestRate = interestRate.toDoubleOrNull() ?: 0.0,
                        monthlyDeposit = monthlyDeposit.toDoubleOrNull() ?: 0.0,
                        finalAmount = finalAmount,
                        earnedInterest = earnedInterest
                    )

                    viewModel.saveCalculation(calculation)
                },

                onHomeClick = {
                    currentScreen = "home"
                },

                onBackClick = {
                    currentScreen = "step2"
                }
            )
        }

        "history" -> {
            HistoryScreen(
                calculations = calculations,

                onClearClick = {
                    viewModel.clearHistory()
                },

                onBackClick = {
                    currentScreen = "home"
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Расчёт вкладов",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onCalculateClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                // Закрываем приложение
                if (context is Activity) {
                    context.finishAffinity()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Закрыть приложение")
        }
    }
}

@Composable
fun DepositStepOneScreen(
    initialAmount: String,
    periodMonths: String,
    onInitialAmountChange: (String) -> Unit,
    onPeriodMonthsChange: (String) -> Unit,
    onHomeClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Основные параметры вклада",
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = initialAmount,

            onValueChange = { newValue ->
                // Разрешаем только цифры
                if (newValue.all { it.isDigit() }) {
                    onInitialAmountChange(newValue)
                }
            },

            label = {
                Text("Стартовый взнос")
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = periodMonths,

            onValueChange = { newValue ->
                // Разрешаем только цифры
                if (newValue.all { it.isDigit() }) {
                    onPeriodMonthsChange(newValue)
                }
            },

            label = {
                Text("Срок вклада в месяцах")
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Далее")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

@Composable
fun DepositStepTwoScreen(
    interestRate: String,
    monthlyDeposit: String,
    onInterestRateChange: (String) -> Unit,
    onMonthlyDepositChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCalculateClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Дополнительные параметры",
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = interestRate,

            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onInterestRateChange(newValue)
                }
            },

            label = {
                Text("Процентная ставка")
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = monthlyDeposit,

            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onMonthlyDepositChange(newValue)
                }
            },

            label = {
                Text("Ежемесячное пополнение")
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (interestRate.isNotEmpty() && monthlyDeposit.isNotEmpty()) {
                    onCalculateClick()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}

fun calculateDeposit(
    initialAmount: Double,
    months: Int,
    annualRate: Double,
    monthlyDeposit: Double
): Pair<Double, Double> {

    var currentAmount = initialAmount

    // Каждый месяц начисляем проценты и добавляем пополнение
    repeat(months) {
        val monthlyInterest = currentAmount * annualRate / 100 / 12
        currentAmount += monthlyInterest
        currentAmount += monthlyDeposit
    }

    // Сколько денег пользователь внёс сам
    val totalDeposits = initialAmount + monthlyDeposit * months

    // Начисленные банком проценты
    val earnedInterest = currentAmount - totalDeposits

    return Pair(currentAmount, earnedInterest)
}

@Composable
fun DepositResultScreen(
    initialAmount: String,
    periodMonths: String,
    interestRate: String,
    monthlyDeposit: String,
    finalAmount: Double,
    earnedInterest: Double,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Результат расчёта",
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Стартовый взнос: $initialAmount ₽")
        Text("Срок вклада: $periodMonths мес.")
        Text("Процентная ставка: $interestRate%")
        Text("Ежемесячное пополнение: $monthlyDeposit ₽")

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Итоговая сумма: %.2f ₽".format(finalAmount),
            fontSize = 20.sp
        )

        Text(
            text = "Начисленные проценты: %.2f ₽".format(earnedInterest),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("В начало")
        }
    }
}

@Composable
fun HistoryScreen(
    calculations: List<DepositCalculation>,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "История расчётов",
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (calculations.isEmpty()) {
            Text("История пока пустая")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(calculations) { calculation ->
                    Text(
                        text = """
                            Взнос: ${calculation.initialAmount} ₽
                            Срок: ${calculation.periodMonths} мес.
                            Ставка: ${calculation.interestRate}%
                            Итог: ${"%.2f".format(calculation.finalAmount)} ₽
                        """.trimIndent(),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Button(
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Очистить историю")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}