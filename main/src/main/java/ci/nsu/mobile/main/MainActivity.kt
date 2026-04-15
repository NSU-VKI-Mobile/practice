package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.db.DepositDatabase
import ci.nsu.mobile.main.ui.MainViewModel
import ci.nsu.mobile.main.ui.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DepositApp()
        }
    }
}


@Composable
fun DepositApp() {
    val context = LocalContext.current
    val dao = DepositDatabase.getInstance(context).depositDao()
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(dao))


    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { HomeScreen(navController) }
        composable("step_one") { StepOneScreen(navController, viewModel) }
        composable("step_two") { StepTwoScreen(navController, viewModel) }
        composable("result") { ResultScreen(navController, viewModel) }
        composable("history") { HistoryScreen(navController, viewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current as Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = { navController.navigate("step_one") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("history") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("История расчётов")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { context.finish() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Закрыть приложение")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepOneScreen(navController: NavController, viewModel: MainViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Параметры вклада") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = viewModel.startAmount,
                onValueChange = { viewModel.startAmount = it },
                label = { Text("Стартовый взнос") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.months,
                onValueChange = { viewModel.months = it },
                label = { Text("Срок (месяцы)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("В начало")
                }

                Button(
                    onClick = { navController.navigate("step_two") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Далее")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun StepTwoScreen(navController: NavController, viewModel: MainViewModel) {

    val rates = viewModel.resolveRate()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Дополнительно") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (rates.isEmpty()) {
                Text("Введите корректный срок на прошлом шаге")
            } else {
                Text("Процентная ставка:")

                rates.forEach {
                    Button(
                        onClick = { viewModel.rate = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("$it %")
                    }
                }
            }

            OutlinedTextField(
                value = viewModel.monthlyTopUp,
                onValueChange = { viewModel.monthlyTopUp = it },
                label = { Text("Ежемесячное пополнение") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Назад")
                }

                Button(
                    onClick = {
                        viewModel.calculateDeposit()
                        navController.navigate("result")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Рассчитать")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ResultScreen(navController: NavController, viewModel: MainViewModel) {

    val result = viewModel.result ?: return

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Результат") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Старт: ${result.startAmount}")
            Text("Срок: ${result.months}")
            Text("Ставка: ${result.rate}%")
            Text("Ежемесячное пополнение: ${result.monthlyTopUp}%")
            Text("Итог: ${result.finalAmount}")
            Text("Прибыль: ${result.profit}")
            Text("Дата: ${result.date}")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveDeposit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }

            OutlinedButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В начало")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: MainViewModel) {
    val history by viewModel.history.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("История") })
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(history) { item ->
                Text("${item.startAmount} → ${item.finalAmount}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }}
