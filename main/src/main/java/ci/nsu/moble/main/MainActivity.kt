package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import ci.nsu.moble.main.ui.main.MainViewModel
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }

    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
    ) {
        Text(text = "счет ${uiState.count}")
        Row(verticalAlignment = Alignment.Bottom){
            Button(
                onClick = { viewModel.decrement() },
                modifier = Modifier.weight(1f),
                content = { Text("-") }
            )
            Button(
                onClick = { viewModel.increment() },
                modifier = Modifier.weight(1f),
                content = { Text("+") }
            )
            Button(
                onClick = { viewModel.reset() },
                modifier = Modifier.weight(1f),
                content = { Text("reset") }
            )
        }
        Text(
            text = "Последние 5 действий:"
        )
        LazyColumn(modifier= Modifier.padding(vertical = 10.dp)) {
            items(uiState.history) { action ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                ) {
                    Text(
                        text = action,
                        modifier = Modifier.padding(12.dp)
                    )
                    }
                }
            }

        }
}