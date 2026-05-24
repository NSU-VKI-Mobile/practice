package ci.nsu.mobile.main.ui.depositScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(navToScreen: (String) -> Unit) {
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button({navToScreen(Screens.FirstScreen.route)},
                modifier =  Modifier.fillMaxWidth().padding(10.dp).width(250.dp)) {
                Text("Новый расчет")
            }
        }
    }
}
