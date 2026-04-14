package com.petbulance.presentation.screen.feature.mypage.sections.user.notificationsettings

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.user.NotificationSettings
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

@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    argument: NotificationSettingsArgument,
    data: NotificationSettingsData
) {
    val settings = data.settings

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "알림 설정",
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
            SettingsToggleRow(
                title = "앱 push 알림 수신",
                checked = settings.isAllEnabled,
                onCheckedChange = { argument.intent(NotificationSettingsIntent.ToggleAll(it)) }
            )
            CommonDivider()

            SettingsToggleRow(
                title = "이벤트 알림 수신",
                checked = settings.isEventEnabled,
                onCheckedChange = { argument.intent(NotificationSettingsIntent.ToggleEvent(it)) }
            )
            CommonDivider()

            SettingsToggleRow(
                title = "마케팅 알림 수신",
                checked = settings.isMarketingEnabled,
                onCheckedChange = { argument.intent(NotificationSettingsIntent.ToggleMarketing(it)) }
            )
            CommonDivider()
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
            onCheckedChange = { onCheckedChange(!checked) },
            enabled = true
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun NotificationSettingsScreenPreview() {
    PetbulanceTheme {
        NotificationSettingsScreen(
            navController = rememberNavController(),
            argument = NotificationSettingsArgument(
                intent = {},
                dataState = NotificationSettingsDataState.Init,
                event = MutableSharedFlow()
            ),
            data = NotificationSettingsData(
                settings = NotificationSettings(
                    isAllEnabled = true,
                    isEventEnabled = true,
                    isMarketingEnabled = true
                )
            )
        )
    }
}
