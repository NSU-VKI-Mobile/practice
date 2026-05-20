package com.example.task_6.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class UserDto(
    @SerialName("userId")
    val id: Int,

    @SerialName("login")
    val login: String,

    @SerialName("email")
    val email: String,

    @SerialName("phoneNumber")
    val phoneNumber: String,

    @SerialName("roleId")
    val roleId: Int,

    @SerialName("authAllowed")
    val authAllowed: Boolean,

    @SerialName("person")
    val person: PersonDto
)