package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.navigation.Routes


@Composable
fun FirstScreenContent(navScreen: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val entryFee = remember{mutableStateOf("")}
        val depositPeriod = remember{mutableStateOf("")}
        Text("first screen")
        TextField(entryFee.value, label = {Text("Стартовый взнос")} ,onValueChange = {newText -> entryFee.value = newText}, modifier = Modifier.padding(10.dp))
        TextField(depositPeriod.value, label = {Text("Срок вклада в месяцах")} ,onValueChange = {newText -> depositPeriod.value = newText}, modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button({navScreen.navigate(Routes.MainScreen.route)}, modifier =  Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Black
                )) {
                Text("<- В начало")
            }
            Button({navScreen.navigate(Routes.SecondScreen.route)}, modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Black
                )) {
                Text("Далее ->")
            }
        }
    }
}
