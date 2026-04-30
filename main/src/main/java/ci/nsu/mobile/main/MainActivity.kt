package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.DepositRepository
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = DepositRepository(database.depositDao())

        val viewModel: DepositViewModel by viewModels { DepositViewModel.Factory(repository) }

        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DepositApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun DepositApp(viewModel: DepositViewModel) {
    val navController = rememberNavController()

    var tempAmount by remember { mutableStateOf("") }
    var tempMonths by remember { mutableStateOf("") }
    var tempRate by remember { mutableStateOf(0.0) }
    var tempTopUp by remember { mutableStateOf(0.0) }

    NavHost(navController = navController, startDestination = "main") {

        // ГЛАВНЫЙ ЭКРАН
        composable("main") {
            MainScreen(
                onCalculateClick = { navController.navigate("step1") },
                onHistoryClick = { navController.navigate("history") },
                onCloseApp = { (navController.context as? ComponentActivity)?.finish() }
            )
        }

        composable("step1") {
            Step1Screen(
                onBack = { navController.popBackStack() },
                onNext = { amount, months ->
                    tempAmount = amount
                    tempMonths = months
                    navController.navigate("step2")
                }
            )
        }
        composable("step2") {
            Step2Screen(
                months = tempMonths.toIntOrNull() ?: 0,
                onBack = { navController.popBackStack() },
                onCalculate = { rate, topUp ->
                    tempRate = rate
                    tempTopUp = topUp
                    navController.navigate("result")
                }
            )
        }

        composable("result") {
            ResultScreen(
                amount = tempAmount.toDoubleOrNull() ?: 0.0,
                months = tempMonths.toIntOrNull() ?: 0,
                rate = tempRate,
                topUp = tempTopUp,
                onSave = {
                    viewModel.calculateAndSave(
                        tempAmount.toDoubleOrNull() ?: 0.0,
                        tempMonths.toIntOrNull() ?: 0,
                        tempTopUp,
                        tempRate
                    )
                    // Возвращаемся в начало и очищаем стек
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onGoHome = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onCloseApp: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Расчёт вкладов") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onCalculateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onHistoryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("История расчётов")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCloseApp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Закрыть приложение")
            }
        }
    }
}