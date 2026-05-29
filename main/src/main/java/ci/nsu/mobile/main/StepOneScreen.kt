package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StepOneScreen(
    vm: DepositViewModel,
    nav: NavController
) {

    Column(
        modifier =
            Modifier.padding(20.dp)
    ) {

        OutlinedTextField(

            value = vm.initialAmount,

            onValueChange = {

                vm.initialAmount = it

            },

            label = {

                Text("Стартовый взнос")

            }
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = vm.months,

            onValueChange = {

                vm.months = it

            },

            label = {

                Text("Срок (месяцы)")

            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            onClick = {

                if (
                    vm.initialAmount.isNotBlank()
                    &&
                    vm.months.isNotBlank()
                ) {

                    nav.navigate(
                        Screen.Step2.route
                    )
                }
            }
        ) {

            Text("Далее")
        }
    }
}