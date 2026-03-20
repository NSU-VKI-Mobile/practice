package com.example.sheydaevtusk3

sealed class BottomNavigationState {
    object Home : BottomNavigationState()
    object Menu : BottomNavigationState()
    object Cart : BottomNavigationState()
    object Profile : BottomNavigationState()
}