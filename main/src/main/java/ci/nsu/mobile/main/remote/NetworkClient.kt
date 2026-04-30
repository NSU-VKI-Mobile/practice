package ci.nsu.mobile.main.remote

import io.ktor.client.HttpClient
import okhttp3.OkHttp

object NetworkClient {
    val client = HttpClient(OkHttp)
}