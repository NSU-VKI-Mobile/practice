package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.theme.PracticeTheme

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Счетчик: ${uiState.count}",
            fontSize = 32.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.increment() }) {
                Text("+")
            }
            Button(onClick = { viewModel.decrement() }) {
                Text("-")
            }
            Button(onClick = { viewModel.reset() }) {
                Text("Сброс")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "История последних 5 действий:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(uiState.history) { action ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = action,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}