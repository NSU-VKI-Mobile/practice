package ci.nsu.mobile.main.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {

    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Column(
        modifier = Modifier.fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Расчёт вкладов")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("step1")
        }) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("history")
        }) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            dispatcher?.onBackPressed()
        }) {
            Text("Закрыть")
        }
    }
}