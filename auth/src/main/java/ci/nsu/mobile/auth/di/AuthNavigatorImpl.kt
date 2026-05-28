package ci.nsu.mobile.auth.di

import android.content.Context
import android.content.Intent
import ci.nsu.mobile.auth.ui.screens.LoginScreen
import ci.nsu.mobile.auth.ui.screens.RegisterScreen
import ci.nsu.mobile.domain.navigation.AuthNavigator

class AuthNavigatorImpl : AuthNavigator {

    override fun navigateToLogin(context: Context) {
        // переход на LoginScreen
    }

    override fun navigateToRegister(context: Context) {
        // переход на RegisterScreen
    }
}