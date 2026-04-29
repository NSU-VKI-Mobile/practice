package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.datasource.local.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.UserRepository
import ci.nsu.mobile.main.ui.auth.login.LoginViewModel
import ci.nsu.mobile.main.ui.navigation.NavGraph
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.ui.users.UsersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create dependencies
        val tokenManager = TokenManager(applicationContext)
        val authRepository = AuthRepository(tokenManager)
        val userRepository = UserRepository(tokenManager)

        setContent {
            PracticeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(
                        authRepository = authRepository,
                        userRepository = userRepository
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    authRepository: AuthRepository,
    userRepository: UserRepository
) {
    val navController = rememberNavController()

    // Create ViewModel with repository
    val loginViewModel: LoginViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                LoginViewModel(authRepository)
            }
        }
    )

    val usersViewModel: UsersViewModel = viewModel(
        factory = viewModelFactory {
            initializer { UsersViewModel(userRepository, authRepository) }
        }
    )

    NavGraph(
        navController = navController,
        loginViewModel = loginViewModel,
        usersViewModel = usersViewModel
    )
}