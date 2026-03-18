package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.DepositViewModel
import ci.nsu.mobile.main.ui.Screen

@Composable
fun MainScreen(viewModel: DepositViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Расчёт вкладов",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.navigateTo(Screen.Stage1) }
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.loadAllCalculations()
                viewModel.navigateTo(Screen.History)
            }
        ) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        ) {
            Text("Закрыть приложение")
        }
    }
}