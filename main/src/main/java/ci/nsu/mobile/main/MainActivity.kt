package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.mobile.auth.data.TokenManager
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(applicationContext)
        ServiceLocator.init(applicationContext)

        setContent {
            AppNavigation()
        }
    }
}