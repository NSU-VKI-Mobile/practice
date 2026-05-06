package ci.nsu.mobile.main.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ci.nsu.mobile.main.utils.TokenManager


object NetworkClient {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        defaultRequest {
            url("http://192.168.200.160:8080/api/")
            contentType(ContentType.Application.Json)

            TokenManager.token?.let {
                headers.append("Authorization", "Bearer $it")
            }
        }
    }
}