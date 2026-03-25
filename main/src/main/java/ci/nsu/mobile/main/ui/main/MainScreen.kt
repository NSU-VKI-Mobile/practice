package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {

    val state = viewModel.uiState.collectAsState().value

    Column (
        modifier = Modifier.fillMaxSize().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state.count.toString(), fontSize = 100.sp)

        Button(onClick = { viewModel.increment() }) {
            Text("+")
        }

        Button(onClick = { viewModel.decrement() }) {
            Text("-")
        }

        Button(onClick = { viewModel.reset() }) {
            Text("Reset")
        }

        LazyColumn {
            items(state.history) { item ->
                Text(text = item)
            }
        }
    }
}