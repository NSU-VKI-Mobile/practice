package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.screen.HomeScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                DepositApp(onExit = this::finish)
            }
        }
    }
}

@Composable
fun DepositApp(onExit: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        HomeScreen(
            onCalculateClick = {
                //TODO  реализовать переход к первому этапу ввода данных
            },
            onHistoryClick = {
                //TODO  реализовать открытие списка сохранённых расчётов
            },
            onExitClick = onExit
        )
    }
}