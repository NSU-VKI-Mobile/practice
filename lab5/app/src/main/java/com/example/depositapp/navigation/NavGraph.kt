package com.example.depositapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.depositapp.ui.DepositViewModel
import com.example.depositapp.ui.history.DetailScreen
import com.example.depositapp.ui.history.HistoryScreen
import com.example.depositapp.ui.main.MainScreen
import com.example.depositapp.ui.result.ResultScreen
import com.example.depositapp.ui.step1.Step1Screen
import com.example.depositapp.ui.step2.Step2Screen

// NavGraph — карта всех экранов и переходов между ними
// NavHostController — объект который управляет навигацией
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: DepositViewModel
) {
    // NavHost — контейнер для всех экранов
    // startDestination — с какого экрана начинаем
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        // composable — регистрируем экран по его маршруту
        composable(Screen.Main.route) {
            MainScreen(
                onCalculate = { navController.navigate(Screen.Step1.route) },
                onHistory   = { navController.navigate(Screen.History.route) },
                onClose     = { /* закрытие обрабатывается в MainActivity */ }
            )
        }

        composable(Screen.Step1.route) {
            Step1Screen(
                viewModel = viewModel,
                onBack  = { navController.navigate(Screen.Main.route) {
                    // popUpTo — убираем Step1 из стека при возврате на Main
                    popUpTo(Screen.Main.route) { inclusive = false }
                }},
                onNext  = { navController.navigate(Screen.Step2.route) }
            )
        }

        composable(Screen.Step2.route) {
            Step2Screen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }, // вернуться назад
                onCalculate = {
                    viewModel.calculate() // считаем перед переходом
                    navController.navigate(Screen.Result.route)
                }
            )
        }

        composable(Screen.Result.route) {
            ResultScreen(
                viewModel = viewModel,
                onSave    = { viewModel.save() },
                onHome    = {
                    viewModel.reset()
                    // popUpTo Main + inclusive=false — очищаем весь стек до Main
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() },
                onDetail  = { id -> navController.navigate(Screen.Detail.createRoute(id)) }
            )
        }

        // Экран с параметром: detail/42
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                // Объявляем параметр id типа Long
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            // Достаём id из аргументов навигации
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            DetailScreen(
                id        = id,
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}
