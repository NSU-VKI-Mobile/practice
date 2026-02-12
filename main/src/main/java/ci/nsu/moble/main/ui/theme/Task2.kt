package ci.nsu.moble.main.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.ui.theme.Blue1
import ci.nsu.moble.main.ui.theme.ui.theme.Green1
import ci.nsu.moble.main.ui.theme.ui.theme.PracticeTheme
import ci.nsu.moble.main.ui.theme.ui.theme.Red1

private val colorsMap = mapOf(
    "Red" to Red1,
    "Blue" to Blue1,
    "Green" to Green1
)

class Task2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                    Main()

            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    var textColor by remember { mutableStateOf("") }
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = textColor,
                onValueChange = { newText -> textColor = newText },
                label = { Text("Введите цвет") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    println("Применяем цвет: $textColor")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Применить цвет")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Main()
    }
}