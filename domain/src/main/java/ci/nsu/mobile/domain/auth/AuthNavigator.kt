package ci.nsu.mobile.domain.auth

import android.app.Activity
import android.content.Context

/**
 * Navigation entry points owned by the auth module.
 */
interface AuthNavigator {
    fun navigateToLogin(context: Context)
    fun navigateToRegister(context: Context)
    fun openAuthFlow(activity: Activity, requestCode: Int)
}
