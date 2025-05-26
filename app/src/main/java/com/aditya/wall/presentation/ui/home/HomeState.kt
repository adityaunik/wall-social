package com.aditya.wall.presentation.ui.home

import com.aditya.wall.domain.model.Post

data class HomeState(
    var switchState:Boolean = false,
    val isPosting: Boolean = false,
    val addPostMessage: String = "",
    val postLoading: Boolean = false,
    val loggingOut: Boolean = false,
    val logOutMessage: String = "",
    val content: String = "",
    val posts: List<Post> = arrayListOf()
)
