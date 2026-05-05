package ci.nsu.mobile.main.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@Composable
fun ResultScreen(navController: NavController, vm: DepositViewModel = viewModel()) {

    val result = vm.calculateResult()

    Column {
        Text("Итог: ${result.first}")
        Text("Проценты: ${result.second}")

        Button(onClick = { navController.navigate("main") }) {
            Text("В начало")
        }
    }
}