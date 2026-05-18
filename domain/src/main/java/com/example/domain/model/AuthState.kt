package com.example.domain.model

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticated   : AuthState()
}