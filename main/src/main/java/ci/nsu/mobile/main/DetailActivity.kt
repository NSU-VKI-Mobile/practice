package ci.nsu.mobile.main

import android.annotation.SuppressLint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import data.DepositCalculations
import data.SingletonDatabase

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailScreenActivity(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenActivity(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier

) {
    val context = LocalContext.current
    val app = context.applicationContext as SingletonDatabase
    val viewModel = app.getViewModel()

    var deposit by remember {mutableStateOf<DepositCalculations?>(null)}

    val depositId = (context as Activity).intent.getLongExtra("deposit_id", -1L)

    //LaunchedEffect принимает ключ. ключ меняется - код запускается заново
    LaunchedEffect(depositId) {
        if (depositId >= 0)
        {
            deposit = viewModel.getCalculationById(depositId)
        }

    }

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

            Text(text = "Детальная информация")

            Column(
                modifier = Modifier
                    .width(700.dp)
                    .padding(10.dp)
                    .border(width = 1.dp, color = Color.Black)
                    .padding(10.dp)
                    .background(Color.White)

            ) {

                deposit?.let { dep ->
                    Text("Стартовый взнос: ${dep.initialAmount}")
                    Text("Срок вклада: ${dep.termMonths}")
                    Text("Процентная ставка: ${dep.interestRate}")
                    Text("Валюта: ${dep.currency}")
                    Text("Ежемесячное пополнение: ${dep.monthlyTopUp ?: "-"}")
                    Text("Итоговая сумма: ${"%.2f".format(dep.finalAmount)}")
                    Text("Начисленные проценты: ${"%.2f".format(dep.interestEarned)}")

                }
            }

            Row(
                modifier = Modifier
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, SaveListDepositActivity::class.java)
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
                    Text("Назад")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    PracticeTheme {
        DetailScreenActivity()
    }
}