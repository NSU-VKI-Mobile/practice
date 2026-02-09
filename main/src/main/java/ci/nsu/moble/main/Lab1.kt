package ci.nsu.moble.main

import android.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class Lab1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                ColorsApp()
            }
        }
    }
}

@Composable
fun ColorsApp()
{
    var pickedcolor by remember { mutableStateOf(Color.White) }
    val rainbowColors = mapOf(
        "red" to Color(0xFFEF4D60),
        "orange" to Color(0xFFFF9156),
        "yellow" to Color(0xFFF9FF3D),
        "green" to Color(0xFF8AFC70),
        "blue" to Color(0xFF4DFFF9),
        "violet" to Color(0xFFB694FF)
    )
    var text by remember {mutableStateOf("")}
    Surface(
        color = pickedcolor,
        modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TextField(
                value = text,
                onValueChange = {newText: String ->
                    text = newText},
                label = {Text("find color")},
                modifier = Modifier.wrapContentSize()
                //.fillMaxWidth(0.8f)
                //.fillMaxHeight(0.1f)
            )
            Button(
                onClick = {
                    val found = rainbowColors.entries.find {(key, _) ->
                        key.contains(text.lowercase(), ignoreCase = true)
                    }
                    if (found != null)
                    {
                        pickedcolor = found.value
                    }
                    else {
                        Log.e("colors", "couldn't find color '$text'")
                    }
                    //pickedcolor = Color.Cyan
                },
                modifier = Modifier.wrapContentSize()
            ) {
                Text("Change")
            }
            ColorCardsList(rainbowColors)
        }

    }
}

@Composable
fun ColorCardsList(colorMap: Map<String, Color>) {
    Column (
        Modifier.width(100.dp)
    ){
        colorMap.forEach { (name, color) ->
            ColorCard(name = name, color = color)
        }
    }
}

@Composable
fun ColorCard(name: String, color: Color) {
    Card(
        modifier = Modifier
            .background(color)
            .fillMaxWidth(),
    ) {
        Text(
            text = name,
            modifier = Modifier
                .background(color)
                .fillMaxWidth()
        )
    }
}