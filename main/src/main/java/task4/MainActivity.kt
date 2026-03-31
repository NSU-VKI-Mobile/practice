package task4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CounterScreen()
                }
            }
        }
    }
}

@Composable
fun CounterScreen(

    viewModel: CounterViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Счетчик: ${uiState.count}", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(24.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.increment() }) {
                Text(text = "+")
            }
            Button(onClick = { viewModel.decrement() }) {
                Text(text = "-")
            }
            Button(onClick = { viewModel.reset() }) {
                Text(text = "Сброс")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "История действий (последние 5):", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))


        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(uiState.history) { historyItem ->
                Text(
                    text = historyItem,
                    modifier = Modifier.padding(4.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}