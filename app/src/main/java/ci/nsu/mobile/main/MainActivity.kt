package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.interfaces.AuthNavigator
import ci.nsu.mobile.domain.interfaces.CalculationsNavigator
import ci.nsu.mobile.main.navigation.MainNavigation
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authNavigator: AuthNavigator
    @Inject lateinit var calculationsNavigator: CalculationsNavigator
    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                MainNavigation(
                    authNavigator = authNavigator,
                    calculationsNavigator = calculationsNavigator,
                    authManager = authManager
                )
            }
        }
    }
}
