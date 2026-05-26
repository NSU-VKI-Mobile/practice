package ci.nsu.mobile.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.api.RetrofitClient
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.ui.navigation.AppNavHost
import ci.nsu.mobile.main.ui.screens.home.HomeViewModel
import ci.nsu.mobile.main.ui.screens.login.LoginViewModel
import ci.nsu.mobile.main.ui.screens.register.RegisterViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import kotlinx.serialization.json.Json
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this
        val tokenManager = TokenManager(context)

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val retrofitClient = RetrofitClient(tokenManager, json)

        enableEdgeToEdge()
        setContent {
            val loginViewModel: LoginViewModel = viewModel {
                LoginViewModel(
                    repository = AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager
                    )
                )
            }

            val registerViewModel: RegisterViewModel = viewModel {
                RegisterViewModel(
                    repository = AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager
                    )
                )
            }

            val homeViewModel: HomeViewModel = viewModel {
                HomeViewModel(
                    repository = AuthRepository(
                        api = retrofitClient.api,
                        tokenManager = tokenManager
                    ),
                    tokenManager = tokenManager
                )
            }

            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        retrofitClient = retrofitClient,
                        tokenManager = tokenManager,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

