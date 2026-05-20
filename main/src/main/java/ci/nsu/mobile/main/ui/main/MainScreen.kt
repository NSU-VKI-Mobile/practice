package ci.nsu.mobile.main.ui.main

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    val activity = androidx.compose.ui.platform.LocalContext.current as? android.app.Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    navController.navigate("step1")
                }
            ) {
                Text("Рассчитать")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("history")
                }
            ) {
                Text("История расчётов")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    activity?.finish()
                }
            ) {
                Text("Закрыть приложение")
            }
        }
    }
}