package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ViewModel.DepositViewModel

@Composable
fun Step2Screen(navController: NavController, viewModel: DepositViewModel) {
    val rate = viewModel.getAvailableRate() // Метод во ViewModel

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Этап 2: Дополнительные параметры", style = MaterialTheme.typography.titleLarge)

        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Text("Рекомендуемая ставка: $rate%", modifier = Modifier.padding(16.dp))
        }

        OutlinedTextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.monthlyTopUp = it },
            label = { Text("Ежемесячное пополнение") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            TextButton(onClick = { navController.popBackStack() }) { Text("Назад") }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                viewModel.onCalculateClicked() // Считаем перед переходом
                navController.navigate("result")
            }) { Text("Рассчитать") }
        }
    }
}