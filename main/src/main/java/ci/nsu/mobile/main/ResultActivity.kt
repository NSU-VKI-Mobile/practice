package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startAmount = intent.getDoubleExtra("start_amount", 0.0)
        val termMonths = intent.getIntExtra("term_months", 0)
        val interestRate = intent.getDoubleExtra("interest_rate", 0.0)
        val monthlyDeposit = intent.getDoubleExtra("monthly_deposit", 0.0)

        setContent {
            MaterialTheme {
                ResultScreen(
                    startAmount = startAmount,
                    termMonths = termMonths,
                    interestRate = interestRate,
                    monthlyDeposit = monthlyDeposit
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    startAmount: Double,
    termMonths: Int,
    interestRate: Double,
    monthlyDeposit: Double
) {
    val context = LocalContext.current
    val viewModel: CalculationViewModel = viewModel()

    val isLoading by viewModel.isLoading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val error by viewModel.error.collectAsState()

    val (totalAmount, totalProfit) = viewModel.calculateResult(
        startAmount, termMonths, interestRate, monthlyDeposit
    )

    LaunchedEffect(saveSuccess) {
        if (saveSuccess) {
            Toast.makeText(context, "Расчёт сохранён!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат расчёта", fontSize = 18.sp, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Итоги вклада", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow("Начальная сумма:", String.format("%.2f ₽", startAmount))
                    InfoRow("Срок вклада:", "$termMonths месяцев")
                    InfoRow("Процентная ставка:", "${interestRate}% годовых")
                    if (monthlyDeposit > 0) {
                        InfoRow("Ежемесячное пополнение:", String.format("%.2f ₽", monthlyDeposit))
                    }
                    HorizontalDivider()
                    InfoRow("Итоговая сумма:", String.format("%.2f ₽", totalAmount), isTotal = true)
                    InfoRow("Общий доход:", String.format("%.2f ₽", totalProfit), isTotal = true)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val calculation = Calculation(
                            startAmount = startAmount,
                            termMonths = termMonths,
                            interestRate = interestRate,
                            monthlyDeposit = monthlyDeposit,
                            totalAmount = totalAmount,
                            totalProfit = totalProfit,
                            date = System.currentTimeMillis()
                        )
                        viewModel.saveCalculation(calculation)
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Сохранить", fontSize = 18.sp)
                    }
                }

                Button(
                    onClick = {
                        // Создаём Intent для перехода на MainActivity
                        val intent = Intent(context, MainActivity::class.java)
                        // Очищаем стек Activity, чтобы нельзя было вернуться назад к ResultActivity
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(intent)
                        // Закрываем текущую Activity
                        (context as? ResultActivity)?.finish()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("В начало", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = if (isTotal) 18.sp else 16.sp,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Text(
            value,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}