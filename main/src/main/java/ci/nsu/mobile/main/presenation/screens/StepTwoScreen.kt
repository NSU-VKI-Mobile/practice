package ci.nsu.mobile.main.presenation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.presenation.viewmodels.DepositViewModel
import ci.nsu.mobile.main.presenation.navigation.Screen

@Composable
fun StepTwoScreen(
    vm: DepositViewModel,
    nav: NavController
) {

    val months =
        vm.months.toIntOrNull() ?: 0

    val rate =
        when {

            months < 6 -> 15.0

            months < 12 -> 10.0

            else -> 5.0
        }

    vm.selectedRate = rate

    Column(
        modifier =
            Modifier.padding(20.dp)
    ) {

        Text(
            text =
                "Процентная ставка: $rate%"
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        OutlinedTextField(

            value =
                vm.monthlyTopUp,

            onValueChange = {

                vm.monthlyTopUp = it

            },

            label = {

                Text("Пополнение")

            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Row {

            Button(
                onClick = {

                    nav.popBackStack()

                }
            ) {

                Text("Назад")
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Button(
                onClick = {

                    nav.navigate(
                        Screen.Result.route
                    )

                }
            ) {

                Text("Рассчитать")
            }
        }
    }
}