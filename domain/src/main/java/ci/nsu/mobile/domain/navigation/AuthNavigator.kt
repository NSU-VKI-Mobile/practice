package ci.nsu.mobile.domain.navigation

import android.content.Context

interface AuthNavigator {
    fun navigateToLogin(context: Context)
    fun navigateToRegister(context: Context)
}