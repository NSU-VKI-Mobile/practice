package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val colorsMap = mapOf(
    "red" to Red,
    "green" to Green,
    "blue" to Blue,
    "yellow" to Yellow,
    "cyan" to Cyan,
    "magenta" to Magenta,
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                    Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {

    var inputColor by remember { mutableStateOf("Red") }
    var buttonColor by remember { mutableStateOf(colorsMap[inputColor.lowercase()]) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = inputColor,
            onValueChange = { newText -> inputColor = newText },
            label = { Text("Введите текст") }
        )
        Button(
            onClick = {
                val foundColor = colorsMap[inputColor.lowercase()]
                if (foundColor != null) {
                    buttonColor = foundColor
                } else {
                    Log.d("ColorFinder", "Пользовательский цвет не найден")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor!!
            )
        ) {
            Text("Применить цвет")
        }
        LazyColumn {
            items(colorsMap.toList()) { (colorName, color) ->
                ColorPaletteItem(colorName, color)
            }
        }
    }
}
    @Composable
    fun ColorPaletteItem(colorName: String, color: Color) {
        Box(
            modifier = Modifier
                .requiredWidth(200.dp)
                .background(color, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
        ) {
            Text(text = colorName)
        }
    }





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting()
    }
}