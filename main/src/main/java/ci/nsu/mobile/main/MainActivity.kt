package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.core.navigation.AppNavGraph
import ci.nsu.mobile.main.core.theme.PracticeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                AppNavGraph(
                    tokenManager
                )
            }
        }
    }
}