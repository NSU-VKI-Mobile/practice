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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaveListDepositActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SaveListDepositScreenActivity(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveListDepositScreenActivity(modifier: Modifier = Modifier
    .background(Color.LightGray))
{
    val context = LocalContext.current
    val app = context.applicationContext as SingletonDatabase
    val viewModel = app.getViewModel()

    val listDeposit by viewModel.getAllHistory().collectAsState(initial = emptyList())

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (listDeposit.isEmpty()) {
                Text("История пуста")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(listDeposit) { deposit ->
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .border(width = 1.dp, color = Color.Black)
                                .padding(10.dp)
                                .background(Color.White),
                            onClick = {
                                val intent = Intent(context, DetailActivity::class.java)
                                intent.putExtra("deposit_id", deposit.id)
                                context.startActivity(intent)
                            }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                Text("Дата: ${formatter.format(Date(deposit.calculationDate))}")
                                Text("Стартовый взнос: ${deposit.initialAmount}")
                                Text("Срок вклада: ${deposit.termMonths}")
                                Text("Итоговая сумма: ${"%.2f".format(deposit.finalAmount)}")

                            }
                        }
                    }
                }
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
                ) {
                    Text("В начало")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaveListDepositPreview() {
    PracticeTheme {
        SaveListDepositScreenActivity()
    }
}