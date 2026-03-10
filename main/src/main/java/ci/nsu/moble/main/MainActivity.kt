package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ){
                        Text("Test")
                        var sliderr by remember { mutableFloatStateOf(0f) }
                        var sliderg by remember { mutableFloatStateOf(0f) }
                        var sliderb by remember { mutableFloatStateOf(0f) }
                        Slider(
                            value = sliderr,
                            onValueChange = { sliderr = it },
                            valueRange = 0f..255f
                        )
                        Slider(
                            value = sliderg,
                            onValueChange = { sliderg = it },
                            valueRange = 0f..255f)
                        Slider(
                            value = sliderb,
                            onValueChange = { sliderb = it },
                            valueRange = 0f..255f
                        )
                    }
                }
            }
        }
    }
}