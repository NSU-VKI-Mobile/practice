package ci.nsu.mobile.main.data.api


import ci.nsu.mobile.main.data.interceptor.AuthInterceptor
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.LoginRequest
import ci.nsu.mobile.main.data.model.LoginResponse
import ci.nsu.mobile.main.data.model.RegisterRequest
import ci.nsu.mobile.main.data.model.UserDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.okhttp.OkHttp


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

        engine {
            config {
                interceptors().add(AuthInterceptor())
            }
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
        return client.get("${baseUrl}users").body()
    }

    suspend fun getGroups(): List<GroupDto> {
        return client.get("${baseUrl}groups").body()
    }
}