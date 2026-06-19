package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.network.RetrofitClient
import ci.nsu.mobile.main.repository.AuthRepository
import ci.nsu.mobile.main.ui.navigation.NavGraph

import ci.nsu.mobile.main.utils.TokenManager
import ci.nsu.mobile.main.ui.theme.UserAppTheme

class MainActivity : ComponentActivity() {  // ComponentActivity, не Activity!

    private lateinit var tokenManager: TokenManager
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(applicationContext)
        val apiService = RetrofitClient.create(tokenManager)
        authRepository = AuthRepository(apiService, tokenManager)

        setContent {
            UserAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(authRepository = authRepository)
                }
            }
        }
    }
}