package com.example.authapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.authapp.data.TokenManager
import com.example.authapp.navigation.NavGraph
import com.example.authapp.ui.AuthViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем TokenManager (SharedPreferences нужен Context)
        TokenManager.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
