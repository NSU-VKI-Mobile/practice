package ci.nsu.moble.main.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import ci.nsu.moble.domain.interfaces.AuthNavigator
import ci.nsu.moble.main.MainActivity

class AuthNavigatorImpl : AuthNavigator {
    override fun navigateToLogin(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "login")
        }
        context.startActivity(intent)
    }

    override fun navigateToRegister(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "register")
        }
        context.startActivity(intent)
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, MainActivity::class.java).apply {
            putExtra("destination", "auth")
        }
        activity.startActivityForResult(intent, requestCode)
    }

    override fun navigateToUsers(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "users")
        }
        context.startActivity(intent)
    }
}