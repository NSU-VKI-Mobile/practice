package ci.nsu.mobile.main
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CounterScreen()
        }
    }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {

        Text("Счетчик: ${uiState.count}")

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.increment() }) {
                Text("+")
            }
            Button(onClick = { viewModel.decrement() }) {
                Text("-")
            }
            Button(onClick = { viewModel.reset() }) {
                Text("Сброс")

            }
            Button(onClick = { viewModel.clearHistory() }) {
                Text("Очистить историю")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("История:")

        LazyColumn {
            items(uiState.history) {
                Text(it)
            }
        }

    }

}