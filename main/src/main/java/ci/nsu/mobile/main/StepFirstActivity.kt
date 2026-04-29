package ci.nsu.mobile.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import data.AppDatabase
import data.DepositRepository
import data.SingletonDatabase
import viewmodel.DepositViewModel
import java.time.format.TextStyle

class StepFirstActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StepFirstScreenActivity(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFirstScreenActivity(modifier: Modifier = Modifier
    .background(Color.LightGray)) {
    val context = LocalContext.current
    val app = context.applicationContext as SingletonDatabase
    val viewModel = app.getViewModel()
    val initialAmount by viewModel.initialAmount.collectAsState()
    val termInMonth by viewModel.termInMonths.collectAsState()

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
                .padding(16.dp)
        ) {
            Text(text="Стартовый взнос *")

            TextField(
                value = initialAmount,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 25.sp),
                placeholder = { Text("Введите стартовый взнос") },
                onValueChange = {
                    viewModel.updateInititalAmount(it)
                })

            Spacer(modifier = Modifier
                .padding(10.dp))

            Text(text="Срок вклада в месяц *")

            TextField(
                value = termInMonth,
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 25.sp),
                placeholder = { Text("Введите срок вклада") },
                onValueChange = {
                    viewModel.updateTermInMonths(it)
                })

            Row(modifier = Modifier
                .height(100.dp)
            ){
                Button(
                    onClick = {
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

                Button(
                    onClick = {
                        val intent = Intent(context, StepSecondActivity::class.java)
                        context.startActivity(intent)
                    },
                    enabled = initialAmount.isNotEmpty() && termInMonth.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(170.dp)

                ) {
                    Text("Далее")
                }


            }

        }

        }
    }


@Preview(showBackground = true)
@Composable
fun StepFirstPreview() {
    PracticeTheme {
        StepFirstScreenActivity()
    }
}