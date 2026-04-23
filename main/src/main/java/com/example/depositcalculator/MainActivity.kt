package com.example.depositcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.depositcalculator.ui.theme.PracticeTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.depositcalculator.screen.HistoryScreen
import com.example.depositcalculator.screen.ResultScreen
import com.example.depositcalculator.screen.Step1Screen
import com.example.depositcalculator.screen.Step2Screen
import com.example.depositcalculator.viewmodel.DepositeViewModel
import com.example.depositcalculator.Deposit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: DepositeViewModel = viewModel()
            val navController = rememberNavController()
            val history by viewModel.history.collectAsState()

            var tempAmount by remember { mutableStateOf(0.0) }
            var tempMonths by remember { mutableStateOf(0) }
            var currentDeposit by remember { mutableStateOf<Deposit?>(null) }

            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    MainScreen(
                        onCalculate = { navController.navigate("step1") },
                        onHistory = { navController.navigate("history") },
                        onCloseApp = { finish() }
                    )
                }
                composable("step1") {
                    Step1Screen(
                        onNext = { amount, months ->
                            tempAmount = amount
                            tempMonths = months
                            navController.navigate("step2")
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("step2") {
                    Step2Screen(
                        months = tempMonths,
                        onBack = { navController.popBackStack() },
                        onCalculate = { rate, topUp ->
                            currentDeposit = DepositCalculator.calculate(tempAmount, tempMonths, rate, topUp)
                            navController.navigate("result")
                        }
                    )
                }
                composable("result") {
                    currentDeposit?.let { deposit ->
                        ResultScreen(
                            deposit = deposit,
                            onBackToMain = { navController.navigate("main") { popUpTo("main") { inclusive = true } } },
                            onSave = {
                                viewModel.saveDeposit(DepositEntity.fromDomain(deposit))
                                navController.navigate("history")
                            }
                        )
                    }
                }
                composable("history") {
                    HistoryScreen(history = history, onBack = { navController.navigate("main")})
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onCalculate: () -> Unit,
    onHistory: () -> Unit,
    onCloseApp: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Расчёт вкладов",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Button(
                onClick = onCalculate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Рассчитать", fontSize = 18.sp)
            }

            Button(
                onClick = onHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("История расчётов", fontSize = 18.sp)
            }

            Button(
                onClick = onCloseApp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Закрыть приложение", fontSize = 18.sp)
            }
        }
    }
}

