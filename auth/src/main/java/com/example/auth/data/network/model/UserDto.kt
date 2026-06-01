package com.example.auth.data.network.model

import com.example.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Int,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int = 1,
    val authAllowed: Boolean = true,
    val personId: Int,
    val createdDate: String,
    val lastLoginDate: String? = null
)

fun UserDto.toDomainUser(): User {
    return User(
        userId = this.userId,
        login = this.login,
        email = this.email,
        phoneNumber = this.phoneNumber,
        roleId = this.roleId,
        authAllowed = this.authAllowed,
        personId = this.personId,
        createdDate = this.createdDate,
        lastLoginDate = this.lastLoginDate
    )
}