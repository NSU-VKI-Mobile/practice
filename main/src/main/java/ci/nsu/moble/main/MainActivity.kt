package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CounterScreen()
            }
        }
    }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Значение счетчика
        Text(
            text = uiState.count.toString(),
            fontSize = 48.sp,
            modifier = Modifier.padding(32.dp)
        )

        // Кнопки управления
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.decrement() },
                modifier = Modifier.weight(1f)
            ) {
                Text("-", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.reset() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сброс")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.increment() },
                modifier = Modifier.weight(1f)
            ) {
                Text("+", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок истории
        Text(
            text = "История последних 5 действий:",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Список истории
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = uiState.history,
                key = { it } // Добавляем key для стабильности
            ) { action ->
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