// :auth/src/main/java/ci/nsu/mobile/auth/data/TokenManagerImpl.kt
package ci.nsu.mobile.auth.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ci.nsu.mobile.domain.token.ITokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("auth_tokens")

@Singleton
class TokenManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ITokenManager {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_LOGIN_KEY = stringPreferencesKey("user_login")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_PERSON_ID_KEY = intPreferencesKey("user_person_id")
        private val USER_CREATED_DATE_KEY = stringPreferencesKey("user_created_date")
        private val USER_PHONE_KEY = stringPreferencesKey("user_phone")
        private val USER_ROLE_ID_KEY = intPreferencesKey("user_role_id")
        private val USER_LAST_LOGIN_DATE_KEY = stringPreferencesKey("user_last_login_date")
    }

    override var userLastLoginDate: String?
        get() = runBlocking { context.dataStore.data.first()[USER_LAST_LOGIN_DATE_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_LAST_LOGIN_DATE_KEY] = it }
                    if (value == null) prefs.remove(USER_LAST_LOGIN_DATE_KEY)
                }
            }
        }

    override var token: String?
        get() = runBlocking { getAccessToken() }
        set(value) {
            runBlocking {
                value?.let { saveTokens(it, getRefreshToken() ?: "") }
            }
        }

    override var userLogin: String?
        get() = runBlocking { context.dataStore.data.first()[USER_LOGIN_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_LOGIN_KEY] = it }
                    if (value == null) prefs.remove(USER_LOGIN_KEY)
                }
            }
        }

    override var userId: Int?
        get() = runBlocking { context.dataStore.data.first()[USER_ID_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_ID_KEY] = it }
                    if (value == null) prefs.remove(USER_ID_KEY)
                }
            }
        }

    override var userEmail: String?
        get() = runBlocking { context.dataStore.data.first()[USER_EMAIL_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_EMAIL_KEY] = it }
                    if (value == null) prefs.remove(USER_EMAIL_KEY)
                }
            }
        }

    override var userPersonId: Int?
        get() = runBlocking { context.dataStore.data.first()[USER_PERSON_ID_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_PERSON_ID_KEY] = it }
                    if (value == null) prefs.remove(USER_PERSON_ID_KEY)
                }
            }
        }

    override var userCreatedDate: String?
        get() = runBlocking { context.dataStore.data.first()[USER_CREATED_DATE_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_CREATED_DATE_KEY] = it }
                    if (value == null) prefs.remove(USER_CREATED_DATE_KEY)
                }
            }
        }

    override var userPhone: String?
        get() = runBlocking { context.dataStore.data.first()[USER_PHONE_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_PHONE_KEY] = it }
                    if (value == null) prefs.remove(USER_PHONE_KEY)
                }
            }
        }

    override var userRoleId: Int?
        get() = runBlocking { context.dataStore.data.first()[USER_ROLE_ID_KEY] }
        set(value) {
            runBlocking {
                context.dataStore.edit { prefs ->
                    value?.let { prefs[USER_ROLE_ID_KEY] = it }
                    if (value == null) prefs.remove(USER_ROLE_ID_KEY)
                }
            }
        }

    override suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    override suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    override fun isLoggedIn(): Boolean {
        return token != null
    }

    override fun observeToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }
}