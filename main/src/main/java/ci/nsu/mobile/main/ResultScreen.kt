package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    vm: DepositViewModel,
    nav: NavController
) {

    val result =
        remember {

            vm.calculate()

        }

    Column(
        modifier =
            Modifier.padding(20.dp)
    ) {

        Card {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Text(
                    "Стартовый взнос: ${result.initialAmount}"
                )

                Text(
                    "Срок: ${result.periodMonths}"
                )

                Text(
                    "Ставка: ${result.interestRate}%"
                )

                Text(
                    "Итог: ${result.finalAmount}"
                )

                Text(
                    "Проценты: ${result.interestEarned}"
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            onClick = {

                vm.save(result)

            }
        ) {

            Text("Сохранить")
        }

        Button(
            onClick = {

                nav.navigate(
                    Screen.Home.route
                )

            }
        ) {

            Text("В начало")
        }
    }
}