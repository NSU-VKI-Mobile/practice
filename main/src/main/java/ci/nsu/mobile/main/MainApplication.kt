package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.remote.TokenManager

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}