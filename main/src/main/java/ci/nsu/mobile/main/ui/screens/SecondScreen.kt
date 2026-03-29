package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.Routes
import ci.nsu.mobile.main.ui.theme.PracticeTheme


@Composable
fun SecondScreenContent(navScreens: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var expanded by remember { mutableStateOf(true) }
        val interestRate = remember{mutableStateOf(0)}
        DropdownMenu(expanded = expanded,
            onDismissRequest = {expanded = false},
            offset = DpOffset(x = 20.dp, y = 50.dp)) {
            DropdownMenuItem(
                onClick = {interestRate.value = 15},
                text = { Text("15%") }
            )
            DropdownMenuItem(
                onClick = {interestRate.value = 10},
                text = { Text("10%") }
            )
            DropdownMenuItem(
                onClick = {interestRate.value = 5},
                text = { Text("5%") }
            )
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
