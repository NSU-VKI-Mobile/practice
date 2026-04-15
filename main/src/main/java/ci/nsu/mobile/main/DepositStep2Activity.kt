package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class DepositStep2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startAmount = intent.getDoubleExtra("start_amount", 0.0)
        val termMonths = intent.getIntExtra("term_months", 0)

        setContent {
            MaterialTheme {
                CalculationStep2Screen(
                    activity = this,
                    startAmount = startAmount,
                    termMonths = termMonths
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationStep2Screen(
    activity: DepositStep2Activity,
    startAmount: Double,
    termMonths: Int
) {
    val viewModel: CalculationViewModel = viewModel()

    // Наблюдаем за полем пополнения и его ошибками
    val monthlyDeposit by viewModel.monthlyDepositInput.collectAsState()
    val monthlyDepositError by viewModel.monthlyDepositError.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val availableRates = viewModel.getAvailableRates(termMonths)
    var selectedRate by remember { mutableStateOf(availableRates.find { it.isAvailable }?.rate ?: 0.0) }

    // Находим доступную ставку для отображения подсказки
    val availableRate = availableRates.find { it.isAvailable }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расчёт вклада - Этап 2",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Дополнительные параметры",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )

            // Карточка с информацией о вкладе
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Стартовый взнос: ${String.format("%.2f", startAmount)} ₽")
                    Text("Срок вклада: $termMonths месяцев")

                    // Подсказка о доступной ставке
                    if (availableRate != null) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        Text(
                            text = "✓ Доступная ставка: ${availableRate.description}",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Text("Выберите процентную ставку:", modifier = Modifier.fillMaxWidth())

            // Выпадающий список ставок
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = "${selectedRate}%",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Процентная ставка") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    enabled = availableRates.any { it.isAvailable }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    availableRates.forEach { rate ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        rate.description,
                                        color = if (rate.isAvailable) Color.Unspecified else Color.Gray
                                    )
                                    if (rate.isAvailable) {
                                        Icon(Icons.Default.Check, contentDescription = "Доступно")
                                    }
                                }
                            },
                            onClick = {
                                if (rate.isAvailable) {
                                    selectedRate = rate.rate
                                    expanded = false
                                }
                            },
                            enabled = rate.isAvailable
                        )
                    }
                }
            }

            // Поле "Ежемесячное пополнение" с валидацией
            OutlinedTextField(
                value = monthlyDeposit,
                onValueChange = { viewModel.updateMonthlyDeposit(it) },
                label = { Text("Ежемесячное пополнение (необязательно)") },
                placeholder = { Text("Введите сумму (например: 5000)") },
                isError = monthlyDepositError != null,
                supportingText = {
                    if (monthlyDepositError != null) {
                        Text(
                            text = monthlyDepositError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    } else {
                        Text("Оставьте пустым, если пополнения не будет")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (monthlyDepositError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { activity.finish() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        val monthlyAmount = viewModel.getValidatedMonthlyDeposit()
                        val intent = Intent(activity, ResultActivity::class.java).apply {
                            putExtra("start_amount", startAmount)
                            putExtra("term_months", termMonths)
                            putExtra("interest_rate", selectedRate)
                            putExtra("monthly_deposit", monthlyAmount)
                        }
                        activity.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}