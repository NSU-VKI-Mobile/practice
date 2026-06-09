package ci.nsu.mobile.main.navigation

import android.app.Activity
import android.content.Context
import ci.nsu.mobile.domain.interfaces.AuthNavigator

class AuthNavigatorImpl : AuthNavigator {
    override fun navigateToLogin(context: Context)  {}
    override fun navigateToRegister(context: Context) {}
    override fun openAuthFlow(activity: Activity, requestCode: Int) {}
}