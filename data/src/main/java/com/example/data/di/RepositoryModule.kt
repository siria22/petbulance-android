package com.example.data.di

import com.example.data.repository.nonfeature.preference.PreferenceRepositoryImpl
import com.example.data.repository.ExampleRepositoryImpl
import com.example.domain.repository.feature.ExampleRepository
import com.example.domain.repository.nonfeature.preference.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

}