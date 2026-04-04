package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ci.nsu.mobile.main.navigation.Routes
import ci.nsu.mobile.main.viewmodel.DepositCalculationViewModel


@Composable
fun SecondScreenContent(navScreens: NavController,
                        viewModel: DepositCalculationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val checkState = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(true) }
    val interestRate = remember{mutableStateOf(0)}

        Scaffold() {innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AssistChip(onClick = {}, label = {Text("15%")})
                    AssistChip(onClick = {}, label = {Text("10%")})
                    AssistChip(onClick = {}, label = {Text("5%")})
                }
                Row(Modifier.fillMaxWidth()) {
                    Checkbox(checked = checkState.value, onCheckedChange = {checkState.value = it})
                    Text("Ежемесячное пополнение")
                }
                if (checkState.value) {
                    TextField(uiState.monthlyTopUp.toString(), label = {Text("Ежемесячное пополнение")},
                        onValueChange = {})
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Button(
                        {navScreens.navigate(Routes.FistScreen.route)}, modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.Black
                        )
                    ) {
                        Text("Назад")
                    }
                    Button(
                        {navScreens.navigate(Routes.ResultScreen.route)}, modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.Black
                        )
                    ) {
                        Text("Рассчитать")
                    }
                }
            }

    }
}
