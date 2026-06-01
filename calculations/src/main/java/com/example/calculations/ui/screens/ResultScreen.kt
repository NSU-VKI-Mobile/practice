package com.example.calculations.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calculations.navigation.Screens
import com.example.calculations.viewmodel.DepositCalculationViewModel
import com.example.calculations.viewmodel.DepositEvents
import com.example.ui.components.CustomButton
import java.util.Date
import java.util.Locale

@Composable
fun ResultScreenContent(navToScreen: (String) -> Unit,
                        viewModel: DepositCalculationViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formattedDate: String? = sdf.format(Date(state.calculationDate))

    Scaffold() { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Card(modifier = Modifier.padding(20.dp)) {
                Text("Стартовый взнос: ${state.initialAmount}₽",
                    Modifier.padding(20.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Срок вклада (в месяцах): ${state.periodMonths}",
                    Modifier.padding(20.dp, 0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Процентная ставка: ${state.interestRate}%",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                val mot = state.monthlyTopUp
                if (mot == null) {
                    Text("Ежемесячное пополнение: 0₽", Modifier.padding(20.dp,  0.dp))
                }
                else {
                    Text("Ежемесячное пополнение: ${mot}₽", Modifier.padding(20.dp,  0.dp))
                }
                Spacer(Modifier.padding(5.dp))
                Text("Итоговая сумма: ${String.format("%.2f", state.finalAmount)}₽",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Начисленные проценты: ${String.format("%.2f", state.interestEarned)}₽",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Дата и время рассчета: ${formattedDate}",
                    Modifier.padding(20.dp))
            }
            CustomButton(
                onClick = {
                    viewModel.depositCalculationEvent(DepositEvents.SaveEntity)
                    viewModel.depositCalculationEvent(DepositEvents.CleanAll)
                    navToScreen(Screens.MainScreen.route)
            }, modifier =  Modifier.padding(20.dp).width(150.dp),
                title = "Сохранить"
            )
        }
    }
}
