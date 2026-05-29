package ci.nsu.mobile.main.presenation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import ci.nsu.mobile.main.presenation.viewmodels.DepositViewModel

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