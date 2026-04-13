package com.petbulance.presentation.screen.feature.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.petbulance.domain.model.feature.support.notice.NoticeListItem
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.screen.feature.mypage.sections.help.notice.composables.NoticeStatusChip
import com.petbulance.presentation.screen.feature.notification.composables.NotificationItemCard
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationScreen(
    navController: NavController,
    argument: NotificationArgument,
    data: NotificationData
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is NotificationEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "알림",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Filled.Settings)) {
                            // TODO: 알림 설정 화면 네비게이션
                        }
                    )
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab Row
            val tabs = NotificationTab.entries
            val selectedIndex = tabs.indexOf(data.selectedTab)

            @Suppress("DEPRECATION")
            TabRow(
                selectedTabIndex = selectedIndex,
                containerColor = colorScheme.bg.frame.default,
                contentColor = colorScheme.text.primary,
                indicator = { tabPositions ->
                    if (selectedIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                            color = colorScheme.text.primary
                        )
                    }
                },
                divider = { CommonDivider() }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedIndex == index,
                        onClick = { argument.intent(NotificationIntent.SwitchTab(tab)) },
                        text = {
                            Text(
                                text = tab.korean,
                                style = if (selectedIndex == index) typography.titleSmall.emp()
                                else typography.titleSmall,
                                color = if (selectedIndex == index) colorScheme.text.primary
                                else colorScheme.text.caption
                            )
                        }
                    )
                }
            }

            when (data.selectedTab) {
                NotificationTab.NOTICE -> NoticeTabContent(
                    notices = data.notices,
                    isLoading = argument.dataState == NotificationDataState.Loading,
                    isLoadingNextPage = data.isNoticeLoadingNextPage,
                    onLoadMore = { argument.intent(NotificationIntent.LoadMoreNotices) },
                    onNoticeClick = { noticeId ->
                        navController.safeNavigate(
                            ScreenDestinations.MyPage.Help.Notice.Detail.createRoute(noticeId)
                        )
                    }
                )

                NotificationTab.ACTIVITY -> ActivityTabContent(
                    notifications = data.notifications,
                    isLoading = argument.dataState == NotificationDataState.Loading,
                    isLoadingNextPage = data.isNotificationLoadingNextPage,
                    onLoadMore = { argument.intent(NotificationIntent.LoadMoreNotifications) },
                    onReadAll = { argument.intent(NotificationIntent.ReadAllNotifications) },
                    onDeleteAll = { showDeleteDialog = true },
                    onItemClick = { /* TODO: targetType 기반 네비게이션 */ }
                )
            }
        }
    }

    if (showDeleteDialog) {
        WarningDialog(
            title = "알림함을 비우시겠어요?",
            cancelText = "아니오",
            confirmText = "예",
            content = "",
            onDismissRequest = { showDeleteDialog = false },
            onExitButtonClicked = {
                showDeleteDialog = false
                argument.intent(NotificationIntent.DeleteAllNotifications)
            }
        )
    }
}

@Composable
private fun NoticeTabContent(
    notices: List<NoticeListItem>,
    isLoading: Boolean,
    isLoadingNextPage: Boolean,
    onLoadMore: () -> Unit,
    onNoticeClick: (Long) -> Unit
) {
    if (isLoading) {
        OnContentLoadingUi("불러오는 중...")
        return
    }

    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem >= listState.layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore }.collect { if (it) onLoadMore() }
    }

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(notices, key = { it.noticeId }) { notice ->
            NoticeItem(notice = notice, onClick = { onNoticeClick(notice.noticeId) })
        }
        if (isLoadingNextPage) {
            item { OnContentLoadingUi("불러오는 중...") }
        }
    }
}

@Composable
private fun NoticeItem(
    notice: NoticeListItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = spacingMedium, vertical = spacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacingXS)
        ) {
            NoticeStatusChip(status = notice.noticeStatus)
            Text(
                text = notice.title,
                style = typography.bodyMedium,
                color = colorScheme.text.primary
            )
            Text(
                text = notice.createdAt,
                style = typography.labelSmall,
                color = colorScheme.text.caption
            )
        }
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Filled.ChevronRight),
            contentDescription = "상세보기",
            size = iconSizeSmall,
            tint = colorScheme.icon.light
        )
    }
    CommonDivider()
}

@Composable
private fun ActivityTabContent(
    notifications: List<com.petbulance.domain.model.feature.notification.NotificationItem>,
    isLoading: Boolean,
    isLoadingNextPage: Boolean,
    onLoadMore: () -> Unit,
    onReadAll: () -> Unit,
    onDeleteAll: () -> Unit,
    onItemClick: (com.petbulance.domain.model.feature.notification.NotificationItem) -> Unit
) {
    if (isLoading) {
        OnContentLoadingUi("불러오는 중...")
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Sub-header: delete + read all
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingMedium, vertical = spacingXS),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Filled.Delete),
                contentDescription = "전체 삭제",
                size = iconSizeSmall,
                tint = colorScheme.icon.light,
                modifier = Modifier.clickable(onClick = onDeleteAll)
            )
            Text(
                text = "전체읽음",
                style = typography.bodyMedium.emp(),
                color = colorScheme.text.primary,
                modifier = Modifier.clickable(onClick = onReadAll)
            )
        }
        CommonDivider()

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "알림이 없습니다.",
                    style = typography.bodyMedium,
                    color = colorScheme.text.caption
                )
            }
        } else {
            val listState = rememberLazyListState()
            val shouldLoadMore by remember {
                derivedStateOf {
                    val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    lastVisibleItem >= listState.layoutInfo.totalItemsCount - 3
                }
            }

            LaunchedEffect(shouldLoadMore) {
                snapshotFlow { shouldLoadMore }.collect { if (it) onLoadMore() }
            }

            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(notifications, key = { it.notificationId }) { item ->
                    NotificationItemCard(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
                if (isLoadingNextPage) {
                    item { OnContentLoadingUi("불러오는 중...") }
                }
            }
        }
    }
}
