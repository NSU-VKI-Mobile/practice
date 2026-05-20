package com.example.task_6.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable
data class ApiError(
    @SerialName("message")
    val message: String,

    @SerialName("status")
    val status: Int? = null,

    @SerialName("error")
    val error: String? = null,

    @SerialName("timestamp")
    val timestamp: String? = null
)