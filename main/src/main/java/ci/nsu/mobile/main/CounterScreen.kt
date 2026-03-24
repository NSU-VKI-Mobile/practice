package ci.nsu.mobile.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.stringResource

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val counterTitle = stringResource(R.string.counter_title, uiState.count)
    val decrementText = stringResource(R.string.button_decrement)
    val resetText = stringResource(R.string.button_reset)
    val incrementText = stringResource(R.string.button_increment)
    val historyTitle = stringResource(R.string.history_title)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = counterTitle,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.decrement() }) {
                Text(decrementText)
            }
            Button(onClick = { viewModel.reset() }) {
                Text(resetText)
            }
            Button(onClick = { viewModel.increment() }) {
                Text(incrementText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(historyTitle)

        Column {
            for (action in uiState.history) {
                Text(
                    text = action,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}