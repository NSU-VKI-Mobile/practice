package ci.nsu.moble.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.ui.*

class MainActivity : ComponentActivity() {

    // viewModels() — ViewModel переживает поворот экрана
    private val vm: DepositViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val nav = rememberNavController()
                    NavHost(navController = nav, startDestination = Navigation.MAIN) {
                        composable(Navigation.MAIN)    { MainScreen(nav, vm, onExit = { finish() }) }
                        composable(Navigation.STEP1)   { Step1Screen(nav, vm) }
                        composable(Navigation.STEP2)   { Step2Screen(nav, vm) }
                        composable(Navigation.RESULT)  { ResultScreen(nav, vm) }
                        composable(Navigation.HISTORY) { HistoryScreen(nav, vm) }
                        composable(Navigation.DETAIL)  { backStack ->
                            val id = backStack.arguments?.getString("id")?.toLongOrNull() ?: return@composable
                            DetailScreen(nav, vm, id)
                        }
                    }
                }
            }
        }
    }
}