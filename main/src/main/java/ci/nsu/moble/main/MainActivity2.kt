package ci.nsu.moble.main
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleColorScreen()
        }
    }
}

@Composable
fun SimpleColorScreen() {

    val myColors = mapOf(
        "red" to Color.Red,
        "Orange" to Color(0xFFFF9800),
        "Green" to Color.Green,
        "Blue" to Color.Blue,
        "Yellow" to Color.Yellow,
        "Violet" to Color(0xFF9C27B0),
        "Cyan" to Color.Cyan
    )

    var inputText by remember { mutableStateOf("") }
    var btnColor by remember { mutableStateOf(Color.Green) }


    var errorMessage by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val colorKey = myColors.keys.find { it.equals(inputText.trim(), ignoreCase = true) }

                if (colorKey != null) {

                    btnColor = myColors[colorKey]!!
                    errorMessage = "" // Очищаем сообщение об ошибке, так как всё хорошо
                } else {

                    errorMessage = "Цвет \"$inputText\" не найден в палитре!"
                    Log.d("colorssss",errorMessage)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
        ) {
            Text("Применить цвет")
        }




        Spacer(modifier = Modifier.height(24.dp))


        myColors.forEach { (name, color) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,

                    color = if (color == Color.Yellow || color == Color.Cyan) Color.Black else Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}