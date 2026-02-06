package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.theme.Blue
import ci.nsu.moble.main.ui.theme.Cyan
import ci.nsu.moble.main.ui.theme.Green
import ci.nsu.moble.main.ui.theme.Magenta
import ci.nsu.moble.main.ui.theme.PracticeTheme
import ci.nsu.moble.main.ui.theme.Red
import ci.nsu.moble.main.ui.theme.Yellow
import androidx.compose.runtime.*
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val colorsMap = mapOf(
    "Red" to Red,
    "Green" to Green,
    "Blue" to Blue,
    "Yellow" to Yellow,
    "Cyan" to Cyan,
    "Magenta" to Magenta,
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun StringInColor(str: String): Color
{
    val color = colorsMap.get(str)
    if (color == null)
    {
        println("Нет такого цвета")
        return Red
    }
    else
        return color
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var inputColor by remember { mutableStateOf("Red") }
    var buttonColor by remember { mutableStateOf(StringInColor(inputColor)) }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = inputColor,
            onValueChange = { newText -> inputColor = newText },
            label = { Text("Введите текст") }
        )
        Button(
            onClick = { buttonColor = StringInColor(inputColor) },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            Text("Применить цвет")
        }
        colorsMap.forEach { key,value ->
            Text(
                text = key
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting("Android")
    }
}