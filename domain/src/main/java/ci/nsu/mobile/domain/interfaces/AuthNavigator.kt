package ci.nsu.mobile.domain.interfaces

import androidx.navigation.NavHostController

interface AuthNavigator {
    fun navigateToLogin(navController: NavHostController)
    fun navigateToRegister(navController: NavHostController)
    fun openAuthFlow(navController: NavHostController)
}