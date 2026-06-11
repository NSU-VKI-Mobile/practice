package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.auth.data.remote.TokenManager
import ci.nsu.mobile.auth.data.remote.UserManager

class MainApplication : Application() {

    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        UserManager.init(this)
        serviceLocator = ServiceLocator(this)
    }
}