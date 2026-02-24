package ci.nsu.moble.main


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.MainColors
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity_task_2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    ColorInputScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}


fun ColorInputScreen(modifier: Modifier = Modifier) {
    var color_name by remember { mutableStateOf("") }
    val primary_color = MaterialTheme.colorScheme.primary
    var button_color by remember { mutableStateOf(primary_color) }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        TextField(
            value = color_name,
            onValueChange = { newText -> color_name = newText },
            label = { Text(text = "Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val down_reg = color_name.lowercase()
                val select_color = MainColors[down_reg]
                if (select_color != null) {
                    button_color = select_color
                    Log.d("Поиск цвета", "Цвет найден: $down_reg")
                } else {
                    Log.d("Поиск цвета", "Цвет не найден: $down_reg")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = button_color)
        ) {
            Text("Окрасить в цвет")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column {

            for ((colorName, colorValue) in MainColors) {

                Spacer(modifier = Modifier.height(8.dp))

                // Создаем бокс с цветом
                Box(
                    modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(8.dp))
                        .background(colorValue).padding(8.dp)
                ) {
                    Text(
                        text = colorName, color = Color.White
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable

fun ColorInputScreenPreview() {
    PracticeTheme {
        ColorInputScreen()
    }
}
//fun GreetingPreview() {
//    PracticeTheme {
//        Greeting("Android")
//    }
//}