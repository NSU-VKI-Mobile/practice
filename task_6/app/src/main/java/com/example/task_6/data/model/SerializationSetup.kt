package com.example.task_6.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Настройки JSON для парсинга
val jsonConfig = Json {
    ignoreUnknownKeys = true           // Игнорировать неизвестные поля
    isLenient = true                   // Либеральный режим парсинга
    coerceInputValues = true           // Приводить типы если возможно
    encodeDefaults = true              // Кодировать значения по умолчанию
}