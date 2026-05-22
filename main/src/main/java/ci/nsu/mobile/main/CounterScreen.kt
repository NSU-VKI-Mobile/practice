import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Счетчик: ${uiState.count}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = { viewModel.increment() }//iewModel меняет state  StateFlow обновляется Compose перерисовывает UI

            ) {
                Text("+")
            }

            Button(
                onClick = { viewModel.decrement() }
            ) {
                Text("-")
            }

            Button(
                onClick = { viewModel.reset() }
            ) {
                Text("Сброс")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "История:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(uiState.history) { item ->

                Text(
                    text = item,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

