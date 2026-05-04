package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.navigation.Navigation
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.LoginViewModel
import ci.nsu.mobile.main.viewmodel.factory.ViewModelsFactory

class MainActivity : ComponentActivity() {
    private lateinit var repository: AuthRepository
    private lateinit var viewModelsFactory: ViewModelsFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = AuthRepository()
        viewModelsFactory = ViewModelsFactory(repository)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                val loginViewModel: LoginViewModel = viewModel(factory = viewModelsFactory)
                Navigation(rememberNavController(), loginViewModel)
            }
        }
    }
}
