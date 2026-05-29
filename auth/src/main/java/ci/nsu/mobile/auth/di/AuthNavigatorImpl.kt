package ci.nsu.mobile.auth.di

import android.content.Context
import ci.nsu.mobile.domain.navigation.AuthNavigator

class AuthNavigatorImpl(private val context: Context) : AuthNavigator {
    override fun navigateToLogin() {
        // Используем context здесь для запуска Intent
    }

    override fun navigateToRegister() {
        // Используем context здесь для запуска Intent
    }
}
