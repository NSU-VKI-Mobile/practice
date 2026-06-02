package ci.nsu.mobile.main.data.network

import ci.nsu.mobile.main.utils.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//настройка Retrofit и OkHttp
object RetrofitInstance { //object - singleton, один экземпляр на всё приложение
    private const val BASE_URL = "http://192.168.200.160:8080/api/" //адрес сервера

    fun create(tokenManager: TokenManager): ApiService {
        //Логирование запросов (для отладки)
        val loggingInterceptor = HttpLoggingInterceptor().apply { //HttpLoggingInterceptor - логирует все запросы (видно в Logcat)
            level = HttpLoggingInterceptor.Level.BODY
        }

        // OkHttpClient с нашими перехватчиками
        val client = OkHttpClient.Builder() //OkHttpClient - низкоуровневый HTTP-клиент
            .addInterceptor(AuthInterceptor(tokenManager)) // добавляем токен
            .addInterceptor(loggingInterceptor)  // логируем
            .build()

        // Retrofit - главный клиент для API
        //Retrofit - высокоуровневый клиент, превращает интерфейс ApiService в работающий код
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // базовый URL
            .client(client) // наш настроенный клиент
            .addConverterFactory(GsonConverterFactory.create()) // парсим JSON в объекты
            .build()

        // Создаем реализацию ApiService
        return retrofit.create(ApiService::class.java)
    }
}