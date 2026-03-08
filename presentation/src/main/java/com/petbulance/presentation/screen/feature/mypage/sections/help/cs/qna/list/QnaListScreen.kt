package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.list

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaStatus
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicFabIcon
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun QnaListScreen(
    navController: NavController,
    argument: QnaListArgument,
    data: QnaListData
) {
    val dataState = argument.dataState
    val isLoading = dataState is QnaListDataState.Loading

    LaunchedEffect(data.successMessage) {
        if (data.successMessage != null) {
            delay(3000)
            argument.intent(QnaListIntent.OnRefresh)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "문의 작성",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.safePopBackStack()
                    },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default,
        floatingActionButton = {
            BasicFabIcon(
                iconResource = IconResource.Vector(Icons.Default.Edit),
                onClick = {
                    navController.safeNavigate(
                        ScreenDestinations.MyPage.Help.CS.Qna.Create.createRoute()
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                isLoading && data.qnaList.isEmpty() -> {
                    OnContentLoadingUi(text = "잠시만 기다려주세요...")
                }

                data.qnaList.isEmpty() -> {
                    QnaListEmptyState()
                }

                else -> {
                    QnaListContent(
                        qnaList = data.qnaList,
                        hasNext = (dataState as? QnaListDataState.Loaded)?.hasNext ?: false,
                        isLoadingMore = isLoading,
                        onLoadMore = { argument.intent(QnaListIntent.OnLoadMore) },
                        onQnaItemClicked = { qnaId ->
                            navController.safeNavigate(
                                ScreenDestinations.MyPage.Help.CS.Qna.Detail.createRoute(qnaId)
                            )
                        }
                    )
                }
            }

            data.successMessage?.let { messageType ->
                SuccessToast(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = spacingXL),
                    messageType = messageType,
                    onActionClicked = { qnaId ->
                        navController.safeNavigate(
                            ScreenDestinations.MyPage.Help.CS.Qna.Detail.createRoute(qnaId)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun QnaListContent(
    qnaList: List<Qna>,
    hasNext: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onQnaItemClicked: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && hasNext && !isLoadingMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(qnaList, key = { it.id }) { qna ->
            QnaListItem(
                qna = qna,
                onClick = { onQnaItemClicked(qna.id) }
            )
            CommonDivider()
        }

        if (isLoadingMore && hasNext) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacingMedium),
                    contentAlignment = Alignment.Center
                ) {
                    CustomGreenLoader()
                }
            }
        }
    }
}

@Composable
private fun QnaListItem(
    qna: Qna,
    onClick: () -> Unit
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
            .clickable { onClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QnaStatusChip(status = qna.status)
                Text(
                    text = qna.title,
                    style = typography.bodyMedium,
                    color = colorScheme.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = qna.date,
                style = typography.labelMedium,
                color = colorScheme.text.caption
            )
        }
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = "Navigate to qna detail",
            size = iconSizeMedium,
            tint = colorScheme.icon.veryLight
        )
    }
}

@Composable
private fun QnaStatusChip(status: QnaStatus) {
    val (text, textColor) = when (status) {
        QnaStatus.ANSWER_WAITING -> Pair("답변대기", colorScheme.text.caption)
        QnaStatus.ANSWER_COMPLETED -> Pair("답변완료", colorScheme.tag.trust.medium)
    }

    Text(
        text = text,
        style = typography.labelMedium,
        color = textColor
    )
}

@Composable
private fun QnaListEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "작성한 문의가 없습니다",
            style = typography.bodyLarge,
            color = colorScheme.text.secondary
        )
    }
}

@Composable
private fun SuccessToast(
    modifier: Modifier = Modifier,
    messageType: SuccessMessageType,
    onActionClicked: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacingMedium)
            .background(
                Color(0xFF222222).copy(alpha = 0.9f),
                RoundedCornerShape(4.dp)
            )
            .padding(spacingSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = messageType.message,
            style = typography.bodySmall,
            color = colorScheme.text.inverse
        )
        if (messageType.hasAction) {
            val qnaId = when (messageType) {
                is SuccessMessageType.Created -> messageType.qnaId
                is SuccessMessageType.Updated -> messageType.qnaId
                else -> null
            }
            qnaId?.let {
                Text(
                    text = "보기",
                    style = typography.bodySmall,
                    color = colorScheme.text.inverse,
                    modifier = Modifier.clickable { onActionClicked(it) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun QnaListScreenPreview() {
    PetbulanceTheme {
        QnaListScreen(
            navController = rememberNavController(),
            argument = QnaListArgument(
                intent = { },
                dataState = QnaListDataState.Loaded(hasNext = false),
                screenState = QnaListScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = QnaListData.stub()
        )
    }
}

@Preview
@Composable
private fun QnaListEmptyStatePreview() {
    PetbulanceTheme {
        QnaListEmptyState()
    }
}

@Preview
@Composable
private fun SuccessToastPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(colorScheme.bg.frame.default)
                .padding(16.dp)
        ) {
            SuccessToast(
                messageType = SuccessMessageType.Created(1L),
                onActionClicked = {}
            )
            SuccessToast(
                messageType = SuccessMessageType.Updated(1L),
                onActionClicked = {}
            )
            SuccessToast(
                messageType = SuccessMessageType.Deleted,
                onActionClicked = {}
            )
        }
    }
}
