package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.data.AuthRepository
import ci.nsu.mobile.data.TokenManager // ИСПРАВЛЕНО: Добавили правильный импорт для менеджера токенов
import ci.nsu.mobile.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Кормим TokenManager контекстом прямо при старте приложения, как и планировали
        TokenManager.init(this)

        // Наша база данных/репозиторий
        val repository = AuthRepository()

        // Создаем одну общую ViewModel для управления состоянием переходов
        val viewModel = AuthViewModel(repository)

        setContent {
            val navController = rememberNavController()

            // Если где-то сработал флаг успеха (залогинились или зарегались) — летим на главный экран
            if (viewModel.isSuccess) {
                LaunchedEffect(Unit) {
                    navController.navigate("main") {
                        // Очищаем историю переходов, чтобы кнопкой "Назад" нельзя было вернуться к логину
                        popUpTo("login") { inclusive = true }
                    }
                    viewModel.isSuccess = false // Сбрасываем флаг, чтобы не зациклить переходы
                }
            }

            // Наш единственный штурман навигации
            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                // 1. Экран логина
                composable("login") {
                    // Принудительно очищаем старые ошибки перед показом экрана
                    LaunchedEffect(Unit) { viewModel.errorMessage = null }

                    LoginScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = {
                            // Перед уходом на регистрацию чистим поля ввода, чтобы они не двоились там
                            viewModel.loginText = ""
                            viewModel.passwordText = ""
                            viewModel.errorMessage = null
                            navController.navigate("register")
                        }
                    )
                }

                // 2. Экран регистрации
                composable("register") {
                    RegistrationScreen(
                        viewModel = viewModel,
                        onBackToLogin = {
                            // При возврате назад тоже прибираем за собой поля
                            viewModel.loginText = ""
                            viewModel.passwordText = ""
                            viewModel.errorMessage = null
                            navController.popBackStack()
                        }
                    )
                }

                // 3. Главный экран (список студентов)
                composable("main") {
                    MainScreen(viewModel)
                }
            }
        }
    }
}
