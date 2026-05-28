package com.example.authapp.navigation

sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Register : Screen("register")
    object Users    : Screen("users")
}
