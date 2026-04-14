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
import androidx.compose.ui.platform.LocalContext
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
    // Убираем фабрику - используем viewModel() без параметров
    val viewModel: CalculationViewModel = viewModel()

    var startAmount by remember { mutableStateOf("") }
    var termMonths by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

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

            OutlinedTextField(
                value = startAmount,
                onValueChange = {
                    startAmount = it
                    showError = false
                },
                label = { Text("Стартовый взнос *") },
                placeholder = { Text("Введите сумму (например: 100000)") },
                isError = showError && startAmount.isEmpty(),
                supportingText = {
                    if (showError && startAmount.isEmpty()) {
                        Text("Обязательное поле", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = termMonths,
                onValueChange = {
                    termMonths = it
                    showError = false
                },
                label = { Text("Срок вклада (месяцы) *") },
                placeholder = { Text("Введите количество месяцев (например: 12)") },
                isError = showError && termMonths.isEmpty(),
                supportingText = {
                    if (showError && termMonths.isEmpty()) {
                        Text("Обязательное поле", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
                        val amount = startAmount.toDoubleOrNull()
                        val months = termMonths.toIntOrNull()

                        if (amount != null && amount > 0 && months != null && months > 0) {
                            val intent = Intent(activity, DepositStep2Activity::class.java).apply {
                                putExtra("start_amount", amount)
                                putExtra("term_months", months)
                            }
                            activity.startActivity(intent)
                        } else {
                            showError = true
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