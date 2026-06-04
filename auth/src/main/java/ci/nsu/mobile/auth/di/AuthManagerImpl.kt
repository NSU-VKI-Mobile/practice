package ci.nsu.mobile.auth.di

import ci.nsu.mobile.auth.data.repository.ApiResult
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.utils.UserPreferences
import ci.nsu.mobile.domain.interfaces.AuthManager
import ci.nsu.mobile.domain.models.AuthState
import ci.nsu.mobile.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthManagerImpl(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : AuthManager {

    override suspend fun isLoggedIn(): Boolean {
        return userPreferences.getToken() != null
    }

    override suspend fun getCurrentUser(): User? {
        val userId = userPreferences.getUserId() ?: return null
        return User(id = userId.toInt(), login = "", email = "")
    }

    override suspend fun getUsers(): List<User> {
        val result = authRepository.getUsers()
        return when (result) {
            is ApiResult.Success -> {
                result.data.map { userDto ->
                    User(
                        id = userDto.id,
                        login = userDto.login,
                        email = userDto.email,
                        phoneNumber = userDto.phoneNumber,
                        firstName = userDto.person?.firstName,
                        lastName = userDto.person?.lastName
                    )
                }
            }
            is ApiResult.Error -> {
                emptyList()
            }
            else -> emptyList()
        }
    }

    override suspend fun logout() {
        userPreferences.clear()
    }

    override fun observeAuthState(): Flow<AuthState> {
        return userPreferences.getTokenFlow().map { token ->
            if (token != null) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }
}