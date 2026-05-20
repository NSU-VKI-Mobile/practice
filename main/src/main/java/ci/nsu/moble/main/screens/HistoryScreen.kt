package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.viewmodel.DepositViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    vm: DepositViewModel
) {

    val list by vm.history.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История расчётов") },
                actions = {
                    OutlinedButton (
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Назад")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            items(list) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    onClick = {
                        navController.navigate("details/${item.id}")
                    }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Взнос: ${item.initialAmount}")
                        Text("Итог: ${item.finalAmount}")
                    }
                }
            }
        }
    }
}