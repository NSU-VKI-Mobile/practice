package com.example.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.auth.navigation.Navigation
import com.example.auth.ui.theme.PracticeTheme
import com.example.auth.viewmodel.login.LoginViewModel
import com.example.auth.viewmodel.registration.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()
                val registerViewModel: RegistrationViewModel = viewModel()
                Navigation(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel,
                    onLoginSuccess = {
                        finish()
                    }
                )
            }
        }
    }
}