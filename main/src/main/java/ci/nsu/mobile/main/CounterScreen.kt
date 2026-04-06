package ci.nsu.mobile.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(modifier: Modifier = Modifier, viewModel: CounterViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Счётчик: ${uiState.count}",
            fontSize = 32.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(onClick = { viewModel.increment() }, modifier = Modifier.weight(1f)) {
                Text("+")
            }
            Button(onClick = { viewModel.decrement() }, modifier = Modifier.weight(1f)) {
                Text("-")
            }
            Button(onClick = { viewModel.reset() }, modifier = Modifier.weight(1f)) {
                Text("Сброс")
            }
        }

        Text(
            text = "История:",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Divider()

        LazyColumn {
            items(uiState.history) { entry ->
                Text(
                    text = entry,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
