package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ci.nsu.moble.main.ui.screens.MainScreen
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                MainScreen()

            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    PracticeTheme {
        MainScreen()
    }
}