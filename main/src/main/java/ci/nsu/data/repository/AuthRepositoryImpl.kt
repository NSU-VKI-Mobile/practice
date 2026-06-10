package ci.nsu.data.repository

import ci.nsu.data.local.SessionManager
import ci.nsu.data.remote.AuthApiService
import ci.nsu.data.remote.dto.AuthResponse
import ci.nsu.data.remote.dto.LoginRequest
import ci.nsu.data.remote.dto.RegisterRequest
import retrofit2.Response
import ci.nsu.data.remote.dto.UserResponse

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Response<AuthResponse> {
        val response = apiService.login(LoginRequest(username, password))

        // Если запрос успешен и тело ответа не пустое, сохраняем сессию
        if (response.isSuccessful) {
            response.body()?.let { authData ->
                sessionManager.saveSession(authData.userId, authData.token)
            }
        }
        return response
    }

    override suspend fun register(username: String, email: String, password: String): Response<AuthResponse> {
        val response = apiService.register(RegisterRequest(username, email, password))

        // Аналогично сохраняем данные сессии при успешной регистрации
        if (response.isSuccessful) {
            response.body()?.let { authData ->
                sessionManager.saveSession(authData.userId, authData.token)
            }
        }
        return response
    }

    override suspend fun getUsers(): Response<List<UserResponse>> {
        return apiService.getUsers()
    }

    override fun logout() {
        sessionManager.clearSession()
    }
}
