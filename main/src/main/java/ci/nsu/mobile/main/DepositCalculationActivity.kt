package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class DepositCalculationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculationStep1Screen(activity = this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationStep1Screen(activity: DepositCalculationActivity) {
    val viewModel: CalculationViewModel = viewModel()

    // Наблюдаем за значениями полей и ошибками из ViewModel
    val startAmount by viewModel.startAmountInput.collectAsState()
    val termMonths by viewModel.termMonthsInput.collectAsState()
    val startAmountError by viewModel.startAmountError.collectAsState()
    val termMonthsError by viewModel.termMonthsError.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расчёт вклада - Этап 1",
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
                text = "Введите параметры вклада",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )

            // Поле "Стартовый взнос" с полной валидацией
            OutlinedTextField(
                value = startAmount,
                onValueChange = { viewModel.updateStartAmount(it) },
                label = { Text("Стартовый взнос *") },
                placeholder = { Text("Введите сумму (например: 100000)") },
                isError = startAmountError != null,
                supportingText = {
                    if (startAmountError != null) {
                        Text(
                            text = startAmountError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    } else {
                        Text("Только цифры и точка (например: 10000.50)")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (startAmountError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )

            // Поле "Срок вклада" с полной валидацией
            OutlinedTextField(
                value = termMonths,
                onValueChange = { viewModel.updateTermMonths(it) },
                label = { Text("Срок вклада (месяцы) *") },
                placeholder = { Text("Введите количество месяцев (например: 12)") },
                isError = termMonthsError != null,
                supportingText = {
                    if (termMonthsError != null) {
                        Text(
                            text = termMonthsError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    } else {
                        Text("Только целое число (от 1 до 600)")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (termMonthsError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { activity.finish() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = {
                        // Проверяем валидацию через ViewModel
                        if (viewModel.validateStep1()) {
                            val amount = viewModel.getValidatedStartAmount()!!
                            val months = viewModel.getValidatedTermMonths()!!

                            val intent = Intent(activity, DepositStep2Activity::class.java).apply {
                                putExtra("start_amount", amount)
                                putExtra("term_months", months)
                            }
                            activity.startActivity(intent)
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Далее")
                }
            }
        }
    }
}