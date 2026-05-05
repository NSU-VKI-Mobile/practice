package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.data.network.NetworkModule
import ci.nsu.mobile.main.data.storage.TokenManager
import ci.nsu.mobile.main.domain.AuthRepository

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        TokenManager.clear()

        val apiService = NetworkModule.provideApiService()
        val publicApiService = NetworkModule.providePublicApiService()
        authRepository = AuthRepository(apiService, publicApiService)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    authRepository = authRepository
                )
            }
        }
    }
}