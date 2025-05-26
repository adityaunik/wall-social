package com.aditya.wall.domain.usecase

import com.aditya.wall.domain.model.Post
import com.aditya.wall.domain.repository.HomeRepository
import com.aditya.wall.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddPostUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    suspend operator fun invoke(post: Post): Flow<NetworkResponse<String>> = homeRepository.addPost(post)
}