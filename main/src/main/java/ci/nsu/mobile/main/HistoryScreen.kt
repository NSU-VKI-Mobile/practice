package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun HistoryScreen(
    vm: DepositViewModel
) {

    val history by
    vm.history.collectAsState()

    LazyColumn {

        items(history) {

            Card {

                Column {

                    Text(
                        "Взнос: ${it.initialAmount}"
                    )

                    Text(
                        "Итог: ${it.finalAmount}"
                    )
                }
            }
        }
    }
}