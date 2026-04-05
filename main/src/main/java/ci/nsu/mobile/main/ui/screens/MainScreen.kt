package ci.nsu.mobile.main.ui.screens

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
import androidx.navigation.NavController
import ci.nsu.mobile.main.navigation.Routes


@Composable
fun MainScreenContent(navToScreen: NavController) {
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val activity = LocalActivity.current
            Button({navToScreen.navigate(Routes.FistScreen.route)}, modifier =  Modifier.fillMaxWidth().padding(10.dp)) {
                Text("Рассчитать")
            }
            Button({navToScreen.navigate(Routes.HistoryScreen.route)}, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text("История расчетов")
            }
            Button({activity?.finish()}, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text("Закрыть приложение")
            }
        }
    }

}
