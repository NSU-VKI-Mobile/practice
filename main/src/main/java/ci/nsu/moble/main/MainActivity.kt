// Task_2: Compose-экран с полем ввода, кнопкой Apply и палитрой цветов.
// При нажатии на Apply ищет цвет в ColorRepository по введённому имени.
// Если найден – кнопка перекрашивается, если нет – пишет в Logcat.

package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ColorSearchScreen()
            }
        }
    }
}

@Composable
fun ColorSearchScreen() {
    var inputText by remember { mutableStateOf("") }
    var buttonBgColor by remember { mutableStateOf<Color?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter color name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val result = ColorRepository.findColor(inputText)
                if (result != null) {
                    buttonBgColor = result.color
                    Log.d("ColorSearch", "Color '${inputText}' found, applying")
                } else {
                    Log.d("ColorSearch", "Color '${inputText}' not found")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBgColor ?: MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Apply", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ColorRepository.colors) { entry ->
                val textColor = if (entry.color.red + entry.color.green + entry.color.blue > 2f)
                    Color.Black else Color.White

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp)
                        .background(entry.color, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = entry.name,
                        fontSize = 18.sp,
                        color = textColor
                    )
                }
            }
        }
    }
}
