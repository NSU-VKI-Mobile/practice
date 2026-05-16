package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.auth.ui.login.LoginViewModel
import ci.nsu.mobile.main.auth.ui.register.RegisterViewModel
import ci.nsu.mobile.main.auth.ui.users.UsersViewModel
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
                val loginVM: LoginViewModel = hiltViewModel()
                val registerVM: RegisterViewModel = hiltViewModel()
                val usersVM: UsersViewModel = hiltViewModel()
                AppNavGraph(tokenManager, loginVM, registerVM, usersVM)
            }
        }
    }
}