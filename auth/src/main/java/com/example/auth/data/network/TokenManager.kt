package com.example.auth.data.network

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREF_NAME = "auth_prefs"
        private const val TOKEN_KEY = "jwt_token"
        private const val USER_LOGIN_KEY = "user_login"
        private const val USER_ID_KEY = "user_id"
    }

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null)
        set(value) {
            prefs.edit { putString(TOKEN_KEY, value) }
        }

    var userLogin: String?
        get() = prefs.getString(USER_LOGIN_KEY, null)
        set(value) {
            prefs.edit { putString(USER_LOGIN_KEY, value) }
        }

    var userId: Long
        get() = prefs.getLong(USER_ID_KEY, -1)
        set(value) {
            prefs.edit { putLong(USER_ID_KEY, value) }
        }

    fun clear() {
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean {
        return !token.isNullOrEmpty()
    }
}