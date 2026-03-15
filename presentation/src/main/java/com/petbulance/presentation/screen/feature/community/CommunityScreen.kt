package com.petbulance.presentation.screen.feature.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.community.post.NoticeBanner
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.PostCategory
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicFabIcon
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.screen.feature.community.components.CommunityTopBar
import com.petbulance.presentation.screen.feature.community.components.SortDropdown
import com.petbulance.presentation.screen.feature.community.components.TopicFilterChips
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun CommunityScreen(
    navController: NavController,
    argument: CommunityArgument,
    data: CommunityData,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        argument.event.collect { event ->
            when (event) {
                is CommunityEvent.ShowComingSoonMessage -> {
                    snackbarHostState.showSnackbar("${event.feature} 기능은 준비 중입니다")
                }

                is CommunityEvent.NavigateToPostDetail -> {
                    // TODO: [구현 필요] 게시글 상세 화면 이동
                }

                is CommunityEvent.NavigateToNotice -> {
                    // TODO: [구현 필요] 공지사항 상세 화면 이동
                }

                is CommunityEvent.DataFetch.Error -> {
                    // BaseViewModel에서 처리
                }
            }
        }
    }

    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && data.hasNext && argument.dataState != CommunityDataState.LoadingMore) {
            argument.intent(CommunityIntent.LoadMorePosts)
        }
    }

    Scaffold(
        topBar = {
            CommunityTopBar(
                selectedAnimalType = data.currentType?.let { type ->
                    AnimalCategory.entries.find { it.name == type }
                },
                onAnimalTypeSelected = { category ->
                    argument.intent(CommunityIntent.FilterByType(category?.name))
                },
                onSearchClick = {
                    argument.intent(CommunityIntent.NavigateToSearch)
                },
                onNotificationClick = {
                    argument.intent(CommunityIntent.NavigateToNotifications)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.COMMUNITY,
                navController = navController
            )
        },
        floatingActionButton = {
            BasicFabIcon(
                iconResource = IconResource.Vector(Icons.Default.Add),
                onClick = {
                    argument.intent(CommunityIntent.NavigateToCreatePost)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = colorScheme.bg.frame.default,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CommunityScreenContents(
                argument = argument,
                data = data,
                listState = listState
            )
        }
    }
}

@Composable
fun CommunityScreenContents(
    argument: CommunityArgument,
    data: CommunityData,
    listState: LazyListState,
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.bg.frame.default)
        ) {
            TopicFilterChips(
                selectedTopic = data.currentTopic?.let { topic ->
                    PostCategory.entries.find { it.name == topic }
                },
                onTopicSelected = { category ->
                    argument.intent(CommunityIntent.FilterByTopic(category?.name))
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = spacingXS),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortDropdown(
                    selectedSort = data.currentSort,
                    onSortSelected = { sort ->
                        argument.intent(CommunityIntent.ChangeSort(sort))
                    }
                )
            }
        }

        if (data.noticeBanner != null) {
                NoticeBannerItem(
                    banner = data.noticeBanner,
                    onClick = {
                        argument.intent(
                            CommunityIntent.NavigateToNotice(
                                data.noticeBanner.noticeId
                            )
                        )
                    }
                )
        }

        when {
            argument.dataState == CommunityDataState.Loading && data.posts.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            data.posts.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 게시글이 없어요",
                        style = typography.bodyLarge,
                        color = colorScheme.text.tertiary
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = data.posts,
                        key = { it.id }
                    ) { post ->
                        PostListItem(
                            post = post,
                            onPostClick = {
                                argument.intent(
                                    CommunityIntent.NavigateToPostDetail(
                                        post.id
                                    )
                                )
                            },
                            onLikeClick = { argument.intent(CommunityIntent.ToggleLike(post.id)) },
                        )
                        CommonDivider()
                    }

                    if (argument.dataState == CommunityDataState.LoadingMore) {
                        item(key = "loading_more") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoticeBannerItem(
    banner: NoticeBanner,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = colorScheme.status.info.bg
            )
            .padding(vertical = spacingXS, horizontal = spacingXL)

    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.ic_bullhorn),
            contentDescription = "Notice",
            size = iconSizeMedium,
            tint = colorScheme.status.info.default,
            modifier = Modifier.clickable { onClick() }
        )

        Text(
            text = banner.title,
            style = typography.labelLarge,
            color = colorScheme.text.tertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PostItemChip(
    text: String,
    type: String = "category"
) {
    val backgroundColor =
        if (type == "category") colorScheme.bg.frame.default else colorScheme.bg.frame.subtle
    val borderColor = colorScheme.border.tertiary
    val cornerShape = RoundedCornerShape(4.dp)

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = cornerShape
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = cornerShape
            )
            .padding(
                vertical = spacingXXXS,
                horizontal = spacingXS
            )
    ) {
        Text(
            text = text,
            style = typography.labelSmall,
            color = colorScheme.text.tertiary
        )
    }
}

@Composable
fun PostListItem(
    post: PostSummary,
    onPostClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXS),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingXL, vertical = spacingMedium)
            .clickable { onPostClick() },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacingXS)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS)
                ) {
                    PostItemChip(
                        text = post.type,
                        type = "category"
                    )
                    PostItemChip(
                        text = post.topic,
                        type = "topic"
                    )
                }

                Text(
                    text = post.title,
                    style = typography.bodySmall.emp(),
                    color = colorScheme.text.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = post.content,
                    style = typography.labelLarge,
                    color = colorScheme.text.secondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (post.thumbnailUrl != null) {
                Box(contentAlignment = Alignment.TopStart) {
                    BasicImageBox(
                        size = 90.dp,
                        uri = post.thumbnailUrl?.toUri(),
                        errorImageResource = R.drawable.img_checker,
                        placeholderImageResource = R.drawable.img_checker,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    )
                    if (post.imageCount > 1) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.imageCount.toString(),
                                color = Color.White,
                                style = typography.labelMedium
                            )
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "작성자", // TODO : 작성자
                    style = typography.labelSmall,
                    color = colorScheme.text.caption
                )

                Dot(dotColor = colorScheme.icon.veryLight)

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
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable(onClick = onLikeClick)
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.likeCount.toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = colorScheme.icon.veryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = post.commentCount.toString(),
                        style = typography.labelSmall,
                        color = colorScheme.text.caption
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityScreenPreview() {
    PetbulanceTheme {
        CommunityScreen(
            navController = rememberNavController(),
            argument = CommunityArgument(
                intent = {},
                dataState = CommunityDataState.Init,
                screenState = CommunityScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = CommunityData(
                noticeBanner = NoticeBanner(
                    noticeId = 1,
                    noticeStatus = "ACTIVE",
                    title = "(공지사항) 12/16 02:00~08:00 서비스 점검으로 인한 앱 사용 중단",
                    content = "안녕하세요. 펫뷸런스입니다."
                ),
                posts = listOf(
                    PostSummary(
                        id = 1,
                        type = "SMALLMAMMALS",
                        topic = "HEALTH",
                        title = "게코 도마뱀 키우시는 분 있나요?",
                        content = "케이지 추천해주세요!",
                        thumbnailUrl = null,
                        imageCount = 5,
                        viewCount = 132,
                        commentCount = 8,
                        likeCount = 12,
                        createdAt = "19시간 전",
                        isLiked = false
                    ),
                    PostSummary(
                        id = 2,
                        type = "AVIAN",
                        topic = "DAILY",
                        title = "햄스터 다리 왜일까요??",
                        content = "아제부터 약간 저는것같은데 병원을 가봐야하지 이렇게 할지 모르겠어요. 케이지 안에 미끄럼틀 타다가 그랬겠죠? 운동을 시키고 싶은데, 어떤 놀이가 좋을까요?",
                        thumbnailUrl = "https://example.com/image.jpg",
                        imageCount = 3,
                        viewCount = 54,
                        commentCount = 4,
                        likeCount = 3,
                        createdAt = "3시간 전",
                        isLiked = false
                    )
                ),
                hasNext = true,
                currentType = null,
                currentTopic = null,
                currentSort = "latest"
            )
        )
    }
}
