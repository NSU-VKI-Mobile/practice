package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.navigation.Navigation
import ci.nsu.mobile.main.ui.theme.PracticeTheme
import ci.nsu.mobile.main.viewmodel.LoginViewModel
import ci.nsu.mobile.main.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val registrationViewModel: RegistrationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Navigation(rememberNavController(), loginViewModel, registrationViewModel)
            }
        }
    }
}
