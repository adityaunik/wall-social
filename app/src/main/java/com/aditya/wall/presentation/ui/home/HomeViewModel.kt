package com.aditya.wall.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.wall.domain.model.Profile
import com.aditya.wall.domain.usecase.AddPostUseCase
import com.aditya.wall.domain.usecase.FetchPostsUseCase
import com.aditya.wall.domain.usecase.FilterPostsUseCase
import com.aditya.wall.domain.usecase.GetProfileUseCase
import com.aditya.wall.domain.usecase.LogOutUseCase
import com.aditya.wall.utils.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val fetchPostsUseCase: FetchPostsUseCase,
    private val filterPostsUseCase: FilterPostsUseCase,
    private val getProfileUseCase: GetProfileUseCase
): ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    init {
        getProfile()
    }

    fun fetchPosts() = viewModelScope.launch {
        fetchPostsUseCase.invoke().collect{
            when(it){
                is NetworkResponse.Failure -> {
                    Log.d("HomeViewModel", "fetchPosts:failure")
                    _homeState.value = _homeState.value.copy(
                        postLoading = false
                    )
                }
                is NetworkResponse.Loading -> {
                    _homeState.value = _homeState.value.copy(
                        posts = arrayListOf(),
                        postLoading = true
                    )
                }
                is NetworkResponse.Success -> {
                    Log.d("HomeViewModel", "fetchPosts:success " + it.data)
                    _homeState.value = _homeState.value.copy(
                        postLoading = false,
                        posts = it.data!!
                    )
                }
            }
        }
    }

    fun getProfile() = viewModelScope.launch {
        getProfileUseCase.invoke().collect{
            when(it){
                is NetworkResponse.Failure -> {}
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> {
                    Log.d("HomeViewModel", "getProfile:success " + it.data)
                    _profile.value = it.data!!
                }
            }
        }
    }

    fun resetPostContent() = viewModelScope.launch {
        _homeState.value = _homeState.value.copy(
            content = "",
            addPostMessage = ""
        )
    }

    fun resetHomeState() = viewModelScope.launch {
        _homeState.value = HomeState()
    }

    fun onEvent(event: HomeEvent) = viewModelScope.launch {
        when (event) {
            is HomeEvent.OnContentValueChange -> {
                _homeState.value = _homeState.value.copy(
                    content = event.content
                )
            }

            HomeEvent.OnLogOutButtonClick -> {
                logOutUseCase.invoke().collect{
                    when(it){
                        is NetworkResponse.Failure -> {
                            _homeState.value = _homeState.value.copy(
                                loggingOut = false,
                                logOutMessage = it.error.toString()
                            )
                        }
                        is NetworkResponse.Loading -> {
                            _homeState.value = _homeState.value.copy(
                                loggingOut = true,
                                logOutMessage = ""
                            )
                        }
                        is NetworkResponse.Success -> {
                            _homeState.value = _homeState.value.copy(
                                loggingOut = false,
                                logOutMessage = it.data.toString()
                            )
                        }
                    }
                }
            }

            is HomeEvent.OnTogglePost -> {
                _homeState.value = _homeState.value.copy(
                    switchState = event.checked
                )
                when (event.checked) {
                    true -> {
                        filterPostsUseCase.invoke(event.userId).collect {
                           when(it){
                               is NetworkResponse.Failure -> {
                                   _homeState.value = _homeState.value.copy(
                                       postLoading = false
                                   )
                               }
                               is NetworkResponse.Loading -> {
                                   _homeState.value = _homeState.value.copy(
                                       posts = arrayListOf(),
                                       postLoading = true
                                   )
                               }
                               is NetworkResponse.Success -> {
                                   _homeState.value = _homeState.value.copy(
                                       postLoading = false,
                                       posts = it.data!!
                                   )
                               }
                           }
                        }
                    }

                    false -> {
                        fetchPostsUseCase.invoke().collect {
                            when(it){
                                is NetworkResponse.Failure -> {
                                    _homeState.value = _homeState.value.copy(
                                        postLoading = false
                                    )
                                }
                                is NetworkResponse.Loading -> {
                                    _homeState.value = _homeState.value.copy(
                                        posts = arrayListOf(),
                                        postLoading = true
                                    )
                                }
                                is NetworkResponse.Success -> {
                                    _homeState.value = _homeState.value.copy(
                                        postLoading = false,
                                        posts = it.data!!
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is HomeEvent.OnAddToWallButtonClick -> {
                addPostUseCase.invoke(event.post).collect {
                    when(it){
                        is NetworkResponse.Failure -> {
                            _homeState.value = _homeState.value.copy(
                                isPosting = false,
                                addPostMessage = it.error.toString()
                            )
                        }
                        is NetworkResponse.Loading -> {
                            _homeState.value = _homeState.value.copy(
                                isPosting = true,
                                addPostMessage = ""
                            )
                        }
                        is NetworkResponse.Success -> {
                            _homeState.value = _homeState.value.copy(
                                isPosting = false,
                                addPostMessage = it.data.toString()
                            )
                        }
                    }

                }
            }
        }
    }
}