package ci.nsu.mobile.main.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ci.nsu.mobile.main.navigation.Routes
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel
import java.util.Date
import java.util.Locale

@Composable
fun ResultScreenContent(navScreens: NavController,
                        viewModel: DepositCalculationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formattedDate: String? = sdf.format(Date(uiState.calculationDate))

    Scaffold() { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Card() {
                Text("Стартовый взнос: ${uiState.initialAmount}",
                    Modifier.padding(20.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Срок вклада: ${uiState.periodMonths}",
                    Modifier.padding(20.dp, 0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Процентная ставка: ${uiState.interestRate}%",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Ежемесячное пополнение: ${uiState.monthlyTopUp}",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Итоговая сумма: ${String.format("%.2f", uiState.finalAmount)}",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Начисленные проценты: ${String.format("%.2f", uiState.interestEarned)}",
                    Modifier.padding(20.dp,  0.dp))
                Spacer(Modifier.padding(5.dp))
                Text("Дата и время рассчета: ${formattedDate}",
                    Modifier.padding(20.dp))
            }
            Button({
                viewModel.cleanAll()
                navScreens.navigate(Routes.MainScreen.route)
            }, modifier =  Modifier.padding(20.dp)) {
                Text("<- В начало")
            }
        }
    }
}
