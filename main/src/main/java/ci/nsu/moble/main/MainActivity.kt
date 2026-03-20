package ci.nsu.moble.main
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColorSearchScreen()
                }
            }
        }
    }
}

private val colorMap = mapOf(
    "Red" to Color.Red,
    "Orange" to Color(0xFFFFA500),
    "Yellow" to Color.Yellow,
    "Green" to Color.Green,
    "Blue" to Color.Blue,
    "Indigo" to Color(0xFF4B0082),
    "Violet" to Color(0xFFEE82EE)
)

@Composable
fun ColorSearchScreen() {
    var inputText by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf<Color?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Введите название цвета (на англ.)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val foundColor = colorMap[inputText]
                if (foundColor != null) {
                    buttonColor = foundColor
                } else {
                    buttonColor = null
                    Toast.makeText(
                        context,
                        "Пользовательский цвет \"$inputText\" не найден",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor ?: MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                "Применить цвет",
                fontSize = 16.sp,
                color = if (buttonColor != null && buttonColor != Color.Yellow && buttonColor != Color(0xFFFFA500))
                    Color.White
                else
                    Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Палитра цветов:",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colorMap.toList()) { (colorName, colorValue) ->
                ColorListItem(colorName, colorValue)
            }
        }
    }
}

@Composable
fun ColorListItem(colorName: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = colorName,
                fontSize = 16.sp,
                color = if (color != Color.Yellow && color != Color(0xFFFFA500))
                    Color.White
                else
                    Color.Black
            )
        }
    }
}