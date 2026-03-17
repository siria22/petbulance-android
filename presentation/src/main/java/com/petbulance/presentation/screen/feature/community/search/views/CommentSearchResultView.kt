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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentRes
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.dropShadow
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.component.ui.spacingXXXS
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchArgument
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchData
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchDataState
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchIntent
import com.petbulance.presentation.screen.feature.community.search.components.SearchEmptyState
import com.petbulance.presentation.screen.feature.community.search.components.SearchScopeDropdown
import com.petbulance.presentation.utils.buildHighlightedText
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun CommentSearchResultView(
    argument: CommunitySearchArgument,
    data: CommunitySearchData,
    dataState: CommunitySearchDataState,
    onFilterClick: () -> Unit,
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
        if (shouldLoadMore && data.hasNextComment && dataState != CommunitySearchDataState.LoadingMore) {
            argument.intent(CommunitySearchIntent.LoadMoreComments)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.bg.frame.default)
    ) {
        // 필터 및 검색 대상 영역
        FilterAndScopeRow(
            filterText = data.filterDisplayText,
            searchScope = data.searchScope,
            onFilterClick = onFilterClick,
            onScopeChange = { scope ->
                argument.intent(CommunitySearchIntent.ChangeSearchScope(scope))
            }
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

            data.commentResults.isEmpty() && data.searchKeyword.isNotEmpty() -> {
                SearchEmptyState(
                    searchKeyword = data.searchKeyword,
                    onCreatePostClick = onCreatePostClick
                )
            }

            else -> {
                CommentSearchList(
                    comments = data.commentResults,
                    searchKeyword = data.searchKeyword,
                    listState = listState,
                    isLoadingMore = dataState is CommunitySearchDataState.LoadingMore,
                    onCommentClick = { postId, commentId ->
                        argument.intent(
                            CommunitySearchIntent.NavigateToPostDetail(
                                postId,
                                commentId
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterAndScopeRow(
    filterText: String,
    searchScope: String,
    onFilterClick: () -> Unit,
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 검색 대상 드롭다운
            SearchScopeDropdown(
                selectedScope = searchScope,
                isCommentTab = true,
                onScopeSelected = onScopeChange
            )
        }
    }
}

@Composable
private fun CommentSearchList(
    comments: List<SearchPostCommentRes>,
    searchKeyword: String,
    listState: LazyListState,
    isLoadingMore: Boolean,
    onCommentClick: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = comments,
            key = { it.commentId }
        ) { comment ->
            CommentSearchItem(
                comment = comment,
                searchKeyword = searchKeyword,
                onClick = { onCommentClick(comment.postId, comment.commentId) }
            )
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

@Composable
private fun CommentSearchItem(
    comment: SearchPostCommentRes,
    searchKeyword: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(PetbulanceTheme.colorScheme.bg.frame.default)
            .padding(horizontal = spacingXL, vertical = spacingSmall),
        verticalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        // 댓글 내용
        Text(
            text = buildHighlightedText(comment.commentContent, searchKeyword),
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.text.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        // 작성자 및 날짜
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = comment.writerNickname,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.text.secondary
            )

            Dot(dotColor = colorScheme.icon.veryLight)

            Text(
                text = comment.createdAt,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.text.tertiary
            )
        }

        // 원본 게시글 정보 (본문 캡션 태그 + 제목)
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(colorScheme.bg.frame.subtle)
                    .padding(horizontal = spacingXS, vertical = spacingXXXS)
            ) {
                Text(
                    text = "원문",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.text.tertiary
                )
            }

            // 원본 게시글 제목
            Text(
                text = buildHighlightedText(comment.postTitle, searchKeyword),
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.text.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

@Preview
@Composable
private fun CommentSearchItemPreview() {
    PetbulanceTheme {
        CommentSearchItem(
            comment = SearchPostCommentRes(
                commentId = 1L,
                boardId = 1L,
                boardName = "소형포유류",
                postId = 100L,
                postTitle = "햄스터 건강 체크 방법",
                writerNickname = "햄스터집사",
                commentContent = "저희 햄스터도 비슷한 증상이 있었는데 병원 가니까 금방 나았어요!",
                createdAt = "2024.03.15"
            ),
            searchKeyword = "햄스터",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentSearchResultViewPreview() {
    val sampleComments = listOf(
        SearchPostCommentRes(
            commentId = 1L,
            boardId = 1L,
            boardName = "소형포유류",
            postId = 100L,
            postTitle = "햄스터 건강 체크 방법",
            writerNickname = "햄스터집사",
            commentContent = "저희 햄스터도 비슷한 증상이 있었는데 병원 가니까 금방 나았어요!",
            createdAt = "2024.03.15"
        ),
        SearchPostCommentRes(
            commentId = 2L,
            boardId = 2L,
            boardName = "고양이",
            postId = 200L,
            postTitle = "고양이 사료 추천",
            writerNickname = "냥이맘",
            commentContent = "저희 아이는 이 사료 정말 잘 먹어요.",
            createdAt = "2024.03.16"
        )
    )

    val data = CommunitySearchData(
        searchKeyword = "햄스터",
        searchScope = "content",
        currentSort = "latest",
        selectedAnimalCategory = null,
        selectedPostCategory = null,
        postResults = emptyList(),
        commentResults = sampleComments,
        hasNextPost = false,
        hasNextComment = false,
        totalPostCount = 0L,
        totalCommentCount = 2L
    )

    val argument = CommunitySearchArgument(
        intent = {},
        dataState = CommunitySearchDataState.Init,
        event = MutableSharedFlow()
    )

    PetbulanceTheme {
        CommentSearchResultView(
            argument = argument,
            data = data,
            dataState = CommunitySearchDataState.Init,
            onFilterClick = {},
            onCreatePostClick = {}
        )
    }
}
