package ci.nsu.moble.main.presentation.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.data.database.DepositCalculationEntity
import ci.nsu.moble.main.presentation.viewmodels.CalculationViewModel
import ci.nsu.moble.main.presentation.viewmodels.ResultViewModel
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(
    calcVM: CalculationViewModel,
    onSave: () -> Unit,
    onBackToMain: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val resultVM: ResultViewModel = viewModel(factory = ResultViewModel.factory(application))
    val calcState by calcVM.state.collectAsState()

    val amount = calcState.initialAmount
    val months = calcState.periodMonths
    val rate = calcState.interestRate
    val topUp = calcState.monthlyTopUp

    LaunchedEffect(amount, months, rate, topUp) {
        if (amount > 0 && months > 0 && rate > 0) {
            resultVM.calculateAndShow(amount, months, rate, topUp)
        }
    }

    val resultState by resultVM.state.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Результаты расчёта", style = MaterialTheme.typography.headlineSmall)

        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Стартовый взнос: ${"%.2f".format(amount)}")
                Text("Срок вклада: $months мес.")
                Text("Процентная ставка: $rate %")
                if (topUp != null) Text("Ежемесячное пополнение: ${"%.2f".format(topUp)}")
                HorizontalDivider()
                Text("Итог: ${"%.2f".format(resultState.finalAmount)}", style = MaterialTheme.typography.titleMedium)
                Text("Проценты: ${"%.2f".format(resultState.interestEarned)}")
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        val entity = DepositCalculationEntity(
                            initialAmount = amount,
                            periodMonths = months,
                            interestRate = rate,
                            monthlyTopUp = topUp,
                            finalAmount = resultState.finalAmount,
                            interestEarned = resultState.interestEarned
                        )
                        resultVM.saveCalculation(entity) {
                            onSave()
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }
            Button(onClick = onBackToMain, modifier = Modifier.weight(1f)) {
                Text("В начало")
            }
        }

        if (resultState.saveSuccess == false) {
            Text("Ошибка сохранения", color = MaterialTheme.colorScheme.error)
        }
    }
}