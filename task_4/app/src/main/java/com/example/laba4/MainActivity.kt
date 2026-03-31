package com.example.laba4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.laba4.ui.theme.Laba4Theme
import com.example.laba4.screens.MainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laba4Theme() {
                MainScreen()
            }
        }
    }
}