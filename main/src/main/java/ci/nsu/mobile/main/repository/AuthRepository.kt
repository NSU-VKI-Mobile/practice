package ci.nsu.mobile.main.repository

import android.util.Log
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.network.AuthApi
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: TokenManager
) {

    suspend fun login(request: LoginRequest): Result<Unit> {
        return runCatching {
            val response = api.login(request)
            sessionManager.saveToken(response.token)
            val userResponse = api.getUserByLogin(request.login)
            val res = sessionManager.saveUserId(userResponse.userId.toString())
            Log.d("LOCAL", sessionManager.getUserId()!!)
            res
        }.onFailure { exception ->
            if (exception is CancellationException) throw exception
            sessionManager.clearSession()
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<Unit> {
        return runCatching {
            val response = api.register(request)
            sessionManager.saveToken(response.token)
            val userResponse = api.getUserByLogin(request.login)
            val res = sessionManager.saveUserId(userResponse.userId.toString())
            Log.d("LOCAL", sessionManager.getUserId()!!)
            res


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