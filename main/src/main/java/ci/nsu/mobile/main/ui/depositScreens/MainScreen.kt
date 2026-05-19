package ci.nsu.mobile.main.ui.depositScreens

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(navToScreen: (String) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title= { Text("Расчет вкладов", fontSize = 22.sp)})
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val activity = LocalActivity.current
            Button({navToScreen(Screen.FirstScreen.route)},
                modifier =  Modifier.fillMaxWidth().padding(10.dp).width(250.dp)) {
                Text("Рассчитать")
            }
            Button({navToScreen(Screen.HistoryScreen.route)},
                modifier = Modifier.fillMaxWidth().padding(10.dp).width(250.dp)) {
                Text("История расчетов")
            }
            Button({
                        activity?.finish()
                   }, modifier = Modifier.fillMaxWidth().padding(10.dp).width(250.dp)) {
                Text("Закрыть приложение")
            }
        }
    }

}
