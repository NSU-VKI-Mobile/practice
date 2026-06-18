package ci.nsu.mobile.main

import android.app.Application
import android.util.Log
import ci.nsu.mobile.main.data.security.TokenManager

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Инициализируем TokenManager один раз при старте приложения
        TokenManager.init(this)
        Log.d("MainApplication", "TokenManager initialized with ApplicationContext")
    }
}