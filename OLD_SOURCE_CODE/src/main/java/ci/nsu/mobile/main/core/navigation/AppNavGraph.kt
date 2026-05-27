package ci.nsu.mobile.main.core.navigation

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.auth.ui.login.LoginScreen
import ci.nsu.mobile.main.auth.ui.login.LoginViewModel
import ci.nsu.mobile.main.auth.ui.register.RegisterScreen
import ci.nsu.mobile.main.auth.ui.register.RegisterViewModel
import ci.nsu.mobile.main.auth.ui.users.UsersScreen
import ci.nsu.mobile.main.auth.ui.users.UsersViewModel
import ci.nsu.mobile.main.calculations.ui.additional.AdditionalScreen
import ci.nsu.mobile.main.calculations.ui.additional.AdditionalViewModel
import ci.nsu.mobile.main.calculations.ui.calculation.CalculationScreen
import ci.nsu.mobile.main.calculations.ui.calculation.CalculationViewModel
import ci.nsu.mobile.main.calculations.ui.history.HistoryScreen
import ci.nsu.mobile.main.calculations.ui.result.ResultScreen
import ci.nsu.mobile.main.calculations.ui.result.ResultViewModel

private val  DoubleNavType = object : NavType<Double>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Double {
        return bundle.getDouble(key)
    }

    override fun parseValue(value: String): Double {
        return value.toDouble()
    }

    override fun put(bundle: Bundle, key: String, value: Double) {
        bundle.putDouble(key, value)
    }
}

@Composable
fun AppNavGraph(
    tokenManager: TokenManager
) {
    val navController = rememberNavController()

    var isLoggedIn by remember {
        mutableStateOf(tokenManager.token != null && tokenManager.userId != null)
    }

    LaunchedEffect(tokenManager.token) {
        isLoggedIn = tokenManager.token != null && tokenManager.userId != null
    }

    // Track current route to conditionally show bottom bar
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Routes that should show bottom navigation bar
    val mainRoutes = setOf(Routes.USERS, Routes.HISTORY, Routes.NEW_CALCULATION)
    val showBottomBar = isLoggedIn && mainRoutes.contains(currentRoute)
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Routes.USERS else Routes.LOGIN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onLoginSuccess = {
                        isLoggedIn = true
                        navController.navigate(Routes.USERS) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        isLoggedIn = true
                        navController.navigate(Routes.USERS) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.USERS) {
                UsersScreen(
                    onLogout = {
                        tokenManager.clear()
                        isLoggedIn = false
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.HISTORY) {
                HistoryScreen()
            }

            composable(Routes.NEW_CALCULATION) {
                CalculationScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAdditional = { amount, term ->
                        navController.navigate(Routes.additional(amount, term))
                    }
                )
            }

            composable(
                route = Routes.ADDITIONAL,
                arguments = listOf(
                    navArgument("amount") { type = NavType.StringType },
                    navArgument("term") { type = NavType.StringType }
                )
            ) {
                AdditionalScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onCalculate = { amount, term, rate, monthlyAddition ->
                        navController.navigate(Routes.result(amount, term, rate, monthlyAddition))
                    }
                )
            }

            composable(
                route = Routes.RESULT,
                arguments = listOf(
                    navArgument("amount") { type = DoubleNavType  },
                    navArgument("term") { type = NavType.IntType },
                    navArgument("rate") { type = DoubleNavType },
                    navArgument("monthlyAddition") { type = DoubleNavType  }
                )
            ) {
                ResultScreen(
                    onNavigateToMain = { navController.popBackStack(Routes.NEW_CALCULATION, false) }
                )
            }
        }
    }
}

@Composable
fun EmptyScreen(title: String = "TODO") { //for dev
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}