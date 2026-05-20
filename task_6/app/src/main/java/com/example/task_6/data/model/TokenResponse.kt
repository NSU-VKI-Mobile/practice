package com.example.task_6.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class TokenResponse(
    @SerialName("token")
    val token: String,

    @SerialName("type")
    val type: String = "Bearer",      // Тип токена (обычно "Bearer")

    @SerialName("userId")
    val userId: Int? = null,           // Может быть null, если сервер не возвращает
)