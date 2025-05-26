package com.aditya.wall.domain.usecase

import androidx.credentials.Credential
import androidx.credentials.GetCredentialResponse
import com.aditya.wall.domain.repository.SignUpRepository
import com.aditya.wall.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {

    suspend operator fun invoke(credential: Credential): Flow<NetworkResponse<String>> = signUpRepository.signUpWithGoogle(credential)
}