package ci.nsu.mobile.main.presentation.ui.screens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.navigation.Screen


@Composable
fun MainScreenContent(navToScreen: (String) -> Unit) {
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val activity = LocalActivity.current
            Button({navToScreen(Screen.FirstScreen.route)}, modifier =  Modifier.fillMaxWidth().padding(10.dp)) {
                Text("Рассчитать")
            }
            Button({navToScreen(Screen.HistoryScreen.route)}, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text("История расчетов")
            }
            Button({
                        activity?.finish()

                   }, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text("Закрыть приложение")
            }
        }
    }

}
