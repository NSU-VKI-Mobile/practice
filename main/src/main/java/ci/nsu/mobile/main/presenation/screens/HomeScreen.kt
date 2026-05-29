package ci.nsu.mobile.main.presenation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.presenation.navigation.Screen
import kotlin.system.exitProcess

@Composable
fun HomeScreen(
    nav: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = {

                nav.navigate(
                    Screen.Step1.route
                )

            }
        ) {

            Text("Рассчитать")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {

                nav.navigate(
                    Screen.History.route
                )

            }
        ) {

            Text("История расчётов")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = {

                exitProcess(0)

            }
        ) {

            Text("Закрыть приложение")
        }
    }
}