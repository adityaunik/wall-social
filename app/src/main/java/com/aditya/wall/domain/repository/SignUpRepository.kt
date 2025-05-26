package com.aditya.wall.domain.repository

import androidx.credentials.Credential
import com.aditya.wall.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface SignUpRepository {
    suspend fun signUpWithGoogle(credential: Credential): Flow<NetworkResponse<String>>
}