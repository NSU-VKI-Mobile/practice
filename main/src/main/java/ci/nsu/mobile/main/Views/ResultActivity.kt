package ci.nsu.mobile.main.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ci.nsu.mobile.main.Views.ui.theme.PracticeTheme

class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                }
            }
        }
    }
}