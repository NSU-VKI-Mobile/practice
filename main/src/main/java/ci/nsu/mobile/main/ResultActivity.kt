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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme

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
    val text = remember { mutableStateOf("") }
    val startingFee by remember { mutableStateOf("") }
    val depositPeriod by remember {mutableStateOf("")}
    val interestRate by remember {mutableStateOf("")} //процентная ставка
    val monthlyReplenishment by remember {mutableStateOf("")} //дефолт прочерк
    val totalSum by remember {mutableStateOf("")}
    val accruedInterest by remember {mutableStateOf("")} //начисленные проценты


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
            .height(200.dp)
            .width(500.dp)
            .padding(10.dp)
            .border(width = 1.dp, color = Color.Black)
            .padding(10.dp)
            .background(Color.White)

        ) {

            Text("Стартовый взнос: 1")
            Text("Срок вклада: 1")
            Text("Процентная ставка: 1")
            Text("Ежемесячное пополнение: 1")
            Text("Итоговая сумма: 1")
            Text("Начисленные проценты: 1")

        }

        Row(
            modifier = Modifier
        ) {
            Button(
                onClick = {
                    if (context is Activity) {
                        context.finishAffinity()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

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
                    if (context is Activity) {
                        context.finishAffinity()
                        val intent = Intent(context, SaveListDepositActivity::class.java)
                        context.startActivity(intent)
                    }
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