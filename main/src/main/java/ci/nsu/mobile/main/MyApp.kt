package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.utils.TokenManager

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}