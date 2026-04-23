package ci.nsu.mobile.main.remote

import kotlinx.serialization.json.Json


//object RetrofitClient {
//    private val json = Json {
//        ignoreUnknownKeys = true
//    }
//
//    private val vlient = OkHttpClient.Builder
//        .addInterceptor(AuthInterceptor())
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
//        .build()
//
//    val api: ApiService = Retrofit.Builder()
//        .baseUrl("http://192.168.200.160:8080/api/")
//        .client(client)
//        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//        .build()
//        .create(ApiService::class.java)
//}