package ci.nsu.moble.main.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = { nav.navigate("step1") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { nav.navigate("history") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("История расчётов")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = { (context as Activity).finishAffinity() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Закрыть приложение")
            }
        }
    }
}