package com.example.app.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.auth.AuthActivity
import com.example.domain.interfaces.AuthNavigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuthNavigator @Inject constructor() : AuthNavigator {

    override fun navigateToLogin(context: Context) {
        val intent = Intent(context, AuthActivity::class.java)
        context.startActivity(intent)
    }

    override fun navigateToRegister(context: Context) {
        val intent = Intent(context, AuthActivity::class.java)
        context.startActivity(intent)
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, AuthActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }
}