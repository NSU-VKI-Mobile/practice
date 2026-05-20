package ci.nsu.mobile.main.ui.step2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    navController: NavController,
    viewModel: DepositViewModel
) {
    var topUp by remember { mutableStateOf("") }

    val months = viewModel.months.toIntOrNull() ?: 0

    val regex = Regex("^\\d*(\\.\\d{0,2})?$")

    val percentOptions = listOf("5", "10", "15")

    var selectedPercent by remember(viewModel.months) {
        mutableStateOf(
            when (viewModel.months.toIntOrNull() ?: 0) {
                in 0..5 -> "5"
                in 6..11 -> "10"
                else -> "15"
            }
        )
    }
    var expanded by remember { mutableStateOf(false) }
    var monthsR by remember { mutableStateOf(viewModel.months) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Шаг 2")

        Spacer(Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedPercent,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ставка") },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                percentOptions.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            selectedPercent = item
                            expanded = false
                            if (selectedPercent == "5") {
                                if (monthsR.toInt() > 5)
                                    monthsR = "5"
                            }
                            if (selectedPercent == "10") {
                                if (monthsR.toInt() < 6)
                                    monthsR = "6"
                                else if (monthsR.toInt() > 11)
                                    monthsR = "11"
                            }
                            if (selectedPercent == "15") {
                                if (monthsR.toInt() < 12)
                                    monthsR = "12"
                            }
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = monthsR,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() } && it.length <= 2) {
                    monthsR = it
                }
                selectedPercent =
                    when (monthsR.toIntOrNull() ?: 0) {
                        in 0..5 -> "5"
                        in 6..11 -> "10"
                        else -> "15" }
            },
            label = { Text("Срок") }
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = topUp,
            onValueChange = {
                if ((it.isEmpty() || it.matches(regex)) && it.length <= 7) {
                    topUp = it
                }
            },
            label = { Text("Ежемесячное пополнение") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.months = monthsR
            viewModel.percent = selectedPercent
            viewModel.topUp = topUp
            navController.navigate("result")
        }) {
            Text("Рассчитать")
        }
    }
}