package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class Stage02Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Получаем данные с предыдущего экрана
        val initialDeposit = intent.getDoubleExtra("INITIAL_DEPOSIT", 0.0)
        val termMonths = intent.getIntExtra("TERM_MONTHS", 0)

        setContent {
            PracticeTheme {
                Stage02Screen(initialDeposit = initialDeposit, termMonths = termMonths)
            }
        }
    }
}

data class DepositOption(
    val id: Int,
    val name: String,
    val minAmount: Double,
    val maxAmount: Double,
    val minTerm: Int,
    val maxTerm: Int,
    val interestRate: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stage02Screen(initialDeposit: Double, termMonths: Int) {
    val context = LocalContext.current
    val viewModel: Stage02ViewModel = viewModel()
    val selectedDeposit by viewModel.selectedDeposit.collectAsStateWithLifecycle()
    val expanded by viewModel.expanded.collectAsStateWithLifecycle()

    val depositOptions = listOf(
        DepositOption(1, "Базовый вклад", 10000.0, 500000.0, 6, 12, 25.5),
        DepositOption(2, "Премиум вклад", 200000.0, 2000000.0, 12, 24, 20.2),
        DepositOption(3, "Стартовый вклад", 5000.0, 100000.0, 3, 6, 35.0),
        DepositOption(4, "VIP вклад", 500000.0, 10000000.0, 24, 60, 12.0),
        DepositOption(5, "Долгосрочный вклад", 50000.0, 1000000.0, 18, 36, 10.0),
        DepositOption(6, "Краткосрочный вклад", 10000.0, 300000.0, 3, 5, 18.5)
    )

    val filteredDeposits = depositOptions.map { deposit ->
        val isAvailable = initialDeposit >= deposit.minAmount &&
                initialDeposit <= deposit.maxAmount &&
                termMonths >= deposit.minTerm &&
                termMonths <= deposit.maxTerm
        Pair(deposit, isAvailable)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расчёт вкладов",
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ваши параметры:",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Стартовый взнос: ${String.format("%.2f", initialDeposit)} ₽",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Срок вклада: $termMonths месяцев",
                        fontSize = 14.sp
                    )
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { viewModel.setExpanded(it) },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedDeposit?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите тип вклада") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { viewModel.setExpanded(false) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    filteredDeposits.forEach { (deposit, isAvailable) ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = deposit.name,
                                        fontSize = 16.sp,
                                        color = if (isAvailable) MaterialTheme.colorScheme.onSurface
                                        else Color.Gray
                                    )
                                    Text(
                                        text = "от ${String.format("%.0f", deposit.minAmount)}₽ до ${String.format("%.0f", deposit.maxAmount)}₽, срок: ${deposit.minTerm}-${deposit.maxTerm} мес., ставка: ${deposit.interestRate}%",
                                        fontSize = 12.sp,
                                        color = if (isAvailable) MaterialTheme.colorScheme.onSurfaceVariant
                                        else Color.LightGray
                                    )
                                }
                            },
                            onClick = {
                                if (isAvailable) {
                                    viewModel.selectDeposit(deposit)
                                    viewModel.setExpanded(false)
                                }
                            },
                            enabled = isAvailable,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (selectedDeposit != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Выбранный вклад: ${selectedDeposit!!.name}",
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Text(text = "Процентная ставка: ${selectedDeposit!!.interestRate}%")
                        Text(text = "Доход за период: ${String.format("%.2f", calculateIncome(initialDeposit, termMonths, selectedDeposit!!.interestRate))} ₽")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


                // TO STAGE 3
            Button(
                onClick = {
                    if (selectedDeposit != null) {

                        val intent = Intent(context, Stage03Activity::class.java).apply {
                            putExtra("INITIAL_DEPOSIT", initialDeposit)
                            putExtra("TERM_MONTHS", termMonths)
                            putExtra("INTEREST_RATE", selectedDeposit!!.interestRate)
                            putExtra("DEPOSIT_NAME", selectedDeposit!!.name)
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedDeposit != null
            ) {
                Text(text = "Рассчитать", fontSize = 16.sp)
            }


            Spacer(modifier = Modifier.height(32.dp))


            // BACK TO STAGE 1

            Button(
                onClick = {
                    // Back to stage 1
                    val intent = Intent(context, Stage01Activity::class.java).apply {
                        putExtra("RETURNED_DEPOSIT", initialDeposit.toString())
                        putExtra("RETURNED_TERM", termMonths.toString())
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Назад", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}

fun calculateIncome(amount: Double, months: Int, rate: Double): Double {
    return amount * (rate / 100) * (months / 12.0)
}

@Preview(showBackground = true)
@Composable
fun Stage02ScreenPreview() {
    PracticeTheme {
        Stage02Screen(initialDeposit = 100000.0, termMonths = 12)
    }
}