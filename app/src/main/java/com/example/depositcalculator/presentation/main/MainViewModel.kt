package com.example.depositcalculator.presentation.main

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    fun closeApp(context: Context) {
        (context as? Activity)?.finishAffinity()
    }
}