package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.TokenManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Инициализация менеджера токена при старте приложения
        TokenManager.init(this)
    }
}