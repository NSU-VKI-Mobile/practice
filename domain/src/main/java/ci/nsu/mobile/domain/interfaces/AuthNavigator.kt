package ci.nsu.mobile.domain.interfaces

import android.app.Activity
import android.content.Context

interface AuthNavigator {
    fun navigateToLogin(context: Context)
    fun navigateToRegister(context: Context)
    fun openAuthFlow(activity: Activity, requestCode: Int)
}