package com.aditya.wall.presentation.navigation

sealed class Navigation(val route: String) {
    data object SignUp : Navigation("home")
    data object Home : Navigation("signup")
}