package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import androidx.compose.foundation.layout.Row
// database imports
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.launch

import androidx.lifecycle.viewmodel.compose.viewModel

class Stage03Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Data from previous screen
        val initialDeposit = intent.getDoubleExtra("INITIAL_DEPOSIT", 0.0)
        val termMonths = intent.getIntExtra("TERM_MONTHS", 0)
        val interestRate = intent.getDoubleExtra("INTEREST_RATE", 0.0)
        val depositName = intent.getStringExtra("DEPOSIT_NAME") ?: ""

        // Resulting summ
        val totalAmount = calculateTotalAmount(initialDeposit, termMonths, interestRate)
        val earnedInterest = totalAmount - initialDeposit

        setContent {
            PracticeTheme {
                Stage03Screen(
                    initialDeposit = initialDeposit,
                    termMonths = termMonths,
                    interestRate = interestRate,
                    totalAmount = totalAmount,
                    earnedInterest = earnedInterest,
                    depositName = depositName
                )
            }
        }
    }

    private fun calculateTotalAmount(initialDeposit: Double, months: Int, rate: Double): Double {
        // Simple : sum * (1 + rate * (months -> years))
        val years = months / 12.0
        return initialDeposit * (1 + (rate / 100) * years)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stage03Screen(
    initialDeposit: Double,
    termMonths: Int,
    interestRate: Double,
    totalAmount: Double,
    earnedInterest: Double,
    depositName: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    // INITIALIZE DATABASE AND REPOS
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { DepositRepository(database.depositDao()) }

    val viewModel: Stage03ViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return Stage03ViewModel(repository) as T
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Расчёт вкладов - Результат",
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
            verticalArrangement = Arrangement.Center
        ) {
            // Calculation results
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Title
                    Text(
                        text = "Результаты расчёта",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = depositName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Start deposit
                    ResultRow(
                        label = "Стартовый взнос:",
                        value = String.format("%.2f ₽", initialDeposit)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Deposit Term
                    ResultRow(
                        label = "Срок вклада:",
                        value = "$termMonths месяцев"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Deposit coeff
                    ResultRow(
                        label = "Процентная ставка:",
                        value = String.format("%.2f%%", interestRate)
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    androidx.compose.material3.Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Total percents
                    ResultRow(
                        label = "Начисленные проценты:",
                        value = String.format("%.2f ₽", earnedInterest),
                        isHighlighted = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Total sum
                    ResultRow(
                        label = "Итоговая сумма:",
                        value = String.format("%.2f ₽", totalAmount),
                        isTotal = true
                    )
                }
            }

            // SaveTO Database
            Button(
                onClick = {
                    viewModel.saveCalculation(
                        initialDeposit = initialDeposit,
                        termMonths = termMonths,
                        interestRate = interestRate,
                        finalAmount = totalAmount,
                        interestEarned = earnedInterest,
                        depositName = depositName,
                        onSuccess = {
                            Toast.makeText(context, "Расчёт успешно сохранён!", Toast.LENGTH_SHORT).show()
                            navigateToMainScreen(context)
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, "Ошибка при сохранении: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Сохранить", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

// To main screen
            Button(
                onClick = {
                    navigateToMainScreen(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "В начало", fontSize = 16.sp)
            }
        }
    }
}


// Go back to main screen
private fun navigateToMainScreen(context: android.content.Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = value,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary
            else if (isTotal) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Stage03ScreenPreview() {
    PracticeTheme {
        Stage03Screen(
            initialDeposit = 100000.0,
            termMonths = 12,
            interestRate = 7.2,
            totalAmount = 107200.0,
            earnedInterest = 7200.0,
            depositName = "Премиум вклад"
        )
    }
}