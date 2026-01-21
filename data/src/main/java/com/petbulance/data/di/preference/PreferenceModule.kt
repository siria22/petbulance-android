package com.petbulance.data.di.preference

import android.content.Context
import com.petbulance.data.datasource.local.preference.PreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun provideUserPreference(@ApplicationContext context: Context): PreferenceProvider {
        return PreferenceProvider(context)
    }
}
