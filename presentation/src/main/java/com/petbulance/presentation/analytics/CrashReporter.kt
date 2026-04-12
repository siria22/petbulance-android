package com.petbulance.presentation.analytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import jakarta.inject.Inject
import jakarta.inject.Singleton

interface CrashReporter {
    fun setUserId(userId: String?)
    fun recordException(throwable: Throwable)
    fun log(message: String)
}

@Singleton
class CrashReporterImpl @Inject constructor() : CrashReporter {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun setUserId(userId: String?) {
        crashlytics.setUserId(userId ?: "")
    }

    override fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        crashlytics.log(message)
    }
}
