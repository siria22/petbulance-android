package com.petbulance.presentation.screen.feature.mypage.sections.user.account

import MyPageAccountData
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.user.ConnectedSocials
import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.ThickDivider
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicToggleSwitch
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.screen.feature.mypage.sections.user.account.composables.LastConnectedSocialAccountWarningDialog
import com.petbulance.presentation.utils.hooks.login.rememberGoogleLoginManager
import com.petbulance.presentation.utils.hooks.login.rememberKakaoLoginManager
import com.petbulance.presentation.utils.hooks.login.rememberNaverLoginManager
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import kotlinx.coroutines.flow.MutableSharedFlow

private enum class AccountStatus {
    LOGGED_IN, CONNECTED, NOT_CONNECTED
}

@Composable
fun MyPageAccountScreen(
    navController: NavController,
    argument: MyPageAccountArgument,
    data: MyPageAccountData
) {
    val context = LocalContext.current

    var showLastConnectedSocialAccountWarningDialog by remember { mutableStateOf(false) }

    val loginWithKakao = rememberKakaoLoginManager { token ->
        token?.let {
            argument.intent(
                MyPageAccountIntent.ConnectSocial(LoginProviderType.KAKAO, it)
            )
        }
    }
    val loginWithNaver = rememberNaverLoginManager { token ->
        token?.let {
            argument.intent(
                MyPageAccountIntent.ConnectSocial(LoginProviderType.NAVER, it)
            )
        }
    }
    val loginWithGoogle = rememberGoogleLoginManager { token ->
        token?.let {
            argument.intent(
                MyPageAccountIntent.ConnectSocial(LoginProviderType.GOOGLE, it)
            )
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is MyPageAccountEvent.SocialLink.IsLast -> {
                    showLastConnectedSocialAccountWarningDialog = true
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "로그인 설정",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Filled.Settings)) {
                            navController.safeNavigate(ScreenDestinations.MyPage.User.Withdrawal.route)
                        }
                    )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MyPageAccountScreenContents(
                data = data,
                onIntent = argument.intent,
                loginWithKakao = loginWithKakao,
                loginWithNaver = loginWithNaver,
                loginWithGoogle = loginWithGoogle,
            )

            if (argument.dataState == MyPageAccountDataState.OnProgress) {
                OnContentLoadingUi("잠시만 기다려주세요...")
            }
        }
    }

    if (showLastConnectedSocialAccountWarningDialog) {
        LastConnectedSocialAccountWarningDialog(
            onDismissRequest = { showLastConnectedSocialAccountWarningDialog = false },
            onNavigateToSupport = {
                showLastConnectedSocialAccountWarningDialog = false
                navController.safeNavigate(ScreenDestinations.MyPage.User.Withdrawal.route)
            }
        )
    }
}

@Composable
private fun MyPageAccountScreenContents(
    data: MyPageAccountData,
    onIntent: (MyPageAccountIntent) -> Unit,
    loginWithKakao: () -> Unit,
    loginWithNaver: () -> Unit,
    loginWithGoogle: () -> Unit,
) {
    Column {
        AutoLoginSection(
            isEnabled = data.isAutoLoginEnabled,
            onToggle = { onIntent(MyPageAccountIntent.ToggleAutoLogin(it)) }
        )

        ThickDivider(color = colorScheme.bg.frame.subtle)

        data.userInfo?.let { info ->
            CurrentAccountSection(
                info = info,
                onDisconnectRequest = { onIntent(MyPageAccountIntent.DisconnectSocial(it)) }
            )
        }

        ThickDivider(color = colorScheme.bg.frame.subtle)

        data.userInfo?.let { info ->
            SocialConnectSection(
                currentLoginProvider = info.provider,
                connectedSocials = info.connectedSocials,
                onConnect = { provider ->
                    when (provider) {
                        LoginProviderType.KAKAO -> loginWithKakao()
                        LoginProviderType.NAVER -> loginWithNaver()
                        LoginProviderType.GOOGLE -> loginWithGoogle()
                    }
                },
                onDisconnect = { provider ->
                    onIntent(MyPageAccountIntent.DisconnectSocial(provider))
                }
            )
        }
    }
}

@Composable
private fun AutoLoginSection(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacingXL, horizontal = spacingMedium),
        ) {
            Text(
                text = "자동 로그인",
                style = typography.bodyLarge,
                color = colorScheme.text.primary
            )
            BasicToggleSwitch(
                checked = isEnabled,
                onCheckedChange = { onToggle(!isEnabled) },
                enabled = true
            )
        }
        CommonDivider()
        Text(
            text = "자동 로그인을 해제하면 모든 기기에서 로그아웃 처리됩니다.",
            color = colorScheme.text.caption,
            style = typography.labelSmall,
            modifier = Modifier.padding(
                start = spacingMedium,
                end = spacingMedium,
                top = spacingXS,
                bottom = spacingLarge
            )
        )
    }
}


@Composable
private fun CurrentAccountSection(
    info: UserInfo,
    onDisconnectRequest: (LoginProviderType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "현재 로그인된 계정",
            style = typography.labelLarge.emp(),
            color = colorScheme.text.primary,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingXS)
        )

        CommonDivider()

        val currentProvider = LoginProviderType.entries.find { it.name == info.provider }
            ?: LoginProviderType.KAKAO

        AccountItem(
            provider = currentProvider,
            email = info.email,
            status = AccountStatus.LOGGED_IN,
            onAction = { onDisconnectRequest(currentProvider) }
        )

        CommonDivider()

        Text(
            text = "최소 1개의 SNS 계정은 연결되어야 합니다.",
            color = colorScheme.text.caption,
            style = typography.labelSmall,
            modifier = Modifier.padding(
                start = spacingMedium,
                end = spacingMedium,
                top = spacingXS,
                bottom = spacingLarge
            )
        )
    }
}

@Composable
private fun AccountItem(
    provider: LoginProviderType,
    email: String?,
    status: AccountStatus,
    onAction: () -> Unit,
) {
    val iconRes = when (provider) {
        LoginProviderType.KAKAO -> R.drawable.login_kakao
        LoginProviderType.NAVER -> R.drawable.login_naver
        LoginProviderType.GOOGLE -> R.drawable.login_google
    }

    val actionBtnText = when (status) {
        AccountStatus.LOGGED_IN -> "로그아웃"
        AccountStatus.CONNECTED -> "연결해제"
        AccountStatus.NOT_CONNECTED -> "연결"
    }

    val actionBtnType = when (status) {
        AccountStatus.LOGGED_IN -> BasicButtonType.DEFAULT
        AccountStatus.CONNECTED -> BasicButtonType.DEFAULT
        AccountStatus.NOT_CONNECTED -> BasicButtonType.DEFAULT
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingXL, vertical = spacingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = provider.korean,
                    modifier = if (provider == LoginProviderType.NAVER) Modifier.size(16.dp) else Modifier.size(
                        20.dp
                    ),
                    colorFilter = if (provider == LoginProviderType.NAVER) ColorFilter.tint(
                        Color(0xFF03C75A)
                    ) else null
                )

                Text(
                    text = provider.korean,
                    style = typography.bodyLarge,
                    color = colorScheme.text.primary
                )
            }
            if (email != null) {
                Text(
                    text = email,
                    style = typography.bodySmall,
                    color = colorScheme.text.tertiary
                )
            }

        }

        BasicButton(
            text = actionBtnText,
            size = BasicButtonSize.XS,
            buttonType = actionBtnType,
            onClicked = onAction,
            radius = 8.dp
        )
    }
}

@Composable
private fun SocialConnectSection(
    currentLoginProvider: String,
    connectedSocials: ConnectedSocials,
    onConnect: (LoginProviderType) -> Unit,
    onDisconnect: (LoginProviderType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "SNS 계정 연결",
            style = typography.labelLarge.emp(),
            color = colorScheme.text.primary,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingXS)
        )

        SocialAccountConnectRow(
            visible = currentLoginProvider != "KAKAO",
            provider = LoginProviderType.KAKAO,
            email = connectedSocials.kakao,
            onConnect = onConnect,
            onDisconnect = onDisconnect
        )

        SocialAccountConnectRow(
            visible = currentLoginProvider != "GOOGLE",
            provider = LoginProviderType.GOOGLE,
            email = connectedSocials.google,
            onConnect = onConnect,
            onDisconnect = onDisconnect
        )

        SocialAccountConnectRow(
            visible = currentLoginProvider != "NAVER",
            provider = LoginProviderType.NAVER,
            email = connectedSocials.naver,
            onConnect = onConnect,
            onDisconnect = onDisconnect
        )

        CommonDivider()
    }
}

@Composable
private fun SocialAccountConnectRow(
    visible: Boolean,
    provider: LoginProviderType,
    email: String?,
    onConnect: (LoginProviderType) -> Unit,
    onDisconnect: (LoginProviderType) -> Unit,
) {
    if (!visible) return

    CommonDivider()
    AccountItem(
        provider = provider,
        email = email,
        status = if (email != null) AccountStatus.CONNECTED else AccountStatus.NOT_CONNECTED,
        onAction = {
            if (email != null) onDisconnect(provider) else onConnect(provider)
        }
    )
}

@Preview
@Composable
private fun MyPageAccountScreenPreview() {
    PetbulanceTheme {
        MyPageAccountScreen(
            navController = rememberNavController(),
            argument = MyPageAccountArgument(
                intent = { },
                dataState = MyPageAccountDataState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageAccountData.empty
        )
    }
}