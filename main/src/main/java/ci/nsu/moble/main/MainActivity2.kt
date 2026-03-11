package ci.nsu.moble.main

import android.R
import android.R.attr.padding
import androidx.compose.material3.TextField
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CheckboxDefaults.colors

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    ColorChange()
                }
            }
        }
    }
}

@Composable
fun ColorChange(){
    var color by remember { mutableStateOf(Color.White) }
    var colorName by remember { mutableStateOf("White") }

    var сolorNames = mapOf(
    "Red" to Color.Red,
    "Blue" to Color.Blue,
    "Cyan" to Color.Cyan,
    "Yellow" to Color.Yellow,
    "Green" to Color.Green
    )

    Column() {
        Button(
            onClick = {
                val newColor = сolorNames[colorName]

                if (newColor != null) {
                    color = newColor
                }
                else {
                    color = Color.White
                }
                      },
            colors = buttonColors(
            color
            )
        ) {
            Text("Цвета")
        }

        TextField(
            value = colorName,
            onValueChange = { colorName = it }
        )

        Button(onClick = {
            color = Color.Red
        }) {
            Text("Red")

        }
        Button(onClick = {
            color = Color.Blue
        }) {
            Text("Blue")

        }
        Button(onClick = {
            color = Color.Cyan
        }) {
            Text("Cyan")

        }
        Button(onClick = {
            color = Color.Yellow
        }) {
            Text("Yellow")

        }
        Button(onClick = {
            color = Color.Green
        }) {
            Text("Green")

        }
    }
}