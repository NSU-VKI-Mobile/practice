package com.example.integratedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.example.integratedapp.data.SessionManager
import com.example.integratedapp.di.ServiceLocator
import com.example.integratedapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация при старте — порядок важен
        SessionManager.init(applicationContext)
        ServiceLocator.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}
