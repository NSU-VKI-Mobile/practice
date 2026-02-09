package ci.nsu.moble.main

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
        "Red" to Color(0xFFEF4D60),
        "Orange" to Color(0xFFFF9156),
        "Yellow" to Color(0xFFF9FF3D),
        "Green" to Color(0xFF8AFC70),
        "Blue" to Color(0xFF4DFFF9),
        "Violet" to Color(0xFF8F59F5)
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
                label = {Text("Find color")},
                modifier = Modifier.wrapContentSize()
                //.fillMaxWidth(0.8f)
                //.fillMaxHeight(0.1f)
            )
            Button(
                onClick = {
                    pickedcolor = Color.Cyan
                },
                modifier = Modifier.wrapContentSize()
            ) {
                Text("Change")
            }
        }

    }
}