package ci.nsu.mobile.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import data.SingletonDatabase

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResultScreenActivity(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreenActivity(modifier: Modifier = Modifier
    .background(Color.LightGray)) {

    val context = LocalContext.current

    val app = context.applicationContext as SingletonDatabase
    val viewModel = app.getViewModel()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val accruedInterest by viewModel.accruedInterest.collectAsState()
    val initialAmount by viewModel.initialAmount.collectAsState()
    val termInMonths by viewModel.termInMonths.collectAsState()
    val interestRate by viewModel.interestRate.collectAsState()
    val monthlyDeposit by viewModel.monthlyDeposit.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Расчет вкладов") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {

        Text(text = "Несохраненный расчет вклада")

        Column(modifier = Modifier
            .width(700.dp)
            .padding(10.dp)
            .border(width = 1.dp, color = Color.Black)
            .padding(10.dp)
            .background(Color.White)

        ) {

            Text("Стартовый взнос: $initialAmount")
            Text("Срок вклада: $termInMonths")
            Text("Процентная ставка: $interestRate")
            Text("Валюта: $selectedCurrency")
            Text("Ежемесячное пополнение: $monthlyDeposit")
            Text("Итоговая сумма: $totalAmount")
            Text("Начисленные проценты: $accruedInterest")

        }

        Row(
            modifier = Modifier
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(170.dp)
            ) {
                Text("В начало")
            }

            Button(
                onClick = {
                    viewModel.saveCalculation()
                    val intent = Intent(context, SaveListDepositActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(170.dp)

            ) {
                Text("Сохранить")
            }
        }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        ResultScreenActivity()
    }
}