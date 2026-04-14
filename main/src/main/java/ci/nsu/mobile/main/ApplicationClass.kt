package ci.nsu.mobile.main

import android.app.Application
import android.content.Context

class ApplicationClass : Application() {
    companion object {
        lateinit var instance: ApplicationClass
            private set

        fun getAppContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}