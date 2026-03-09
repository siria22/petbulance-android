package com.petbulance.presentation.screen.feature.mypage.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageScreen(
    navController: NavController,
    argument: MyPageArgument,
    data: MyPageData
) {
    var isLoginRequiredDialogVisible by remember { mutableStateOf(false) }

    val userInfo = data.userInfo

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is MyPageEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "마이페이지",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = false,
                    trailingIcons = listOf(Pair(IconResource.Vector(Icons.Filled.NotificationsNone)) { })
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.subtle
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MyPageScreenContents(
                userInfo = userInfo,
                navController = navController,
                appLatestVersion = data.latestVersion,
                appCurrentVersion = data.currentVersion
            )
        }
    }

    if (isLoginRequiredDialogVisible) {
        LoginRequiredDialog(
            onDismiss = { isLoginRequiredDialogVisible = false },
            onLoginButtonClicked = {
                navController.safeNavigate(ScreenDestinations.Login.route)
                isLoginRequiredDialogVisible = false
            },
        )
    }
    // BackHandler {  }
}

@Composable
private fun MyPageScreenContents(
    userInfo: UserInfo?,
    navController: NavController,
    appLatestVersion: String,
    appCurrentVersion: String,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LoginOrUserInfo(
            userInfo,
            navigateToLogin = { navController.safeNavigate(ScreenDestinations.Login.route) },
            navigateToProfileEdit = {
                navController.safeNavigate(ScreenDestinations.MyPage.User.Profile.route)
            },
        )

        MyPageSection(
            sectionTitle = "사용자 설정",
            options = listOf(
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_notification),
                    title = "알림 설정",
                    onClicked = { /* TODO */ }
                ),
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_logout),
                    title = "로그인 계정 관리",
                    onClicked = { navController.safeNavigate(ScreenDestinations.MyPage.User.Account.route) }
                ),
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_permission),
                    title = "권한",
                    onClicked = { /* TODO */ }
                )
            ),
        )

        MyPageSection(
            sectionTitle = "작성글 관리",
            options = listOf(
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_reviews),
                    title = "후기 관리",
                    onClicked = { navController.safeNavigate(ScreenDestinations.MyPage.Activity.Reviews.route) }
                ),
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_docs),
                    title = "게시글 관리",
                    onClicked = { /* TODO */ }
                ),
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_comments),
                    title = "댓글 관리",
                    onClicked = { /* TODO */ }
                )
            ),
        )

        MyPageSection(
            sectionTitle = "고객 지원",
            options = listOf(
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_bullhorn),
                    title = "공지사항",
                    onClicked = { navController.safeNavigate(ScreenDestinations.MyPage.Help.Notice.route) }
                ),
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_headset),
                    title = "문의 및 고객센터",
                    onClicked = { navController.safeNavigate(ScreenDestinations.MyPage.Help.CS.route) }
                ),
                MyPageSectionItem(
                    iconResource = IconResource.Drawable(R.drawable.ic_information),
                    title = "약관 및 정책",
                    onClicked = { navController.safeNavigate(ScreenDestinations.MyPage.Help.Terms.route) }
                )
            ),
            isLast = true,
            appLatestVersion = appLatestVersion,
            appCurrentVersion = appCurrentVersion
        )
    }
}

@Composable
private fun LoginOrUserInfo(
    user: UserInfo?,
    navigateToLogin: () -> Unit,
    navigateToProfileEdit: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.bg.frame.default, RoundedCornerShape(12.dp))
            .padding(spacingMedium)
    ) {
        if (user != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicImageBox(
                    modifier = Modifier.clip(RoundedCornerShape(1000.dp)),
                    size = 60.dp,
                    uri = user.profileImageUrl?.toUri(),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = user.nickname,
                        style = typography.bodyMedium.emp(),
                        color = colorScheme.text.primary
                    )
                    Text(
                        text = user.email.ifEmpty { "등록된 이메일이 없어요" },
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }
            }

            BasicButton(
                text = "프로필 수정",
                size = BasicButtonSize.XS,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp
            ) { navigateToProfileEdit() }

        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "로그인 해주세요",
                    style = typography.bodyMedium.emp(),
                    color = colorScheme.text.primary
                )
                Text(
                    text = "회원가입까지 단 3초!",
                    style = typography.labelSmall,
                    color = colorScheme.text.caption
                )
            }

            BasicButton(
                text = "로그인 하기",
                size = BasicButtonSize.XS,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp
            ) { navigateToLogin() }
        }
    }
}

data class MyPageSectionItem(
    val iconResource: IconResource,
    val title: String,
    val onClicked: () -> Unit
)

@Composable
private fun MyPageSection(
    sectionTitle: String,
    options: List<MyPageSectionItem>,
    isLast: Boolean = false,
    appLatestVersion: String = "",
    appCurrentVersion: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorScheme.bg.frame.default,
                RoundedCornerShape(20.dp)
            )
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier.padding(vertical = spacingXXS, horizontal = spacingMedium)
        ) {
            Text(
                text = sectionTitle,
                style = typography.labelLarge.emp(),
                color = colorScheme.text.primary,
            )
        }
        options.forEach { elem ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = elem.onClicked)
                    .padding(horizontal = spacingMedium, vertical = spacingSmall)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicIcon(
                        iconResource = elem.iconResource,
                        contentDescription = elem.title,
                        size = iconSizeMS,
                        tint = colorScheme.icon.dark
                    )
                    Text(
                        text = elem.title,
                        style = typography.bodyMedium,
                        color = colorScheme.text.primary
                    )
                }
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Filled.ChevronRight),
                    contentDescription = "Navigate to screen",
                    size = iconSizeMS,
                    tint = colorScheme.icon.dark
                )
            }
        }

        if (isLast) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingSmall)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacingXS),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicIcon(
                            iconResource = IconResource.Drawable(R.drawable.ic_version),
                            contentDescription = "version",
                            size = iconSizeMS,
                            tint = colorScheme.icon.dark
                        )
                        Text(
                            text = "최신버전 업데이트",
                            style = typography.bodyMedium,
                            color = colorScheme.text.primary
                        )
                    }
                    Text(
                        text = "최신버전: $appLatestVersion",
                        style = typography.labelMedium,
                        color = colorScheme.text.caption
                    )
                }
                Text(
                    text = appCurrentVersion,
                    style = typography.bodySmall.emp(),
                    color = colorScheme.tag.green.medium
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    PetbulanceTheme {
        MyPageScreen(
            navController = rememberNavController(),
            argument = MyPageArgument(
                intent = { },
                dataState = MyPageDataState.Init,
                screenState = MyPageScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageData(
                userInfo = UserInfo.stub,
                currentVersion = "1.0.0",
                latestVersion = "1.0.1",
            )
        )
    }
}