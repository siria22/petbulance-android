package com.petbulance.presentation.screen.feature.community.search.views

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.community.post.PostSearchSummary
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.dropShadow
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchArgument
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchData
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchDataState
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchIntent
import com.petbulance.presentation.screen.feature.community.search.components.PostListItem
import com.petbulance.presentation.screen.feature.community.search.components.SearchEmptyState
import com.petbulance.presentation.screen.feature.community.search.components.SearchScopeDropdown
import com.petbulance.presentation.screen.feature.community.search.mapper.toPostSummaryList
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun PostSearchResultView(
    argument: CommunitySearchArgument,
    data: CommunitySearchData,
    dataState: CommunitySearchDataState,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && data.hasNextPost && dataState != CommunitySearchDataState.LoadingMore) {
            argument.intent(CommunitySearchIntent.LoadMorePosts)
        }
    }

    PostSearchResultViewContent(
        data = data,
        dataState = dataState,
        onFilterClick = onFilterClick,
        onSortClick = onSortClick,
        onScopeChange = { scope ->
            argument.intent(CommunitySearchIntent.ChangeSearchScope(scope))
        },
        onCreatePostClick = onCreatePostClick,
        listState = listState,
        onPostClick = { postId ->
            argument.intent(CommunitySearchIntent.NavigateToPostDetail(postId))
        },
        onLikeClick = { postId ->
            // TODO: 좋아요 기능 구현
        },
        modifier = modifier
    )
}

@Composable
private fun PostSearchResultViewContent(
    data: CommunitySearchData,
    dataState: CommunitySearchDataState,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onScopeChange: (String) -> Unit,
    onCreatePostClick: () -> Unit,
    listState: LazyListState,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.bg.frame.default)
    ) {
        // 필터 및 정렬 영역
        FilterAndSortRow(
            filterText = data.filterDisplayText,
            currentSort = data.currentSort,
            searchScope = data.searchScope,
            onFilterClick = onFilterClick,
            onSortClick = onSortClick,
            onScopeChange = onScopeChange
        )

        // 검색 결과
        when {
            dataState is CommunitySearchDataState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorScheme.action.primary.default
                    )
                }
            }

            data.postResults.isEmpty() && data.searchKeyword.isNotEmpty() -> {
                SearchEmptyState(
                    searchKeyword = data.searchKeyword,
                    onCreatePostClick = onCreatePostClick
                )
            }

            else -> {
                PostSearchList(
                    posts = data.postResults,
                    searchKeyword = data.searchKeyword,
                    listState = listState,
                    isLoadingMore = dataState is CommunitySearchDataState.LoadingMore,
                    onPostClick = onPostClick,
                    onLikeClick = onLikeClick
                )
            }
        }
    }
}

@Composable
private fun FilterAndSortRow(
    filterText: String,
    currentSort: String,
    searchScope: String,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onScopeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacingXS)
            .dropShadow(
                shape = RoundedCornerShape(0.dp),
                color = colorScheme.border.subtle,
                blur = 0.dp,
                offsetY = 1.dp
            )
            .background(colorScheme.bg.frame.default)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            modifier = Modifier.padding(horizontal = spacingXXXS)
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Outlined.FilterAlt),
                contentDescription = "Filter",
                size = iconSizeMS,
                tint = colorScheme.icon.basic
            )
            Text(
                text = filterText,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.text.secondary,
                modifier = Modifier.clickable(onClick = onFilterClick)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 정렬 드롭다운 (추후 구현)
            Text(
                text = when (currentSort) {
                    "latest" -> "최신순"
                    "popular" -> "인기순"
                    "comment" -> "댓글순"
                    else -> "최신순"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.text.secondary
            )

            // 검색 대상 드롭다운
            SearchScopeDropdown(
                selectedScope = searchScope,
                isCommentTab = false,
                onScopeSelected = onScopeChange
            )
        }
    }
}

@Composable
private fun PostSearchList(
    posts: List<PostSearchSummary>,
    searchKeyword: String,
    listState: LazyListState,
    isLoadingMore: Boolean,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val postSummaries = remember(posts) { posts.toPostSummaryList() }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = postSummaries,
            key = { it.id }
        ) { post ->
            PostListItem(
                post = post,
                onPostClick = { onPostClick(post.id) },
                onLikeClick = { onLikeClick(post.id) },
                searchKeyword = searchKeyword
            )
            CommonDivider()
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorScheme.action.primary.default
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostSearchResultViewPreview() {
    val samplePosts = List(10) { i ->
        PostSearchSummary(
            id = i.toLong(),
            type = "DOG",
            topic = "HEALTH",
            title = "강아지 건강 관련 질문입니다 $i",
            content = "내용 요약입니다. $i 번째 게시글의 내용입니다. 검색어와 관련된 내용이 포함됩니다.",
            thumbnailUrl = null,
            imageCount = 0,
            viewCount = 100,
            commentCount = 5,
            likeCount = 10,
            createdAt = "1시간 전",
            writerNickname = "작성자$i",
            isLiked = false
        )
    }

    val sampleData = CommunitySearchData(
        searchKeyword = "강아지",
        searchScope = "title_content",
        currentSort = "latest",
        selectedAnimalCategory = null,
        selectedPostCategory = null,
        postResults = samplePosts,
        commentResults = emptyList(),
        hasNextPost = true,
        hasNextComment = false,
        totalPostCount = 100,
        totalCommentCount = 0
    )

    PetbulanceTheme {
        Surface {
            PostSearchResultView(
                argument = CommunitySearchArgument(
                    intent = {},
                    dataState = CommunitySearchDataState.Init,
                    event = MutableSharedFlow()
                ),
                data = sampleData,
                dataState = CommunitySearchDataState.Init,
                onFilterClick = {},
                onSortClick = {},
                onCreatePostClick = {}
            )
        }
    }
}
