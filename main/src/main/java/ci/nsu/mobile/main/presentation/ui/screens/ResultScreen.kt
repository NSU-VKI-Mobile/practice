package ci.nsu.mobile.main.presentation.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.navigation.Screen
import ci.nsu.mobile.main.presentation.ui.viewmodel.DepositCalculationViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@Composable
fun ResultScreenContent(navToScreen: (String) -> Unit,
                        viewModel: DepositCalculationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formattedDate: String? = sdf.format(Date(uiState.calculationDate))
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
            Row() {
                Button({
                    viewModel.cleanAll()
                    navToScreen(Screen.MainScreen.route)
                }, modifier =  Modifier.padding(20.dp)) {
                    Text("<- На главный экран")
                }
                Button({
                    viewModel.saveEntity()
                    scope.launch {
                        snackbarHostState.showSnackbar("Расчёт сохранён!")
                    }
                }, modifier =  Modifier.padding(20.dp)) {
                    Text("Сохранить")
                }
            }
        }
    }
}
