package com.petbulance.presentation.screen.feature.mypage.sections.help.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.support.notice.NoticeListItem
import com.petbulance.domain.model.type.NoticeStatusType
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageNoticeScreen(
    navController: NavController,
    argument: MyPageNoticeArgument,
    data: MyPageNoticeData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dataState = argument.dataState
    val screenState = argument.screenState

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is MyPageNoticeEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "공지사항",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.safePopBackStack()
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (data.isLoadingNextPage) {
                OnContentLoadingUi(text = "잠시만 기다려주세요...")
            } else {
                MyPageNoticeScreenContents(notices = data.notices)
            }
        }
    }
}

@Composable
private fun MyPageNoticeScreenContents(
    notices: List<NoticeListItem>
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(notices) { notice ->
            NoticeListItemCard(
                noticeListItem = notice,
                onNoticeClicked = {
                    /* TODO : Navigate to Notice Details */
                }
            )
            CommonDivider()
        }
    }
}

@Composable
private fun NoticeListItemCard(
    noticeListItem: NoticeListItem,
    onNoticeClicked: () -> Unit
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
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
            NoticeStatusChip(noticeListItem.noticeStatus)
            Text(
                text = noticeListItem.title,
                style = typography.bodyMedium,
                color = colorScheme.text.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = noticeListItem.createdAt,
                style = typography.labelMedium,
                color = colorScheme.text.caption,
                overflow = TextOverflow.Ellipsis
            )
        }
        BasicIcon(
            modifier = Modifier.clickable { onNoticeClicked() },
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = "Navigate to notice detail",
            size = iconSizeMedium,
            tint = colorScheme.icon.veryLight
        )
    }
}

@Composable
private fun NoticeStatusChip(status: NoticeStatusType) {
    val backgroundColor = when (status) {
        NoticeStatusType.EVENT -> colorScheme.tag.blue.bg
        NoticeStatusType.ADVERTISING -> colorScheme.tag.green.bg
        NoticeStatusType.NOTICE -> colorScheme.tag.yellow.subtle
    }

    val textColor = when (status) {
        NoticeStatusType.EVENT -> colorScheme.tag.blue.strong
        NoticeStatusType.ADVERTISING -> colorScheme.tag.green.medium
        NoticeStatusType.NOTICE -> colorScheme.tag.trust.verystrong
    }

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(vertical = spacingXXXS, horizontal = spacingXS)
    ) {
        Text(
            text = status.korean,
            color = textColor,
            style = typography.labelMedium.emp()
        )
    }
}

@Preview
@Composable
private fun MyPageNoticeScreenPreview() {
    PetbulanceTheme {
        MyPageNoticeScreen(
            navController = rememberNavController(),
            argument = MyPageNoticeArgument(
                intent = { },
                dataState = MyPageNoticeDataState.Init,
                screenState = MyPageNoticeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageNoticeData.stub()
        )
    }
}