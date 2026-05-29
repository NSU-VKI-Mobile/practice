package ci.nsu.mobile.main.presenation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.presenation.navigation.Screen
import ci.nsu.mobile.main.presenation.viewmodels.DepositViewModel

@Composable
fun HistoryScreen(
    vm: DepositViewModel
) {

    val history by vm.history.collectAsState()

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)

    ) {



        items(history) {

            Card(

                modifier =
                    Modifier.fillMaxWidth()

            ) {

                Column(

                    modifier =
                        Modifier.padding(16.dp)

                ) {

                    Text(
                        "Взнос: ${it.initialAmount}"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        "Итог: ${it.finalAmount}"
                    )
                }
            }
        }
    }
}