package com.petbulance.petbulance

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petbulance.petbulance.common.nav.AppNavGraph
import com.petbulance.presentation.analytics.AnalyticsTracker
import com.petbulance.presentation.analytics.LocalAnalyticsTracker
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.utils.SdkInitializer
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sdkInitializer: SdkInitializer

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    private val viewModel: MainViewModel by viewModels()

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdkInitializer.initialize(this)

        enableEdgeToEdge()
        setContent {
            val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
            CompositionLocalProvider(LocalAnalyticsTracker provides analyticsTracker) {
                PetbulanceTheme(appTheme = appTheme) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppNavGraph(
                            modifier = Modifier.padding(innerPadding),
                            analyticsTracker = analyticsTracker
                        )
                    }
                }
            }
        }
    }
}
