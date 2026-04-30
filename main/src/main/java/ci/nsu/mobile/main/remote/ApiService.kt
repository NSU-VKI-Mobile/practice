package ci.nsu.mobile.main.remote

import ci.nsu.mobile.main.model.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

class ApiService {
    private val client = NetworkClient.client

    suspend fun login(request: LoginRequest): AuthResponse {
        return client.post("auth/login") {
            setBody(request)
        }.body()
    }

    suspend fun register(request: RegisterRequest) {
        client.post("auth/login") {
            setBody(request)
        }
    }

    suspend fun getUsers(): List<UserDto> {
        return client.get("users").body()
    }

    suspend fun getGroups(): List<GroupDto> {
        return client.get("groups").body()
    }
}