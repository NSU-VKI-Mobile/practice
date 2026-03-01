package com.example.practicenow

import android.app.Application
import com.example.practicenow.data.local.TokenManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}