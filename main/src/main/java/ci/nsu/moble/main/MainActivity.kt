package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.theme.PracticeTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import android.util.Log
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxWidth



val colorMap = mapOf(
    "red" to Color.Red,
    "green" to Color.Green,
    "blue" to Color.Blue,
    "yellow" to Color.Yellow,
    "black" to Color.Black
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = Color.White
                    ) {
                        ColorScreen()
                    }
                }
            }
        }
    }
}

@Composable

fun ColorScreen(){
    var text by remember { mutableStateOf("")}
    var buttonColor by remember {mutableStateOf(Color.Gray)}

    Column(
        modifier = Modifier.padding(16.dp)
    ){
        TextField(
            value = text,
            onValueChange = {newValue ->
                text = newValue
            },
            label = {Text("Введите цвет")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            onClick = {
                val foundColor = colorMap[text.lowercase()]

                if (foundColor != null){
                    buttonColor = foundColor
                } else{
                    Log.d("ColorSearch", "Цвет \"$text\" не найден")
                }
            }){
            Text("Найти цвет")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text="Доступные цвета:",
            color = Color.Black)

        colorMap.keys.forEach { colorName ->
            Text(
                text = colorName,
                color = Color.Black)
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting("Android")
    }
}