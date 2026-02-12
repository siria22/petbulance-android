package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import MyPageNoticeDetailData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.support.notice.AdjacentNotice
import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.screen.feature.mypage.sections.help.notice.composables.NoticeStatusChip
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageNoticeDetailScreen(
    navController: NavController,
    argument: MyPageNoticeDetailArgument,
    data: MyPageNoticeDetailData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dataState = argument.dataState
    val screenState = argument.screenState

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is MyPageNoticeDetailEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
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
            MyPageNoticeDetailScreenContents(
                noticeDetail = data.noticeDetail,
                callOtherNotice = { noticeId ->
                    argument.intent(MyPageNoticeDetailIntent.NavigateToNotice(noticeId))
                }
            )
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
private fun MyPageNoticeDetailScreenContents(
    noticeDetail: NoticeDetail?,
    callOtherNotice: (Long) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXL),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .heightIn(screenHeight)
                .padding(spacingMedium)
        ) {
            if (noticeDetail == null) {
                EmptyResult()
            } else {
                NoticeHeaderSection(notice = noticeDetail)

                if (noticeDetail.buttons.isNullOrEmpty()) {
                    Text(
                        text = noticeDetail.content,
                        style = typography.bodyMedium,
                        color = colorScheme.text.secondary
                    )
                } else {
                    // TODO : Hardcoded Banner Notice Info

                }
            }
            Spacer(modifier = Modifier.weight(1f))

            NoticeFooterSection(
                nextNotice = noticeDetail?.nextNotice,
                prevNotice = noticeDetail?.previousNotice,
                callOtherNotice = callOtherNotice
            )
        }
    }
}

@Composable
private fun EmptyResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "공지사항을 찾을 수 없습니다.",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
    }
}

@Composable
private fun NoticeFooterSection(
    nextNotice: AdjacentNotice?,
    prevNotice: AdjacentNotice?,
    callOtherNotice: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacingXXXS),
        verticalArrangement = Arrangement.spacedBy(spacingXXXS)
    ) {
        CommonDivider()
        if (prevNotice != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXL),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = spacingXS, horizontal = spacingMedium)
            ) {
                Text(
                    text = "이전글",
                    style = typography.labelLarge,
                    color = colorScheme.text.caption
                )

                Text(
                    text = prevNotice.title,
                    style = typography.labelLarge,
                    color = colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable {
                        callOtherNotice(prevNotice.noticeId)
                    }
                )
            }
            CommonDivider()
        }
        if (nextNotice != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXL),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = spacingXS, horizontal = spacingMedium)
            ) {
                Text(
                    text = "다음글",
                    style = typography.labelLarge,
                    color = colorScheme.text.caption
                )

                Text(
                    text = nextNotice.title,
                    style = typography.labelLarge,
                    color = colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable {
                        callOtherNotice(nextNotice.noticeId)
                    }
                )
            }
            CommonDivider()
        }
    }
}

@Composable
private fun NoticeHeaderSection(notice: NoticeDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingXXS)) {
        NoticeStatusChip(notice.noticeStatus)
        Text(
            text = notice.title,
            style = typography.bodyMedium,
            color = colorScheme.text.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = notice.createdAt,
            style = typography.labelMedium,
            color = colorScheme.text.caption,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun MyPageNoticeDetailScreenPreview() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = MyPageNoticeDetailArgument(
                intent = { },
                dataState = MyPageNoticeDetailDataState.Init,
                screenState = MyPageNoticeDetailScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageNoticeDetailData.empty
        )
    }
}