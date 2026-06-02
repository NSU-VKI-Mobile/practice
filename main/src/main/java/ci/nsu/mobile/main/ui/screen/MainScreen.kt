package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToInput: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val viewModel: MainViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Button(
                onClick = onNavigateToInput,
                modifier = Modifier.width(200.dp)
            ) {
                Text("Рассчитать")
            }

            Button(
                onClick = onNavigateToHistory,
                modifier = Modifier.width(200.dp)
            ) {
                Text("История расчётов")
            }

            Button(
                onClick = { viewModel.exitApp() },
                modifier = Modifier.width(200.dp)
            ) {
                Text("Закрыть приложение")
            }
        }
    }
}