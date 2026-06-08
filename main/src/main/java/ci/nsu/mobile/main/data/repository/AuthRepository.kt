package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.api.AuthApiService
import ci.nsu.mobile.main.data.local.SessionManager
import ci.nsu.mobile.main.data.model.AuthResponse
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegistrationRequest
import ci.nsu.mobile.main.data.model.UserDto
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository @Inject constructor(
    private val api: AuthApiService,
    private val sessionManager: SessionManager
) {

    suspend fun login(request: LoginRequest): Result<Unit> {
        return runCatching {
            val response = api.login(request)
            sessionManager.saveToken(response.token)
            val userResponse = api.getUserByLogin(request.login)
            sessionManager.saveUserId(userResponse.userId.toString())
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
            sessionManager.clearSession()
        }
    }

    suspend fun register(
        request: RegistrationRequest
    ): Result<Unit> {
        return runCatching {
            val response = api.register(request)
            sessionManager.saveToken(response.token)
            val userResponse = api.getUserByLogin(request.login)
            sessionManager.saveUserId(userResponse.userId.toString())

        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
            sessionManager.clearSession()
        }
    }

    suspend fun getGroups(): Result<List<GroupDto>> {
        return try {
            Result.success(api.getGroups())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<UserDto>> {
        return runCatching {
            api.getUsers()
        }
    }
}