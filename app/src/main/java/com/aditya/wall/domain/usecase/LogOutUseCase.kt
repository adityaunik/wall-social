package com.aditya.wall.domain.usecase

import com.aditya.wall.domain.repository.HomeRepository
import com.aditya.wall.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogOutUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    suspend operator fun invoke(): Flow<NetworkResponse<String>> = homeRepository.logOut()
}