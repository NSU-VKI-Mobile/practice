package com.example.auth.api.requestData

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenRespone(
    val token: String
)