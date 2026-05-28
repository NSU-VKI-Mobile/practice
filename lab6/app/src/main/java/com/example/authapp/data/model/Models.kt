package com.example.authapp.data.model

import com.google.gson.annotations.SerializedName

// DTO = Data Transfer Object — объект для передачи данных между сервером и приложением
// @SerializedName — говорит Gson: "в JSON это поле называется вот так"
// Например сервер шлёт {"groupId": 5} — Gson запишет это в поле id

// Группа (учебная группа, например "2307б2")
data class GroupDto(
    @SerializedName("groupId")
    val id: Int,
    @SerializedName("groupName")
    val name: String
)

// Пользователь (полученный от сервера)
data class UserDto(
    @SerializedName("userId")
    val id: Int? = null,
    val login: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val person: PersonDto? = null,
    val token: String? = null  // JWT-токен приходит при логине
)

// Персональные данные человека
data class PersonDto(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val groupId: Int? = null
)

// Запрос на регистрацию — то что мы ОТПРАВЛЯЕМ серверу
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val roleId: Int = 1,           // всегда 1 по заданию
    val authAllowed: Boolean = true,
    val person: PersonDto
)

// Запрос на вход — логин + пароль
data class LoginRequest(
    val login: String,
    val password: String
)
