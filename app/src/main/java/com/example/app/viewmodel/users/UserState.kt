package com.example.app.viewmodel.users

import com.example.domain.models.User


data class UserState(
    val users: List<User> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val selectedBottomItem: String = "users"
)