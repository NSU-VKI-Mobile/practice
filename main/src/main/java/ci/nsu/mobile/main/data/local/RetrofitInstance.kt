package ci.nsu.mobile.main.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL =
        "http://192.168.200.160:8080/api/"

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

    val api: AuthApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(AuthApi::class.java)
}