package com.example.task_6.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)