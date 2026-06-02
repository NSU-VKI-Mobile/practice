package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ci.nsu.moble.auth.data.storage.TokenManager
import ci.nsu.moble.main.ui.navigation.AppNavHost
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity()
{
    private val tokenManager: TokenManager by inject()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        tokenManager.clearAll()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost()
                }
            }
        }
    }
}