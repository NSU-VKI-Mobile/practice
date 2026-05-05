package ci.nsu.mobile.main.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@Composable
fun HistoryScreen(vm: DepositViewModel = viewModel()) {

    val list by vm.history.collectAsState(emptyList())

    LazyColumn {
        items(list) {
            Text("Сумма: ${it.initialAmount}, Итог: ${it.finalAmount}")
        }
    }
}