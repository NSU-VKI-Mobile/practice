package ci.nsu.moble.main

import android.nfc.Tag
import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign

private const val TAG = "MainActivity"

private val Red = Color.Red
private val Blue = Color.Blue
private val Green = Color.Green
private val Black = Color.Black
private val Cyan = Color.Cyan
private val Yellow = Color.Yellow

private val colorsMap = mapOf(
    "Red" to  Red,
    "Blue" to Blue,
    "Green" to Green,
    "Black" to Black,
    "Cyan" to Cyan,
    "Yellow" to Yellow
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            MaterialTheme{
                Main()
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color.Gray) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
    ) {

        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth(),
            label = { Text("Введите цвет") }
        )

        Button( onClick = {
            val foundColor = colorsMap.entries.find {
                it.key.equals(text, ignoreCase = true)
            }

            if (foundColor != null){
                buttonColor = foundColor.value
                Log.i(TAG, "Select color $foundColor")

            }
            else{
                buttonColor = Color.Gray
                Log.i(TAG, "Color $text is not found")
            }

        },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            Text(text = "Найти")
        }

        Column(modifier = Modifier
            ,
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            for (i in colorsMap){
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = i.value)
                    .fillMaxWidth()
                    .height(50.dp)
                    ,
                    contentAlignment = Alignment.Center

                ) {
                    Text(text = i.key,
                        modifier = Modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme(){
        Main()
    }
}