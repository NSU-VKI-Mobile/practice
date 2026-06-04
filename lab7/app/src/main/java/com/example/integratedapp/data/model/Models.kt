package com.example.integratedapp.data.model

import com.google.gson.annotations.SerializedName

// DTO для работы с REST API сервера

data class GroupDto(
    @SerializedName("groupId") val id: Int,
    @SerializedName("groupName") val name: String
)

data class UserDto(
    @SerializedName("userId") val id: Long? = null,
    val login: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val person: PersonDto? = null,
    val token: String? = null
)

data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val groupId: Int? = null
)

data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val person: PersonDto
)

data class LoginRequest(
    val login: String,
    val password: String
)
