package com.aditya.wall.domain.repository

import com.aditya.wall.domain.model.Post
import com.aditya.wall.domain.model.Profile
import com.aditya.wall.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun addPost(post: Post): Flow<NetworkResponse<String>>
    suspend fun getPosts(): Flow<NetworkResponse<List<Post>>>
    suspend fun filterPosts(userId: String): Flow<NetworkResponse<List<Post>>>
    suspend fun logOut(): Flow<NetworkResponse<String>>
    suspend fun getProfile(): Flow<NetworkResponse<Profile>>
}