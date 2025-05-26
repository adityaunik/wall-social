package com.aditya.wall.presentation.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aditya.wall.domain.usecase.SignUpUseCase
import com.aditya.wall.presentation.ui.home.HomeEvent
import com.aditya.wall.presentation.ui.home.HomeState
import com.aditya.wall.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
): ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    fun resetSignState() = viewModelScope.launch {
        _signUpState.value = SignUpState()
    }

    fun onEvent(event: SignUpEvent) = viewModelScope.launch {
        when(event){
            is SignUpEvent.OnSignInButtonClick -> {
                signUpUseCase.invoke(event.result.credential).collect{
                    when(it){
                        is NetworkResponse.Failure -> {
                            _signUpState.value = _signUpState.value.copy(
                                isSigningIn = false,
                                signInMessage = it.error.toString()
                            )
                        }
                        is NetworkResponse.Loading -> {
                            _signUpState.value = _signUpState.value.copy(
                                isSigningIn = true,
                                signInMessage = ""
                            )
                        }
                        is NetworkResponse.Success -> {
                            _signUpState.value = _signUpState.value.copy(
                                isSigningIn = false,
                                signInMessage = it.data.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}