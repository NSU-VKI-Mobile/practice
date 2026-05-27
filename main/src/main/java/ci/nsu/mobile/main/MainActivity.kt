package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceLocator = (application as MainApplication).serviceLocator

        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation(serviceLocator = serviceLocator)
                }
            }
        }
    }
}
