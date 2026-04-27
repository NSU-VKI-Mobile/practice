package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    val calculations by viewModel.calculations.collectAsState()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История расчётов",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (calculations.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Нет сохранённых расчётов")
            }
        } else {
            LazyColumn {
                items(calculations) { calculation ->
                    HistoryItem(
                        calculation = calculation,
                        dateFormat = dateFormat,
                        onClick = {
                            viewModel.loadCalculationById(calculation.id)
                            viewModel.navigateTo(Screen.HistoryDetail)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.navigateToMain() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("На главную")
        }
    }
}

@Composable
fun HistoryItem(
    calculation: DepositCalculation,
    dateFormat: SimpleDateFormat,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Дата: ${dateFormat.format(Date(calculation.calculationDate))}")
            Text("Стартовый взнос: ${calculation.initialAmount} руб.")
            Text("Итоговая сумма: ${calculation.finalAmount} руб.")
        }
    }
}