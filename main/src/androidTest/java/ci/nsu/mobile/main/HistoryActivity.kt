package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ci.nsu.mobile.main.ui.screens.HistoryScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                HistoryScreen(onNavigateBack = { finish() })
            }
        }
    }
}