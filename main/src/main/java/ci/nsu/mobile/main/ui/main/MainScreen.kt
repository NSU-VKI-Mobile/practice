package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    val state = viewModel.uiState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = state.count.toString(), fontSize = 100.sp)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Button(onClick = { viewModel.increment() }) {
                    Text("+")
                }
                Button(onClick = { viewModel.decrement() }) {
                    Text("-")
                }
                Button(onClick = { viewModel.reset() }) {
                    Text("Reset")
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(state.history) { item ->
                Text(text = item, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }

        Button(
            onClick = { viewModel.clearHistory() },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Clear history")
        }
    }
}