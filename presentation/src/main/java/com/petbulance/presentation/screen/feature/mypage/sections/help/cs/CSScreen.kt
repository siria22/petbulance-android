package com.petbulance.presentation.screen.feature.mypage.sections.help.cs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate

@Composable
fun CSScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "문의 및 고객센터",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.safeNavigate(ScreenDestinations.MyPage.route)
                    },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false,
                ),
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
        Box(modifier = Modifier.padding(innerPadding)) {
            CSScreenContents(
                onQnaClicked = {
                    navController.safeNavigate(ScreenDestinations.MyPage.Help.CS.Qna.List.route)
                },
                onCoalitionClicked = {
                    navController.safeNavigate(ScreenDestinations.MyPage.Help.CS.Coalition.route)
                }
            )
        }
    }
}

@Composable
private fun CSScreenContents(onQnaClicked: () -> Unit, onCoalitionClicked: () -> Unit) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CsItem(
            title = "문의 작성",
            desc = "서비스 이용 관련 문의를 작성해요",
            onClicked = onQnaClicked
        )

        CommonDivider()

        CsItem(
            title = "광고/병원 제휴 문의",
            desc = "광고 및 제휴 관련 상담을 신청해요",
            onClicked = onCoalitionClicked
        )

        CommonDivider()
    }
}

@Composable
private fun CsItem(
    title: String,
    desc: String,
    onClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = spacingSmall,
                horizontal = spacingMedium
            )
            .clickable { onClicked() }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
            Text(
                text = title,
                style = typography.bodyLarge,
                color = colorScheme.text.primary
            )
            Text(
                text = desc,
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
        }
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = "",
            size = iconSizeMedium,
            tint = colorScheme.icon.veryLight
        )
    }
}

@Preview
@Composable
private fun CSScreenPreview() {
    PetbulanceTheme {
        CSScreen(navController = rememberNavController())
    }
}