package com.example.auth.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
