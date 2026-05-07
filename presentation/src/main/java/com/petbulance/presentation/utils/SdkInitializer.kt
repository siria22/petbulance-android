package com.petbulance.presentation.utils

import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.petbulance.presentation.BuildConfig
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NidOAuth
import jakarta.inject.Inject

interface SdkInitializer {
    fun initialize(context: Context)
}

class SdkInitializerImpl @Inject constructor() : SdkInitializer {
    override fun initialize(context: Context) {

        FirebaseApp.initializeApp(context)

        NidOAuth.initialize(
            context = context,
            clientId = BuildConfig.NAVER_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
            clientName = "tmpName"
        )

        KakaoSdk.init(context, BuildConfig.KAKAO_NATIVE_APP_KEY)

        FacebookSdk.setApplicationId(BuildConfig.FB_APP_ID)
        FacebookSdk.setClientToken(BuildConfig.FB_CLIENT_TOKEN)
        FacebookSdk.setIsDebugEnabled(BuildConfig.DEBUG)
        FacebookSdk.sdkInitialize(context)
        AppEventsLogger.activateApp(context.applicationContext as android.app.Application)
    }
}
