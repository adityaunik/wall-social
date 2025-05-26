package com.aditya.wall.presentation.ui.home

import com.aditya.wall.domain.model.Post

sealed class HomeEvent {
    data class OnContentValueChange(val content: String): HomeEvent()
    data class OnTogglePost(val checked: Boolean, val userId: String): HomeEvent()
    data class OnAddToWallButtonClick(val post: Post): HomeEvent()
    data object OnLogOutButtonClick: HomeEvent()
}