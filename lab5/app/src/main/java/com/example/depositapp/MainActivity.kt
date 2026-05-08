package com.example.depositapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.depositapp.data.db.AppDatabase
import com.example.depositapp.data.repository.DepositRepository
import com.example.depositapp.navigation.NavGraph
import com.example.depositapp.ui.DepositViewModel

class MainActivity : ComponentActivity() {

    // viewModels() — создаёт ViewModel и привязывает к lifecycle Activity
    // by — делегат: когда нужен viewModel, Android создаёт его сам
    // factory — передаём нашу фабрику потому что ViewModel принимает параметры
    private val viewModel: DepositViewModel by viewModels {
        // Создаём зависимости: Database → Repository → ViewModel
        // Это называется Dependency Injection (ручной вариант)
        val db = AppDatabase.getDatabase(this)
        val repository = DepositRepository(db)
        DepositViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                // rememberNavController — создаёт и запоминает навигационный контроллер
                val navController = rememberNavController()

                // NavGraph управляет всеми экранами
                // Передаём один viewModel на все экраны — данные не теряются
                NavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }

    // Обработка кнопки "Закрыть приложение"
    // Это вызывается из MainScreen через onClose
    fun closeApp() {
        finish() // завершаем Activity = закрываем приложение
    }
}
