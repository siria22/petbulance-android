package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.support.qna.QnaAnswer
import com.petbulance.domain.model.feature.support.qna.QnaStatus
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.ThickDivider
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun QnaDetailScreen(
    navController: NavController,
    argument: QnaDetailArgument,
    data: QnaDetailData
) {
    val dataState = argument.dataState
    val isOnProgress = dataState == QnaDetailDataState.OnProgress

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorToast by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is QnaDetailEvent.DataFetch.Error -> {
                    errorMessage = event.userMessage
                    showErrorToast = true
                    delay(2000)
                    navController.navigate(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                        popUpTo(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                            inclusive = true
                        }
                    }
                }

                is QnaDetailEvent.Delete.Success -> {
                    navController.navigate(
                        ScreenDestinations.MyPage.Help.CS.Qna.List.route + "?resultType=deleted"
                    ) {
                        popUpTo(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                            inclusive = true
                        }
                    }
                }

                is QnaDetailEvent.Delete.Error -> {
                    errorMessage = event.userMessage
                    showErrorToast = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.navigate(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                            popUpTo(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                                inclusive = true
                            }
                        }
                    },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(Pair(IconResource.Text {
                        Text(
                            "수정",
                            style = typography.bodyLarge.emp(),
                            color = colorScheme.text.secondary
                        )
                    }) {
                        navController.safeNavigate(
                            ScreenDestinations.MyPage.Help.CS.Qna.Create.createRoute(data.qnaId)
                        )
                    })
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            QnaDetailScreenContents(
                title = data.title,
                content = data.content,
                date = data.date,
                status = data.status,
                answer = data.answer,
                isLoading = data.isLoading,
                onDeleteClicked = {
                    showDeleteDialog = true
                },
                onReturnToListClicked = {
                    navController.navigate(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                        popUpTo(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }

    if (showDeleteDialog) {
        QnaDeleteConfirmDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                argument.intent(QnaDetailIntent.OnDeleteConfirmed)
            }
        )
    }

    if (showErrorToast) {
        LaunchedEffect(Unit) {
            delay(3000)
            showErrorToast = false
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingXL)
                    .background(
                        Color(0xFF222222).copy(alpha = 0.9f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(spacingSmall),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = errorMessage,
                    style = typography.bodySmall,
                    color = colorScheme.text.inverse
                )
            }
        }
    }
}

@Composable
private fun QnaDetailScreenContents(
    title: String,
    content: String,
    date: String,
    status: QnaStatus,
    answer: QnaAnswer?,
    isLoading: Boolean,
    onDeleteClicked: () -> Unit,
    onReturnToListClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingMedium, vertical = spacingXL),
        verticalArrangement = Arrangement.spacedBy(spacingLarge)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            QnaDetailStatusChip(status = status)
            Text(
                text = title,
                style = typography.titleLarge,
                color = colorScheme.text.primary
            )
            Text(
                text = date,
                style = typography.bodySmall,
                color = colorScheme.text.caption
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorScheme.border.subtle
        )

        Text(
            text = content,
            style = typography.bodyMedium,
            color = colorScheme.text.secondary,
            modifier = Modifier.fillMaxWidth()
        )

        if (answer != null) {
            ThickDivider()
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXXS)
            ) {
                Text(
                    text = "펫블런스 운영팀 답변",
                    style = typography.titleMedium,
                    color = colorScheme.action.primary.default
                )
                Text(
                    text = answer.answeredAt,
                    style = typography.bodySmall,
                    color = colorScheme.text.caption
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = colorScheme.border.subtle
            )

            Text(
                text = answer.content,
                style = typography.bodyMedium,
                color = colorScheme.text.secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacingMedium)
        ) {
            BasicButton(
                modifier = Modifier.weight(1f),
                text = "목록",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.DEFAULT,
                radius = 12.dp,
                onClicked = onReturnToListClicked
            )
            BasicButton(
                modifier = Modifier.weight(1f),
                text = "삭제",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp,
                onClicked = onDeleteClicked
            )
        }
    }
}

@Composable
private fun QnaDeleteConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    com.petbulance.presentation.component.ui.atom.BasicDialog(
        backHandler = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingLarge)
        ) {
            Text(
                text = "문의를 삭제하시겠습니까?",
                style = typography.titleMedium,
                color = colorScheme.text.primary
            )
            Text(
                text = "삭제된 문의는 복구할 수 없습니다.",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacingMedium)
            ) {
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "취소",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp,
                    onClicked = onDismiss
                )
                BasicButton(
                    modifier = Modifier.weight(1f),
                    text = "삭제",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.WARNING,
                    radius = 28.dp,
                    onClicked = onConfirm
                )
            }
        }
    }
}

@Composable
private fun QnaDetailStatusChip(status: QnaStatus) {
    val (text, textColor) = when (status) {
        QnaStatus.ANSWER_WAITING -> Pair(
            "답변대기",
            colorScheme.text.caption
        )

        QnaStatus.ANSWER_COMPLETED -> Pair(
            "답변완료",
            colorScheme.tag.trust.medium
        )
    }

    Box(
        modifier = Modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = typography.labelMedium,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun QnaDetailScreenPreview() {
    PetbulanceTheme {
        QnaDetailScreen(
            navController = rememberNavController(),
            argument = QnaDetailArgument(
                intent = { },
                dataState = QnaDetailDataState.Init,
                screenState = QnaDetailScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = QnaDetailData.stub()
        )
    }
}