package ci.nsu.data.repository

import ci.nsu.data.remote.dto.AuthResponse
import retrofit2.Response
import ci.nsu.data.remote.dto.UserResponse

interface AuthRepository {
    // Метод для входа в систему
    suspend fun login(username: String, password: String): Response<AuthResponse>

    // Метод для регистрации нового аккаунта
    suspend fun register(username: String, email: String, password: String): Response<AuthResponse>

    suspend fun getUsers(): Response<List<UserResponse>>

    // Метод для выхода из аккаунта (очистка сессии)
    fun logout()
}
