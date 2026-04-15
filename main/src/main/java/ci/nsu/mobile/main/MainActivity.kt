package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.mobile.main.ui.CounterScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme   // ← если тема называется иначе, поправь

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                CounterScreen()
            }
        }
    }
}