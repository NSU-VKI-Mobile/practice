package com.example.depositcalculator.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object FirstStep : Screen("first_step")
    object SecondStep : Screen("second_step")
    object Result : Screen("result")
    object History : Screen("history")
    object Detail : Screen("detail/{id}") {
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun passArgs(id: Long) = "detail/$id"
    }
}