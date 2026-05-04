package com.example.practicenow.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    @SerialName("groupId") val id: Int,
    @SerialName("groupName") val name: String
)

@Serializable
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: String,
    val gender: String,
    val groupId: Int
)

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String? = null // Сделал nullable на случай, если сервер возвращает не объект, а просто строку
)

@Serializable
data class UserDto(
    val id: Int = 0,
    val login: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)