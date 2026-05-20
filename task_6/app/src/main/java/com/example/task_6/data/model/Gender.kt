package com.example.task_6.data.model

enum class Gender(val value: String) {
    MALE("MALE"),
    FEMALE("FEMALE");

    companion object {
        fun fromString(value: String): Gender? {
            return values().find { it.value.equals(value, ignoreCase = true) }
        }
    }
}