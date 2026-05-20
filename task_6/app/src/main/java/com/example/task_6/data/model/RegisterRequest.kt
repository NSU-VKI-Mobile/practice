package com.example.task_6.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class RegisterRequest(
    @SerialName("login")
    val login: String,

    @SerialName("password")
    val password: String,

    @SerialName("email")
    val email: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("roleId")
    val roleId: Int = 1,              // Всегда 1, не меняется

    @SerialName("authAllowed")
    val authAllowed: Boolean = true,

    @SerialName("person")
    val person: PersonDto
)