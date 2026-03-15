package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                ColorPaletteScreen()
            }
        }
    }
}

@Composable
fun ColorPaletteScreen() {
    val colors = remember {
        listOf(
            NamedColor("Red", Color(0xFFFF0000)),
            NamedColor("Orange", Color(0xFFFFA500)),
            NamedColor("Yellow", Color(0xFFFFFF00)),
            NamedColor("Green", Color(0xFF00FF00)),
            NamedColor("Blue", Color(0xFF0000FF)),
            NamedColor("Indigo", Color(0xFF4B0082)),
            NamedColor("Violet", Color(0xFF8F00FF))
        )
    }

    val colorMap = remember {
        colors.associate { it.name.lowercase() to it.color }
    }

    val (input, setInput) = remember { mutableStateOf("") }
    val (buttonColor, setButtonColor) = remember { mutableStateOf(Color(0xFF00FF00)) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = setInput,
                label = { Text("Green") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.Black
                ),
                onClick = {
                    val query = input.trim()
                    if (query.isNotEmpty()) {
                        val found = colorMap[query.lowercase()]
                        if (found == null) {
                            android.util.Log.d(
                                "ColorPalette",
                                "Пользовательский цвет \"$query\" не найден"
                            )
                        } else {
                            setButtonColor(found)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(text = "Применить цвет")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(colors) { item ->
                    ColorItemRow(item)
                }
            }
        }
    }
}

@Composable
fun ColorItemRow(color: NamedColor) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color.color)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = color.name,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

data class NamedColor(
    val name: String,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        ColorPaletteScreen()
    }
}