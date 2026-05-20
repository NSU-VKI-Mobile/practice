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

    val percentOptions = when {
        months < 6 -> listOf("15")
        months < 12 -> listOf("10")
        else -> listOf("5")
    }

    var selectedPercent by remember { mutableStateOf(percentOptions.first()) }
    var expanded by remember { mutableStateOf(false) }

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
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = topUp,
            onValueChange = {
                if (it.all { ch -> ch.isDigit() } && it.length <= 7) {
                    topUp = it
                }
            },
            label = { Text("Ежемесячное пополнение") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.percent = selectedPercent
            viewModel.topUp = topUp
            navController.navigate("result")
        }) {
            Text("Рассчитать")
        }
    }
}