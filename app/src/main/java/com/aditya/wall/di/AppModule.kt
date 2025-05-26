package com.aditya.wall.di

import com.aditya.wall.data.repository.HomeRepositoryImpl
import com.aditya.wall.data.repository.SignUpRepositoryImpl
import com.aditya.wall.domain.repository.HomeRepository
import com.aditya.wall.domain.repository.SignUpRepository
import com.aditya.wall.domain.usecase.AddPostUseCase
import com.aditya.wall.domain.usecase.FetchPostsUseCase
import com.aditya.wall.domain.usecase.FilterPostsUseCase
import com.aditya.wall.domain.usecase.GetProfileUseCase
import com.aditya.wall.domain.usecase.LogOutUseCase
import com.aditya.wall.domain.usecase.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSignUpRepo(
        fAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): SignUpRepository = SignUpRepositoryImpl(fAuth, firestore)

    @Provides
    @Singleton
    fun provideHomeRepo(
        fAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): HomeRepository = HomeRepositoryImpl(fAuth, firestore)

    @Provides
    @Singleton
    fun provideFetchPostsUseCase(
        homeRepository: HomeRepository
    ): FetchPostsUseCase = FetchPostsUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideFilterPostsUseCase(
        homeRepository: HomeRepository
    ): FilterPostsUseCase = FilterPostsUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideLogOutUseCase(
        homeRepository: HomeRepository
    ): LogOutUseCase = LogOutUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(
        signUpRepository: SignUpRepository
    ): SignUpUseCase = SignUpUseCase(signUpRepository)

    @Provides
    @Singleton
    fun provideGetProfileUseCase(
        homeRepository: HomeRepository
    ): GetProfileUseCase = GetProfileUseCase(homeRepository)

}