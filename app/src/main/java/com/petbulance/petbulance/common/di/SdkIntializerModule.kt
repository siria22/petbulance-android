package com.petbulance.petbulance.common.di

import com.petbulance.presentation.utils.SdkInitializer
import com.petbulance.presentation.utils.SdkInitializerImpl
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