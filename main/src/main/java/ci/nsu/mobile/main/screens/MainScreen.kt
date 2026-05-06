package ci.nsu.mobile.main.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {

    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Расчёт вкладов", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate("step1") }) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { navController.navigate("history") }) {
            Text("История расчётов")
        }

//        Spacer(modifier = Modifier.height(10.dp))
//
//        Button(onClick = { activity.finish() }) {
//            Text("Закрыть приложение")
//        }
    }
}