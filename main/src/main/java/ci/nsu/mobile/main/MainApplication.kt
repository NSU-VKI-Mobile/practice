package ci.nsu.mobile.main

import android.app.Application
import ci.nsu.mobile.main.data.remote.TokenManager
import ci.nsu.mobile.main.data.remote.UserManager
import ci.nsu.mobile.main.di.ServiceLocator

class MainApplication : Application() {

    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
        UserManager.init(this)
        serviceLocator = ServiceLocator(this)
    }
}