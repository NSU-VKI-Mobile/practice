package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Main()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Main() {
    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    val colorsMap = mapOf(
        "red" to Color(0xFFFF0000),
        "orange" to Color(0xFFFFA500),
        "yellow" to Color(0xFFFFEB3B),
        "green" to Color(0xFF00FF00),
        "blue" to Color(0xFF2196F3),
        "indigo" to Color(0xFF4B0082),
        "violet" to Color(0xFF9400D3)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5)
            )
        )

        val buttonBrush = Brush.horizontalGradient(
            colors = listOf(buttonColor, buttonColor.copy(alpha = 0.85f))
        )

        Button(
            onClick = {
                val colorName = text.trim().lowercase()
                val foundColor = colorsMap[colorName]

                if (foundColor != null) {
                    buttonColor = foundColor
                } else {
                    Log.d("MainActivity", "пользовательский цвет \"$colorName\" не найден")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(buttonBrush, RoundedCornerShape(50.dp)),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Применить цвет",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorsMap.toList()) { (name, color) ->
                val barBrush = Brush.horizontalGradient(
                    colors = listOf(color, color.copy(alpha = 0.9f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(barBrush, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.replaceFirstChar { it.uppercase() },
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}