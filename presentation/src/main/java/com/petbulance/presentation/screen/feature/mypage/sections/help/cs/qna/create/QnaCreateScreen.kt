package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.create.composables.QnaCreateSuccessDialog
import com.petbulance.presentation.screen.feature.review.common.ReviewInputTextField
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun QnaCreateScreen(
    navController: NavController,
    argument: QnaCreateArgument,
    data: QnaCreateData
) {
    val dataState = argument.dataState
    val isOnProgress = dataState == QnaCreateDataState.OnProgress

    var showExitDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var submittedQnaId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is QnaCreateEvent.SubmitSuccess -> {
                    val resultType = if (data.mode == QnaCreateMode.EDIT) "updated" else "created"
                    navController.navigate(
                        ScreenDestinations.MyPage.Help.CS.Qna.List.route +
                                "?resultType=$resultType&qnaId=${event.qnaId}"
                    ) {
                        popUpTo(ScreenDestinations.MyPage.Help.CS.Qna.List.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled = !isOnProgress) {
        if (data.mode == QnaCreateMode.EDIT) {
            // 수정 모드: 변경사항 있으면 경고
            showExitDialog = true
        } else {
            // 생성 모드: 작성 중인 내용 있으면 경고
            if (data.title.isNotBlank() || data.content.isNotBlank()) {
                showExitDialog = true
            } else {
                navController.safePopBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            val isEditMode = data.mode == QnaCreateMode.EDIT
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = if (isEditMode) "문의 수정" else "문의 작성",
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        if (isEditMode) {
                            showExitDialog = true
                        } else if (data.title.isNotBlank() || data.content.isNotBlank()) {
                            showExitDialog = true
                        } else {
                            navController.safePopBackStack()
                        }
                    },
                    leadingIconResource = if (isEditMode) {
                        IconResource.Vector(Icons.Default.Close)
                    } else {
                        IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft)
                    },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(Pair(IconResource.Text {
                        Text(
                            if (isEditMode) "완료" else "등록",
                            style = typography.bodyLarge.emp(),
                            color = if (data.isSubmitEnabled && !isOnProgress) {
                                colorScheme.text.secondary
                            } else {
                                colorScheme.text.disabled
                            }
                        )
                    }) {
                        if (data.isSubmitEnabled && !isOnProgress) {
                            argument.intent(QnaCreateIntent.OnSubmitClicked)
                        }
                    })
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
        Box(modifier = Modifier.padding(innerPadding)) {
            QnaCreateScreenContents(
                title = data.title,
                content = data.content,
                onTitleChanged = { argument.intent(QnaCreateIntent.OnTitleChanged(it)) },
                onContentChanged = { argument.intent(QnaCreateIntent.OnContentChanged(it)) }
            )
        }
    }

    if (showExitDialog) {
        val message = if (data.mode == QnaCreateMode.EDIT) {
            "수정 중인 내용이 있습니다. 정말 나가시겠습니까?"
        } else {
            "작성 중인 내용이 있습니다. 정말 나가시겠습니까?"
        }

        WarningDialog(
            title = "변경 사항이 있습니다.",
            content = message,
            cancelText = "취소",
            confirmText = "삭제",
            onDismissRequest = { showExitDialog = false },
            onExitButtonClicked = {
                showExitDialog = false
                navController.safePopBackStack()
            }
        )
    }

    if (showSuccessDialog) {
        QnaCreateSuccessDialog(
            isEditMode = data.mode == QnaCreateMode.EDIT,
            onDismiss = {
                showSuccessDialog = false
            },
            onNavigateToDetail = {
                showSuccessDialog = false
                submittedQnaId?.let { qnaId ->
                    navController.safeNavigate(
                        ScreenDestinations.MyPage.Help.CS.Qna.Detail.createRoute(qnaId)
                    )
                }
            }
        )
    }
}

@Composable
private fun QnaCreateScreenContents(
    title: String,
    content: String,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacingMedium, vertical = spacingXL),
        verticalArrangement = Arrangement.spacedBy(spacingXL)
    ) {
        // 제목 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "제목",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                modifier = Modifier.fillMaxWidth(),
                queryString = title,
                placeholder = "제목을 입력하세요.",
                onQueryStringChanged = onTitleChanged,
                singleLine = true
            )
        }

        // 내용 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "내용",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                queryString = content,
                placeholder = "문의 내용을 입력하세요.",
                onQueryStringChanged = onContentChanged,
                singleLine = false
            )
        }
    }
}

@Preview
@Composable
private fun QnaCreateScreenPreview() {
    PetbulanceTheme {
        QnaCreateScreen(
            navController = rememberNavController(),
            argument = QnaCreateArgument(
                intent = { },
                dataState = QnaCreateDataState.Init,
                screenState = QnaCreateScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = QnaCreateData.empty()
        )
    }
}