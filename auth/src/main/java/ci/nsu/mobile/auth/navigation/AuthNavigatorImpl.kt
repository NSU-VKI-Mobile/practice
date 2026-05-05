package ci.nsu.mobile.auth.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import ci.nsu.mobile.auth.AuthActivity
import ci.nsu.mobile.domain.auth.AuthNavigator

class AuthNavigatorImpl : AuthNavigator {
    override fun navigateToLogin(context: Context) {
        context.startActivity(authIntent(context, AuthActivity.ROUTE_LOGIN))
    }

    override fun navigateToRegister(context: Context) {
        context.startActivity(authIntent(context, AuthActivity.ROUTE_REGISTER))
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {
        activity.startActivityForResult(
            authIntent(activity, AuthActivity.ROUTE_LOGIN),
            requestCode
        )
    }

    private fun authIntent(context: Context, route: String): Intent {
        return Intent(context, AuthActivity::class.java)
            .putExtra(AuthActivity.EXTRA_START_ROUTE, route)
            .apply {
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
    }
}
