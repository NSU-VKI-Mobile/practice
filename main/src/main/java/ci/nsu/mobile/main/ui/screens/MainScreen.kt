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
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: DepositViewModel) {
    val context = LocalContext.current
    Scaffold(
        topBar = { TopAppBar(title = { Text("Расчёт вкладов") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { viewModel.reset(); navController.navigate("step1") }, modifier = Modifier.fillMaxWidth(0.7f)) {
                Text("Рассчитать")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("history") }, modifier = Modifier.fillMaxWidth(0.7f)) {
                Text("История расчётов")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { (context as? Activity)?.finish() }, modifier = Modifier.fillMaxWidth(0.7f)) {
                Text("Закрыть приложение")
            }
        }
    }
}