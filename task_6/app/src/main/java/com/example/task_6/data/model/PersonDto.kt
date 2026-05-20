package com.example.task_6.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class PersonDto(
    @SerialName("firstName")
    val firstName: String,

    @SerialName("lastName")
    val lastName: String,

    @SerialName("middleName")
    val middleName: String? = null,  // Отчество может отсутствовать

    @SerialName("birthDate")
    val birthDate: String,            // Формат: "YYYY-MM-DD"

    @SerialName("gender")
    val gender: String,               // "MALE", "FEMALE" или другие значения

    @SerialName("groupId")
    val groupId: Int
)