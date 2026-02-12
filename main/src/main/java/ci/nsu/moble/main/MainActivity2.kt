package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color as c

@Stable
val Orange = c(color = 0xFF910A)
@Stable
val Indigo = c(color = 0x4B0082)
@Stable
val Violet = c(color = 0x800080)

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
//            PeopleTheme{
//                Main()
//            }
        }
    }
}

@Composable
fun Main() {
    var text by remember { mutableStateOf("") }
}

