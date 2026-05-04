package ci.nsu.mobile.auth.data.local

import android.content.Context
import android.content.SharedPreferences
import ci.nsu.mobile.domain.auth.AuthState
import ci.nsu.mobile.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val LOGIN_KEY = "user_login"
    private const val PASSWORD_KEY = "user_password"
    private var prefs: SharedPreferences? = null
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Эту функцию мы вызовем один раз при старте приложения (в MainActivity)
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        syncAuthState()
    }

    var token: String?
        get() = prefs?.getString(TOKEN_KEY, null)
        set(value) {
            prefs?.edit()?.putString(TOKEN_KEY, value)?.apply()
            syncAuthState()
        }

    var login: String?
        get() = prefs?.getString(LOGIN_KEY, null)
        set(value) {
            prefs?.edit()?.putString(LOGIN_KEY, value)?.apply()
            syncAuthState()
        }

    var password: String?
        get() = prefs?.getString(PASSWORD_KEY, null)
        set(value) {
            prefs?.edit()?.putString(PASSWORD_KEY, value)?.apply()
        }

    fun clear() {
        prefs?.edit()?.remove(TOKEN_KEY)?.remove(LOGIN_KEY)?.remove(PASSWORD_KEY)?.apply()
        _authState.value = AuthState.Unauthenticated
    }

    fun isLoggedIn(): Boolean = !token.isNullOrBlank()

    fun currentUser(): User? {
        val savedLogin = login ?: return null
        return User(id = stableUserId(savedLogin), login = savedLogin)
    }

    private fun syncAuthState() {
        val user = currentUser()
        _authState.value = if (isLoggedIn() && user != null) {
            AuthState.Authenticated(user)
        } else {
            AuthState.Unauthenticated
        }
    }

    private fun stableUserId(login: String): Long {
        val hash = login.fold(1125899906842597L) { acc, char -> acc * 31 + char.code }
        return hash and Long.MAX_VALUE
    }
}
