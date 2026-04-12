package com.petbulance.presentation.screen.feature.mypage.sections.activity.comments

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.community.comment.MyCommentListRes
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicCheckBox
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.molecule.WarningDialog
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
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.mypage.sections.activity.common.ActivityDeleteOptionDialog
import com.petbulance.presentation.screen.feature.mypage.sections.activity.common.ActivitySelectionControlBar
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageCommentsScreen(
    navController: NavController,
    argument: MyPageCommentsArgument,
    data: MyPageCommentsData
) {
    val dataState = argument.dataState
    val screenState = argument.screenState

    val normalScreenState = (screenState as? MyPageCommentsScreenState.Normal)
        ?: MyPageCommentsScreenState.Normal()

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessToast by remember { mutableStateOf(false) }
    var showDeleteRevertedToast by remember { mutableStateOf(false) }
    var pendingDeleteIds by remember { mutableStateOf<Set<Long>?>(null) }

    BackHandler {
        if (normalScreenState.isSelectionMode) {
            argument.intent(MyPageCommentsIntent.ToggleSelectionMode(false))
        } else {
            navController.safePopBackStack()
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is MyPageCommentsEvent.Comment.DeleteSuccess -> {
                    argument.intent(MyPageCommentsIntent.ToggleSelectionMode(false))
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(pendingDeleteIds) {
        if (pendingDeleteIds != null) {
            showDeleteSuccessToast = true
            delay(3000)
            showDeleteSuccessToast = false
            if (pendingDeleteIds != null) {
                argument.intent(MyPageCommentsIntent.DeleteSelected)
                pendingDeleteIds = null
            }
        }
    }

    LaunchedEffect(showDeleteRevertedToast) {
        if (showDeleteRevertedToast) {
            delay(3000)
            showDeleteRevertedToast = false
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "댓글 관리",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(
                            IconResource.Vector(Icons.Default.MoreVert),
                            { showMenu = true }
                        )
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
        Box(modifier = Modifier.padding(innerPadding)) {
            when (dataState) {
                is MyPageCommentsDataState.Loading -> {
                    OnContentLoadingUi(text = "잠시만 기다려주세요...")
                }

                is MyPageCommentsDataState.Loaded -> {
                    if (dataState.comments.isEmpty()) {
                        MyCommentsEmptyView(
                            onMoveToCommunityButtonClicked = {
                                navController.safeNavigate(ScreenDestinations.Community.route)
                            }
                        )
                    } else {
                        MyPageCommentsListView(
                            comments = dataState.comments,
                            hasNext = dataState.hasNext,
                            screenState = normalScreenState,
                            onLoadMore = { argument.intent(MyPageCommentsIntent.LoadMore) },
                            onCommentClick = { commentId, postId ->
                                if (normalScreenState.isSelectionMode) {
                                    argument.intent(
                                        MyPageCommentsIntent.ToggleCommentSelection(
                                            commentId
                                        )
                                    )
                                } else {
                                    navController.safeNavigate(
                                        ScreenDestinations.Community.PostDetail.createRoute(postId)
                                    )
                                }
                            },
                            onSelectAllClick = { argument.intent(MyPageCommentsIntent.SelectAll) },
                            onDeleteClick = { showDeleteDialog = true }
                        )
                    }
                }

                else -> {}
            }

            if (showDeleteSuccessToast) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF222222).copy(alpha = 0.9f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(spacingSmall),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "댓글을 삭제했어요",
                            style = typography.bodySmall,
                            color = colorScheme.text.inverse
                        )
                        Text(
                            text = "취소",
                            style = typography.bodySmall,
                            color = colorScheme.text.inverse,
                            modifier = Modifier.clickable {
                                pendingDeleteIds = null
                                showDeleteSuccessToast = false
                                showDeleteRevertedToast = true
                            }
                        )
                    }
                }
            }

            if (showDeleteRevertedToast) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF222222).copy(alpha = 0.9f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(spacingSmall),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "댓글 삭제를 취소했어요",
                            style = typography.bodySmall,
                            color = colorScheme.text.inverse
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        WarningDialog(
            title = "주의",
            content = "댓글을 삭제하면 되돌릴 수 없어요.\n그래도 삭제하시겠어요?",
            cancelText = "취소",
            confirmText = "전체 삭제",
            onDismissRequest = { showDeleteDialog = false },
            onExitButtonClicked = {
                showDeleteDialog = false
                pendingDeleteIds = normalScreenState.selectedIds
            }
        )
    }

    if (showMenu) {
        ActivityDeleteOptionDialog(
            title = "댓글 삭제",
            onDeleteOptionClicked = {
                showMenu = false
                argument.intent(MyPageCommentsIntent.ToggleSelectionMode(true))
            },
            onDismissRequest = { showMenu = false }
        )
    }
}

@Composable
private fun MyPageCommentsListView(
    comments: List<MyCommentListRes>,
    hasNext: Boolean,
    screenState: MyPageCommentsScreenState.Normal,
    onLoadMore: () -> Unit,
    onCommentClick: (Long, Long) -> Unit,
    onSelectAllClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem =
                listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && hasNext) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        if (screenState.isSelectionMode) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ActivitySelectionControlBar(
                        isAllSelected = screenState.selectedIds.size == comments.size && comments.isNotEmpty(),
                        hasSelection = screenState.selectedIds.isNotEmpty(),
                        onSelectAllClick = onSelectAllClick,
                        onDeleteClick = onDeleteClick
                    )

                    CommonDivider()
                }
            }
        }

        items(comments, key = { it.commentId }) { comment ->
            MyPageCommentItem(
                comment = comment,
                isSelectionMode = screenState.isSelectionMode,
                isSelected = screenState.selectedIds.contains(comment.commentId),
                onClick = { onCommentClick(comment.commentId, comment.postId) }
            )
            CommonDivider()
        }
    }
}

@Composable
private fun MyPageCommentItem(
    comment: MyCommentListRes,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val background =
        if (comment.hidden) colorScheme.bg.frame.subtle else colorScheme.bg.frame.default

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(vertical = spacingMedium, horizontal = spacingXL)
            .clickable { onClick() },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelectionMode) {
                BasicCheckBox(
                    checkState = isSelected,
                    onCheckedChange = onClick
                )
            }
            if (comment.hidden) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Filled.Lock),
                    contentDescription = "Secret",
                    size = iconSizeSmall,
                    tint = colorScheme.icon.dark
                )
            }

            Text(
                text = comment.postTitle,
                style = typography.bodyMedium.emp(),
                color = colorScheme.text.primary,
                maxLines = 1
            )
        }

        Text(
            text = comment.createdAt,
            style = typography.labelSmall.emp(),
            color = colorScheme.text.caption,
            maxLines = 1
        )

        Text(
            text = comment.commentContent,
            style = typography.bodyMedium,
            color = colorScheme.text.secondary,
            maxLines = 2
        )
    }
}

@Composable
fun MyCommentsEmptyView(
    onMoveToCommunityButtonClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = spacingXL)
    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.img_empty_state_1),
            contentDescription = "No result",
            size = 160.dp,
            tint = Color.Unspecified
        )
        Text(
            text = "작성한 댓글이 없어요.",
            style = typography.titleSmall,
            color = colorScheme.text.tertiary
        )
        Text(
            text = "첫 댓글을 작성하고\n펫뷸런스 커뮤니티에 참여해보세요!",
            textAlign = TextAlign.Center,
            style = typography.bodySmall,
            color = colorScheme.text.tertiary
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXS),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingLarge)
        ) {
            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                text = "커뮤니티 구경하기",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp,
                onClicked = onMoveToCommunityButtonClicked
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageCommentsScreenPreview() {
    val mockComments = listOf(
        MyCommentListRes(
            commentId = 1L,
            boardId = 1L,
            postId = 100L,
            postTitle = "게시글 제목",
            commentContent = "너무 많이 궁어하는 것 같아요. 저는 상세정보에 써있는 정량만 먹 급여해요.",
            createdAt = "2024-12-07",
            hidden = false
        ),
        MyCommentListRes(
            commentId = 2L,
            boardId = 1L,
            postId = 101L,
            postTitle = "숨김 처리된 게시글",
            commentContent = "우리집 햄스터 병원 2살 된 경과 할배인데.. 최근에 예가 너무 노쇠해진건지 슬...",
            createdAt = "2024-12-06",
            hidden = true
        )
    )

    PetbulanceTheme {
        MyPageCommentsScreen(
            navController = rememberNavController(),
            argument = MyPageCommentsArgument(
                intent = {},
                dataState = MyPageCommentsDataState.Loaded(
                    comments = mockComments,
                    hasNext = false
                ),
                screenState = MyPageCommentsScreenState.Normal(
                    isSelectionMode = false,
                    selectedIds = emptySet()
                ),
                event = MutableSharedFlow()
            ),
            data = MyPageCommentsData.stub()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageCommentsEmptyScreenPreview() {
    PetbulanceTheme {
        MyPageCommentsScreen(
            navController = rememberNavController(),
            argument = MyPageCommentsArgument(
                intent = {},
                dataState = MyPageCommentsDataState.Loaded(
                    comments = emptyList(),
                    hasNext = false
                ),
                screenState = MyPageCommentsScreenState.Normal(
                    isSelectionMode = false,
                    selectedIds = emptySet()
                ),
                event = MutableSharedFlow()
            ),
            data = MyPageCommentsData.stub()
        )
    }
}
