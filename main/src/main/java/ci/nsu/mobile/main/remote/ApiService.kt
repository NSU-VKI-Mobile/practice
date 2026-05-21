package ci.nsu.mobile.main.remote

import ci.nsu.mobile.main.model.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import android.util.Log

class ApiService {
    private val client = NetworkClient.client

    suspend fun login(request: LoginRequest): AuthResponse {
        val response = client.post("auth/login") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess() || response.contentType()?.match(ContentType.Application.Json) != true) {
            val errorBody = runCatching { response.bodyAsText() }.getOrNull() ?: "No response body"
            throw Exception("Login failed: ${response.status}, body: $errorBody")
        }

        return response.body<AuthResponse>()

    }

    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            val response = client.post("auth/register") {
                setBody(request)
                contentType(ContentType.Application.Json)
            }

            val status = response.status.value
            val body = runCatching { response.bodyAsText() }.getOrDefault("<<empty>>")
            Log.d("API_REGISTER", "Status: ${response.status}, Body: $body")

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Registrator failed: $status"))
            }
        } catch (e: Exception) {
            Log.e("API_REGISTER", "Error: ${e.message}", e)
            Result.failure(e)
        }

    }

    suspend fun getUsers(): List<UserDto> {
        return client.get("users").body()
    }

    suspend fun getGroups(): List<GroupDto> {
        return client.get("groups").body()
    }
}