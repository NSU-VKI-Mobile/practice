package ci.nsu.moble.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CounterContent(
        modifier = modifier,
        uiState = uiState,
        onIncrement = viewModel::increment,
        onDecrement = viewModel::decrement,
        onReset = viewModel::reset,
        onClear = viewModel::clear
    )
}

@Composable
fun CounterContent(
    modifier: Modifier = Modifier,
    uiState: CounterUiState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onClear: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Счётчик", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "${uiState.count}", fontSize = 64.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onDecrement) { Text("-") }
            Button(onClick = onReset)    { Text("Сброс") }
            Button(onClick = onIncrement) { Text("+") }
        }

        Button(onClick = onClear)    { Text("Очистить историю") }

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "История:", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.history.isEmpty()) {
            Text(text = "-")
        } else {
            LazyColumn {
                itemsIndexed(uiState.history) { index, action ->
                    Text(
                        text = "${index + 1}. $action",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    MaterialTheme {
        CounterContent(
            uiState = CounterUiState(
                count = 3,
                history = listOf("+1 → итого: 3", "-1 → итого: 2", "Сброс → итого: 0")
            ),
            onIncrement = {},
            onDecrement = {},
            onReset = {},
            onClear = {}
        )
    }
}