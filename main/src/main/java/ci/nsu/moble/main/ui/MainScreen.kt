package ci.nsu.moble.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ci.nsu.moble.main.DepositViewModel
import ci.nsu.moble.main.Navigation

@Composable
fun MainScreen(nav: NavController, vm: DepositViewModel, onExit: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Расчёт вкладов",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(48.dp))

        Button(
            onClick = { vm.reset(); nav.navigate(Navigation.STEP1) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { nav.navigate(Navigation.HISTORY) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История расчётов")
        }
        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Закрыть приложение")
        }
    }
}