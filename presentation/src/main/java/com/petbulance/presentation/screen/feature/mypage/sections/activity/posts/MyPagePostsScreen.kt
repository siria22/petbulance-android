package com.petbulance.presentation.screen.feature.mypage.sections.activity.posts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.community.post.MyPostSummary
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicCheckBox
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
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
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.screen.feature.mypage.sections.activity.common.ActivityDeleteOptionDialog
import com.petbulance.presentation.screen.feature.mypage.sections.activity.common.ActivitySelectionControlBar
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPagePostsScreen(
    navController: NavController,
    argument: MyPagePostsArgument
) {
    val dataState = argument.dataState
    val screenState = argument.screenState

    val normalScreenState = (screenState as? MyPagePostsScreenState.Normal)
        ?: MyPagePostsScreenState.Normal()

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessToast by remember { mutableStateOf(false) }
    var showDeleteRevertedToast by remember { mutableStateOf(false) }
    var pendingDeleteIds by remember { mutableStateOf<Set<Long>?>(null) }

    BackHandler {
        if (normalScreenState.isSelectionMode) {
            argument.intent(MyPagePostsIntent.ToggleSelectionMode(false))
        } else {
            navController.safePopBackStack()
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is MyPagePostsEvent.Post.DeleteSuccess -> {
                    argument.intent(MyPagePostsIntent.ToggleSelectionMode(false))
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
                argument.intent(MyPagePostsIntent.DeleteSelected)
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
                    text = "게시글 관리",
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
                is MyPagePostsDataState.Loading -> {
                    OnContentLoadingUi(text = "잠시만 기다려주세요...")
                }

                is MyPagePostsDataState.Loaded -> {
                    if (dataState.posts.isEmpty()) {
                        MyPostsEmptyView(
                            onMoveToCommunityButtonClicked = {
                                navController.safeNavigate(ScreenDestinations.Community.route)
                            }
                        )
                    } else {
                        MyPagePostsListView(
                            posts = dataState.posts,
                            hasNext = dataState.hasNext,
                            screenState = normalScreenState,
                            onLoadMore = { argument.intent(MyPagePostsIntent.LoadMore) },
                            onPostClick = { id ->
                                if (normalScreenState.isSelectionMode) {
                                    argument.intent(MyPagePostsIntent.TogglePostSelection(id))
                                } else {
                                    navController.safeNavigate(
                                        ScreenDestinations.Community.PostDetail.createRoute(id)
                                    )
                                }
                            },
                            onSelectAllClick = { argument.intent(MyPagePostsIntent.SelectAll) },
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
                            text = "게시글을 삭제했어요",
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
                            text = "게시글 삭제를 취소했어요",
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
            content = "게시글을 삭제하면 되돌릴 수 없어요.\n그래도 삭제하시겠어요?",
            cancelText = "취소",
            confirmText = "삭제",
            onDismissRequest = { showDeleteDialog = false },
            onExitButtonClicked = {
                showDeleteDialog = false
                pendingDeleteIds = normalScreenState.selectedIds
            }
        )
    }

    if (showMenu) {
        ActivityDeleteOptionDialog(
            title = "커뮤니티 게시글 삭제",
            onDeleteOptionClicked = {
                showMenu = false
                argument.intent(MyPagePostsIntent.ToggleSelectionMode(true))
            },
            onDismissRequest = { showMenu = false }
        )
    }
}

@Composable
private fun MyPagePostsListView(
    posts: List<MyPostSummary>,
    hasNext: Boolean,
    screenState: MyPagePostsScreenState.Normal,
    onLoadMore: () -> Unit,
    onPostClick: (Long) -> Unit,
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
                Column {
                    ActivitySelectionControlBar(
                        isAllSelected = screenState.selectedIds.size == posts.size && posts.isNotEmpty(),
                        hasSelection = screenState.selectedIds.isNotEmpty(),
                        onSelectAllClick = onSelectAllClick,
                        onDeleteClick = onDeleteClick
                    )
                    CommonDivider()
                }
            }
        }

        items(posts, key = { it.postId }) { post ->
            MyPagePostItem(
                post = post,
                isSelectionMode = screenState.isSelectionMode,
                isSelected = screenState.selectedIds.contains(post.postId),
                onClick = { onPostClick(post.postId) }
            )
            CommonDivider()
        }
    }
}


@Composable
private fun MyPagePostItem(
    post: MyPostSummary,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.bg.frame.default)
            .padding(vertical = spacingMedium, horizontal = spacingXL)
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
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
                if (post.hidden) {
                    Text(
                        text = "숨김",
                        style = typography.labelSmall,
                        color = colorScheme.text.caption,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = colorScheme.border.subtle,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = post.title,
                    style = typography.bodyMedium.emp(),
                    color = colorScheme.text.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingXXXS)
            ) {
                Text(
                    text = post.createdAt,
                    style = typography.labelSmall,
                    color = colorScheme.text.caption
                )

                Dot(dotColor = colorScheme.icon.veryLight)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.viewCount.toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }

                Dot(dotColor = colorScheme.icon.veryLight)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = (post.likeCount ?: 0L).toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (post.thumbnailUrl != null) {
                BasicImageBox(
                    size = 90.dp,
                    uri = post.thumbnailUrl?.toUri(),
                    errorImageResource = R.drawable.img_checker,
                    placeholderImageResource = R.drawable.img_checker,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                )
            }
            Text(
                text = post.content,
                style = typography.bodyMedium,
                color = colorScheme.text.secondary,
                maxLines = 2
            )
        }
    }
}

@Composable
fun MyPostsEmptyView(
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
            text = "작성한 글이 없어요",
            style = typography.titleSmall,
            color = colorScheme.text.tertiary
        )
        Text(
            text = "첫 게시글을 작성하고\n펫뷸런스 커뮤니티에 참여해보세요!",
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
private fun MyPagePostsScreenPreview() {
    val mockPosts = listOf(
        MyPostSummary(
            postId = 1L,
            title = "첫 번째 게시글 제목",
            content = "첫 번째 게시글 내용입니다. 아주 긴 내용을 적어서 두 줄이 넘어가는지 확인해봅시다. 이 게시글은 삭제되지 않았습니다.",
            createdAt = "2024.01.01",
            viewCount = 10,
            likeCount = 5,
            thumbnailUrl = null,
            hidden = false
        ),
        MyPostSummary(
            postId = 2L,
            title = "두 번째 게시글 제목 (숨김)",
            content = "두 번째 게시글 내용입니다. 이 게시글은 숨김 처리된 게시글입니다.",
            createdAt = "2024.01.02",
            viewCount = 5,
            likeCount = 2,
            thumbnailUrl = null,
            hidden = true
        )
    )

    PetbulanceTheme {
        MyPagePostsScreen(
            navController = rememberNavController(),
            argument = MyPagePostsArgument(
                intent = {},
                dataState = MyPagePostsDataState.Loaded(
                    posts = mockPosts,
                    hasNext = false
                ),
                screenState = MyPagePostsScreenState.Normal(
                    isSelectionMode = false,
                    selectedIds = emptySet()
                ),
                event = MutableSharedFlow()
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPagePostsEmptyScreenPreview() {
    PetbulanceTheme {
        MyPagePostsScreen(
            navController = rememberNavController(),
            argument = MyPagePostsArgument(
                intent = {},
                dataState = MyPagePostsDataState.Loaded(
                    posts = emptyList(),
                    hasNext = false
                ),
                screenState = MyPagePostsScreenState.Normal(
                    isSelectionMode = false,
                    selectedIds = emptySet()
                ),
                event = MutableSharedFlow()
            ),
        )
    }
}
