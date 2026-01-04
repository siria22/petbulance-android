package com.example.petbulance.common.di

import com.example.presentation.utils.SdkInitializer
import com.example.presentation.utils.SdkInitializerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SdkInitializerModule {

    @Binds
    @Singleton
    abstract fun bindSdkInitializer(initializer: SdkInitializerImpl): SdkInitializer
}