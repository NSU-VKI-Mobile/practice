package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    var pickedcolor = Color.White
    Surface(
        color = pickedcolor,
        modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                pickedcolor = Color.Cyan
            },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text("Change")
        }
    }
}