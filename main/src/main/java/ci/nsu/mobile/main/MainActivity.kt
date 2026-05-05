package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.ui.theme.DraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // устанавливаем Compose-интерфейс
            DraftTheme { // применяем тему оформления
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToInput: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Расчёт вкладов",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onNavigateToInput,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateToHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История расчётов")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                android.os.Process.killProcess(android.os.Process.myPid())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Закрыть приложение")
        }
    }
}