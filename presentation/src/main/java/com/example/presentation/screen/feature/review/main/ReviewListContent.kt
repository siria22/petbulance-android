package com.example.presentation.screen.feature.review.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.molecule.ReviewCard

@Composable
fun ReviewListContent(
    modifier: Modifier = Modifier,
    data: ReviewData,
    onLoadMore: () -> Unit,
    onFilterClick: (FilterBottomSheetTab) -> Unit,
    onSortClick: () -> Unit,
    onReceiptToggle: () -> Unit,
    onPhotoToggle: () -> Unit,
    emptyView: @Composable () -> Unit = { ReviewEmptyView() }
) {
    val listState = rememberLazyListState()

    // 무한 스크롤 감지
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount) &&
                        (lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            onLoadMore()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.bg.frame.default)
    ) {
        ReviewFilterChips(
            data = data,
            onFilterClick = onFilterClick,
            onSortClick = onSortClick,
            onReceiptToggle = onReceiptToggle,
            onPhotoToggle = onPhotoToggle
        )

        if (data.reviews.isEmpty()) {
            emptyView()
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(data.reviews) { review ->
                    ReviewCard(review = review)
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorScheme.border.verySubtle
                    )
                }
            }
        }
    }
}