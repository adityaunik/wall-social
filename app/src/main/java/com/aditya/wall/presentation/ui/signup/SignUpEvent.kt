package com.aditya.wall.presentation.ui.signup

import androidx.credentials.GetCredentialResponse
import com.aditya.wall.presentation.ui.home.HomeEvent

sealed class SignUpEvent {
    data class OnSignInButtonClick(val result: GetCredentialResponse): SignUpEvent()
}