package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.ui.theme.Green40
import ci.nsu.moble.main.ui.theme.PracticeTheme
import ci.nsu.moble.main.ui.theme.Purple80


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ColorPicker()
        }
    }
}

@Composable
fun ColorPicker() {
    var input by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf<Color?>(null) }

    val availableColors: Map<String, Color> = mapOf(
        "red" to Color(0xFFFF0000),
        "green" to Color(0xFF00FF00),
        "blue" to Color(0xFF0000FF),
        "yellow" to Color(0xFFFFFF00),
        "cyan" to Color(0xFF00FFFF),
        "pink" to Color(0xFFFF00FF),
        "white" to Color(0xFFFFFFFF),
        "gray" to Color(0xFF888888),
        "orange" to Color(0xFFFF9800)
    )

    Column(modifier = Modifier.padding(16.dp)) {

        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (availableColors[input] != null) {
                    buttonColor = availableColors[input]
                } else {
                    Log.d("ColorPicker", "Пользовательский цвет '$input' не найден")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor ?: MaterialTheme.colorScheme.primary)
        ) {
            Text("Применить цвет")
        }

        for (color in availableColors)
        {
            Text(color.key,
                Modifier.background(color.value, shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .size(height = 40.dp, width = 20.dp)
                    .padding(start = 10.dp, top = 5.dp, end = 0.dp, bottom = 0.dp)
            )
        }
    }
}