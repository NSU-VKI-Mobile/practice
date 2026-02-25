package ci.nsu.moble.main

import android.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
    "red" to c.Red,
    "orange" to Orange,
    "yellow" to c.Yellow,
    "green" to c.Green,
    "blue" to c.Blue,
    "indigo" to Indigo,
    "violet" to Violet,

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
    val colorsList = collormaps.toList()
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
            onClick = {
                if (text.lowercase() in collormaps) {
                    Log.i("color","Цвет $text применен")
                } else {
                    Log.e("color","Цвет не найден")
                }
                buttoncolor=(if (text.lowercase() in collormaps) collormaps[text.lowercase()]
                else c.Gray)!! },
            colors = ButtonDefaults.buttonColors(containerColor = buttoncolor),
            modifier = Modifier
                .height(height = 48.dp)
                .fillMaxWidth()){
            Text(text = "Применить цвет")
        }
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn {
            items(colorsList, key = {it.first}){
                Surface (
                    color = it.second,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .size(256.dp, 56.dp)
                        .padding(8.dp),
                ){
                    Box(
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = it.first,
                            color = c.White
                        )

                    }
                }
            }
        }
    }
}

