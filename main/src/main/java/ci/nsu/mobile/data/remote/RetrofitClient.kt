package ci.nsu.mobile.data.remote

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "http://192.168.200.160:8080/api/"

    val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val api: ApiService by lazy {

        Log.d("API", "BASE_URL = $BASE_URL")

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}