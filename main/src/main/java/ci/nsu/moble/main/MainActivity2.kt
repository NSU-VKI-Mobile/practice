package ci.nsu.moble.main

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color as c
import androidx.compose.ui.unit.sp
@Stable
val Orange = c(color = 0xFFFA8C0F)
@Stable
val Indigo = c(color = 0xFF4B0082)
@Stable
val Violet = c(color = 0xFF910AFF)

private val collormaps = mapOf(
    "Red" to c.Red,
    "Orange" to Orange,
    "Yellow" to c.Yellow,
    "Green" to c.Green,
    "Blue" to c.Blue,
    "Indigo" to Indigo,
    "Violet" to Violet,

    )


class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun Main() {
    var text by remember { mutableStateOf("") }
    var buttoncolor by remember { mutableStateOf(value = c.Gray) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement=Arrangement.Top,
        horizontalAlignment=Alignment.CenterHorizontally
    ){
        TextField(
            value = text,
            onValueChange = {newText -> text=newText},
            label = {Text("Введите цвет")},
            modifier= Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { buttoncolor= (if (text in collormaps) collormaps[text] else c.Gray)!! },
            colors = ButtonDefaults.buttonColors(containerColor = buttoncolor),
            modifier = Modifier
                .height(height = 48.dp)
                .fillMaxWidth()){
            Text(text = "Применить цвет")
        }
    }
}

