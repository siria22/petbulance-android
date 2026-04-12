package com.petbulance.presentation.analytics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(impl: AnalyticsTrackerImpl): AnalyticsTracker

    @Binds
    @Singleton
    abstract fun bindCrashReporter(impl: CrashReporterImpl): CrashReporter
}
