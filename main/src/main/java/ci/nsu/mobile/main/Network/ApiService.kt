package ci.nsu.mobile.main.Network

import ci.nsu.mobile.main.Data.DataModels.GroupDto
import ci.nsu.mobile.main.Data.DataModels.LoginRequest
import ci.nsu.mobile.main.Data.DataModels.LoginResponse
import ci.nsu.mobile.main.Data.DataModels.RegisterRequest
import ci.nsu.mobile.main.Data.DataModels.UserDto
import ci.nsu.mobile.main.Token.TokenManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest

object ApiService {
    var baseUrl: String = "http://192.168.200.160:8080/api/"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(DefaultRequest) {
            header("Content-Type", "application/json")
        }
    }

    private suspend fun HttpRequestBuilder.addAuthToken() {
        val token = TokenManager.token
        if (token != null) {
            header("Authorization", "Bearer $token")
        }
    }

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return client.post("${baseUrl}auth/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }.body()
    }

    suspend fun register(registerRequest: RegisterRequest): HttpResponse {
        return client.post("${baseUrl}auth/register") {
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }
    }

    suspend fun getUsers(): List<UserDto> {
        return client.get("${baseUrl}users") {
            addAuthToken()
        }.body()
    }

    suspend fun getGroups(): List<GroupDto> {
        return client.get("${baseUrl}groups") {
            addAuthToken()
        }.body()
    }
}