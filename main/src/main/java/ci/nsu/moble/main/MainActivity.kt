package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import ci.nsu.moble.main.ui.AppNavigation
import ci.nsu.moble.main.ui.theme.MobilepracticeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobilepracticeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Koin сам создаст и свяжет все ViewModels со всеми их репозиториями
                    AppNavigation()
                }
            }
        }
    }
}
