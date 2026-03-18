package ci.nsu.moble.people

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import ci.nsu.moble.main.Colors

class MainActivity : ComponentActivity() {

    private val colorList = listOf(
        Colors("red", Color.Red),
        Colors("green", Color.Green),
        Colors("blue", Color.Blue),
        Colors("black", Color.Black),
        Colors("white", Color.White),
        Colors("yellow", Color.Yellow)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(colorList)
        }
    }
}

@Composable
fun MainScreen(colors: List<Colors>) {

    var input by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Введите цвет") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val found = colors.find {
                    it.name.equals(input.trim(), ignoreCase = true)
                }

                if (found != null) {
                    buttonColor = found.color
                } else {
                    Log.d("COLOR_SEARCH", "Цвет \"$input\" не найден")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = if (buttonColor == Color.White) Color.Black else Color.White
            )
        ) {
            Text("Найти цвет")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Палитра:")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colors) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.color)
                ) {
                    Text(
                        text = item.name,
                        color = if (item.color == Color.Black) Color.White else Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}