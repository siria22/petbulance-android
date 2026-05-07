package com.petbulance.presentation.screen.feature.mypage.sections.user.permissionsettings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.user.AuthoritySettings
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicToggleSwitch
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PermissionSettingsScreen(
    navController: NavController,
    argument: PermissionSettingsArgument,
    data: PermissionSettingsData
) {
    val context = LocalContext.current
    val settings = data.settings

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is PermissionSettingsEvent.OpenAppSettings -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        this.data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "권한",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    isTrailingIconAvailable = false,
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PermissionToggleRow(
                title = "위치기반 서비스 이용 동의",
                checked = settings.locationService,
                onCheckedChange = { argument.intent(PermissionSettingsIntent.ToggleLocation) }
            )
            CommonDivider()

            PermissionToggleRow(
                title = "마케팅 활용 및 광고성 정보 수신",
                checked = settings.marketing,
                onCheckedChange = { argument.intent(PermissionSettingsIntent.ToggleMarketing) }
            )
            CommonDivider()

            PermissionToggleRow(
                title = "카메라 이용 동의",
                checked = settings.camera,
                onCheckedChange = { argument.intent(PermissionSettingsIntent.ToggleCamera) }
            )
            CommonDivider()
        }
    }
}

@Composable
private fun PermissionToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        Text(
            text = title,
            style = typography.bodyLarge,
            color = colorScheme.text.primary
        )
        BasicToggleSwitch(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            enabled = true
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun PermissionSettingsScreenPreview() {
    PetbulanceTheme {
        PermissionSettingsScreen(
            navController = rememberNavController(),
            argument = PermissionSettingsArgument(
                intent = {},
                dataState = PermissionSettingsDataState.Init,
                event = MutableSharedFlow()
            ),
            data = PermissionSettingsData(
                settings = AuthoritySettings(
                    locationService = true,
                    marketing = true,
                    camera = false
                )
            )
        )
    }
}
