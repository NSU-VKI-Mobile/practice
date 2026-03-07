package ci.nsu.moble.main

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.ui.theme.PracticeTheme

@Composable
fun CounterScreen (viewModel: CounterVM = viewModel())
{
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "${state.count}",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(vertical = 32.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        )
        {
            Button(onClick = {viewModel.increment()}) {Text("+")}
            Button(onClick = {viewModel.decrement()}) {Text("-")}
            Button(onClick = {viewModel.reset()}) {Text("Сброс")}
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "История действий:", style = MaterialTheme.typography.titleMedium)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth())
        {
            items(state.history)
            {
                action ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                {
                    Text(
                        text = action,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )

                }
            }
        }

    }
}