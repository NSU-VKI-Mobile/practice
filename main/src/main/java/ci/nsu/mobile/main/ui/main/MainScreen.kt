package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToCalculation: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    //val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { /*TODO: навигация к истории*/ }) {
            Text("История вкладов")
        }
        Button(onClick = { /*TODO: навигация к вкладам*/ }) {
            Text("Рассчитать вклад")
        }
        Button(onClick = { /*TODO: выход*/ }) {
            Text("Выйти")
        }
    }
}