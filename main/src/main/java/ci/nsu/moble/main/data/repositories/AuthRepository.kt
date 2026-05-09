package ci.nsu.moble.main.data.repositories

import ci.nsu.moble.main.api.ApiService
import ci.nsu.moble.main.data.dto.LoginRequestDto
import ci.nsu.moble.main.data.dto.RegisterRequestDto

/**
 *Wraps HTTP answers and handles potential errors and exceptions
 */
class AuthRepository(private val api: ApiService) {
    suspend fun login(login: String, pass: String) = runCatching { api.login(
        LoginRequestDto(
            login,
            pass
        )
    ) }
    suspend fun register(req: RegisterRequestDto) = runCatching { api.register(req) }
    suspend fun getUsers() = runCatching { api.getUsers() }
    suspend fun getGroups() = runCatching { api.getGroups() }
}