package ci.nsu.mobile.main.network

object RetrofitClient {
    private const val BASE_URL = "http://192.168.200.160:8080/api/"

    fun create(tokenManager: TokenManager): ApiService {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ApiService::class.java)
    }
}