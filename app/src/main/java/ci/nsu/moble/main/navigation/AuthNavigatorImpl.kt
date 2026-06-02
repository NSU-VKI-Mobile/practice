package ci.nsu.moble.main.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import ci.nsu.moble.domain.interfaces.AuthNavigator

class AuthNavigatorImpl(
    private val navController: NavController
) : AuthNavigator {

    override fun navigateToLogin(context: Context) {
        navController.navigate("auth") {
            popUpTo(0) { inclusive = true }
        }
    }

    override fun navigateToRegister(context: Context) {
        navController.navigate("register")
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {

        navController.navigate("auth") {
            popUpTo(0) { inclusive = true }
        }
    }

    override fun navigateToUsers(context: Context) {
        navController.navigate("users") {
            popUpTo(0) { inclusive = true }
        }
    }
}