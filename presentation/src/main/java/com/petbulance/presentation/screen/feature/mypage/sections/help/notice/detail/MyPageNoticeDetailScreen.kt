package com.petbulance.presentation.screen.feature.mypage.sections.help.notice.detail

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.petbulance.domain.model.feature.support.notice.AdjacentNotice
import com.petbulance.domain.model.feature.support.notice.Attachment
import com.petbulance.domain.model.feature.support.notice.NoticeButton
import com.petbulance.domain.model.feature.support.notice.NoticeButtonTarget
import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.model.type.NoticeStatusType
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
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
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageNoticeDetailScreen(
    navController: NavController,
    argument: MyPageNoticeDetailArgument,
    data: MyPageNoticeDetailData
) {
    val dataState = argument.dataState

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
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MyPageNoticeDetailScreenContents(
                navController = navController,
                dataState = dataState,
                noticeDetail = data.noticeDetail,
                callOtherNotice = { noticeId ->
                    navController.navigate(
                        ScreenDestinations.MyPage.Help.Notice.Detail.createRoute(noticeId)
                    )
                }
            )
        }
    }
}

@Composable
private fun MyPageNoticeDetailScreenContents(
    navController: NavController,
    dataState: MyPageNoticeDetailDataState,
    noticeDetail: NoticeDetail?,
    callOtherNotice: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        when {
            dataState is MyPageNoticeDetailDataState.NotFound -> {
                NotFoundResult(modifier = Modifier.weight(1f))
            }

            noticeDetail == null -> {
                EmptyResult(modifier = Modifier.weight(1f))
            }

            noticeDetail.attachments.isNotEmpty() -> {
                NoticeCTAContent(
                    modifier = Modifier.weight(1f),
                    notice = noticeDetail,
                    navController = navController
                )
            }

            else -> {
                NoticeTextContent(
                    modifier = Modifier.weight(1f),
                    notice = noticeDetail,
                    navController = navController
                )
            }
        }

        NoticeFooterSection(
            nextNotice = noticeDetail?.nextNotice,
            prevNotice = noticeDetail?.previousNotice,
            callOtherNotice = callOtherNotice
        )
    }
}

@Composable
private fun NoticeCTAContent(
    modifier: Modifier = Modifier,
    notice: NoticeDetail,
    navController: NavController
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacingXL)
    ) {
        Column(
            modifier = Modifier.padding(spacingMedium),
            verticalArrangement = Arrangement.spacedBy(spacingXL)
        ) {
            NoticeHeaderSection(notice = notice)
            CommonDivider()
        }

        NoticeAttachmentSection(attachments = notice.attachments)

        if (!notice.buttons.isNullOrEmpty()) {
            NoticeButtonSection(
                navController = navController,
                modifier = Modifier.padding(spacingMedium),
                buttons = notice.buttons!!
            )
        }
    }
}

@Composable
private fun NoticeTextContent(
    modifier: Modifier = Modifier,
    notice: NoticeDetail,
    navController: NavController
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(spacingMedium),
        verticalArrangement = Arrangement.spacedBy(spacingXL)
    ) {
        NoticeHeaderSection(notice = notice)

        Text(
            text = notice.content,
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )

        if (!notice.buttons.isNullOrEmpty()) {
            NoticeButtonSection(
                modifier = Modifier.padding(spacingMedium),
                buttons = notice.buttons!!,
                navController = navController
            )
        }
    }
}

@Composable
private fun NoticeAttachmentSection(attachments: List<Attachment>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        attachments.forEach { attachment ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(attachment.fileUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = attachment.fileName,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
                loading = { CustomGreenLoader() }
            )
        }
    }
}

@Composable
private fun NoticeButtonSection(
    modifier: Modifier = Modifier,
    buttons: List<NoticeButton>,
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        buttons.forEach { button ->
            BasicButton(
                text = button.text,
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (button.target) {
                    NoticeButtonTarget.INNER -> {
                        val route = NoticeButtonRouteMapper.resolve(button.link)
                        if (route != null) navController.safeNavigate(route)
                    }
                    // TODO : 외부 링크 안되는 이유??
                    NoticeButtonTarget.EXTERNAL -> {
                        if (button.link.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, button.link.toUri()).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyResult(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "공지사항을 찾을 수 없습니다.",
            style = typography.bodyMedium,
            color = colorScheme.text.secondary
        )
    }
}

@Composable
private fun NotFoundResult(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "존재하지 않는 공지사항입니다.",
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
    if (prevNotice == null && nextNotice == null) return

    Column(
        modifier = Modifier.fillMaxWidth(),
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
                    color = colorScheme.text.caption,
                    modifier = Modifier.width(48.dp)
                )
                Text(
                    text = prevNotice.title,
                    style = typography.labelLarge,
                    color = colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { callOtherNotice(prevNotice.noticeId) }
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
                    color = colorScheme.text.caption,
                    modifier = Modifier.width(48.dp)
                )
                Text(
                    text = nextNotice.title,
                    style = typography.labelLarge,
                    color = colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { callOtherNotice(nextNotice.noticeId) }
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
            style = typography.titleMedium,
            color = colorScheme.text.primary
        )
        Text(
            text = notice.createdAt,
            style = typography.labelMedium,
            color = colorScheme.text.caption
        )
    }
}

private val previewArgument = MyPageNoticeDetailArgument(
    intent = { },
    dataState = MyPageNoticeDetailDataState.Init,
    screenState = MyPageNoticeDetailScreenState.Init,
    event = MutableSharedFlow()
)

private val prevNoticeStub = AdjacentNotice(noticeId = 1L, title = "앱 업데이트 안내 (v1.1.0)")
private val nextNoticeStub = AdjacentNotice(noticeId = 3L, title = "개인정보 처리방침 개정 안내")

private val baseNoticeStub = NoticeDetail(
    noticeId = 2L,
    noticeStatus = NoticeStatusType.NOTICE,
    title = "제목",
    createdAt = "2025-11-30",
    content = "내용",
    previousNotice = prevNoticeStub,
    nextNotice = nextNoticeStub
)

@Preview(name = "1. 텍스트 공지 (이전글+다음글)")
@Composable
private fun PreviewTextNotice() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument,
            data = MyPageNoticeDetailData(noticeDetail = baseNoticeStub)
        )
    }
}

@Preview(name = "2. 텍스트 공지 (이전글만)")
@Composable
private fun PreviewPrevOnly() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument,
            data = MyPageNoticeDetailData(
                noticeDetail = baseNoticeStub.copy(nextNotice = null)
            )
        )
    }
}

@Preview(name = "3. 텍스트 공지 (다음글만)")
@Composable
private fun PreviewNextOnly() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument,
            data = MyPageNoticeDetailData(
                noticeDetail = baseNoticeStub.copy(previousNotice = null)
            )
        )
    }
}

@Preview(name = "4. 텍스트 공지 (긴 본문)")
@Composable
private fun PreviewLongContent() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument,
            data = MyPageNoticeDetailData(
                noticeDetail = baseNoticeStub.copy(
                    title = "펫불런스 오픈 기념 리뷰 작성 이벤트",
                    content = "펫불런서 여러분 안녕하세요!\n\n펫불런스가 런칭되어 정말 기쁩니다. 이를 기념하기 위해 특별한 리뷰 작성 이벤트를 준비했습니다! 여러분의 소중한 의견을 듣고 싶습니다. 펫불런스를 이용해 주신 경험에 대해 솔직한 리뷰를 남겨주시면, 추첨을 통해 다양한 상품을 드리는 기회를 드립니다.\n\n이벤트 참여 방법은 저희 앱을 이용한 후, 웹사이트나 소셜 미디어에 캡처와 함께 리뷰를 남겨주시면 됩니다. 리뷰는 길이 제한 없이 자유롭게 작성하실 수 있으며, 여러분의 경험과 느낌을 적어주시면 됩니다.\n\n감사합니다"
                )
            )
        )
    }
}

@Preview(name = "5. 이벤트 타입")
@Composable
private fun PreviewEventType() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument,
            data = MyPageNoticeDetailData(
                noticeDetail = baseNoticeStub.copy(
                    noticeStatus = NoticeStatusType.EVENT,
                    title = "펫불런스 오픈 기념 리뷰 작성 이벤트"
                )
            )
        )
    }
}

@Preview(name = "6. CTA 공지 (이미지+버튼)")
@Composable
private fun PreviewCTANotice() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument,
            data = MyPageNoticeDetailData(
                noticeDetail = baseNoticeStub.copy(
                    title = "펫불런스 정상영업합니다.",
                    attachments = listOf(
                        Attachment(
                            fileId = 16L,
                            fileName = "bande.png",
                            fileUrl = "https://petbulance-s3-bucket.s3.ap-northeast-2.amazonaws.com/notice_file/05402716-bd8b-45aa-9d48-fd785ee75c33_bande.png"
                        )
                    ),
                    buttons = listOf(
                        NoticeButton(
                            buttonId = 1L,
                            text = "자세히 보기",
                            link = "https://petbulance.cloud",
                            target = NoticeButtonTarget.EXTERNAL
                        )
                    )
                )
            )
        )
    }
}

@Preview(name = "7. NotFound")
@Composable
private fun PreviewNotFound() {
    PetbulanceTheme {
        MyPageNoticeDetailScreen(
            navController = rememberNavController(),
            argument = previewArgument.copy(
                dataState = MyPageNoticeDetailDataState.NotFound
            ),
            data = MyPageNoticeDetailData.empty
        )
    }
}