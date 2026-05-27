package ci.nsu.mobile.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.mobile.app.navigation.AppNavGraph
import ci.nsu.mobile.app.theme.PracticeTheme
import ci.nsu.mobile.domain.auth.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                AppNavGraph(
                    authManager = authManager
                )
            }
        }
    }
}