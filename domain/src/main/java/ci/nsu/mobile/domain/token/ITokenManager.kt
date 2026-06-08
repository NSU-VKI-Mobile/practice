package ci.nsu.mobile.domain.token

import kotlinx.coroutines.flow.Flow

interface ITokenManager {
    var token: String?
    var userLogin: String?
    var userId: Int?
    var userEmail: String?
    var userPersonId: Int?
    var userCreatedDate: String?
    var userPhone: String?
    var userRoleId: Int?
    var userLastLoginDate: String?
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clear()
    fun isLoggedIn(): Boolean
    fun observeToken(): Flow<String?>
}