package ci.nsu.mobile.auth

import android.app.Activity
import android.content.Context
import ci.nsu.mobile.domain.AuthNavigator

/**
 * Реализация навигации для модуля auth.
 * Принимает действия (лямбды) из основного приложения,
 * чтобы не зависеть от конкретной реализации (NavController).
 */
class AuthNavigatorImpl(
    private val onNavigateToLogin: () -> Unit,
    private val onNavigateToRegister: () -> Unit
) : AuthNavigator {

    override fun navigateToLogin(context: Context) {
        onNavigateToLogin()
    }

    override fun navigateToRegister(context: Context) {
        onNavigateToRegister()
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {
        // При необходимости можно запустить отдельную Activity
        onNavigateToLogin()
    }
}