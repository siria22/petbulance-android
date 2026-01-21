package com.example.presentation.screen.feature.review.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.type.ReviewSortType
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMS
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXXS
import com.example.presentation.screen.feature.review.main.ReviewData
import com.example.presentation.screen.feature.review.main.ReviewEmptyView
import com.example.presentation.screen.feature.review.main.ReviewListContent
import com.example.presentation.screen.feature.review.main.ReviewSortTypeDialog
import com.example.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.example.presentation.screen.feature.search.main.views.search.SearchBar
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewSearchScreen(
    navController: NavController,
    argument: ReviewSearchArgument,
    data: ReviewSearchData
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var startTab by remember { mutableStateOf(FilterBottomSheetTab.REGION) }
    var showSortingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBar(
                queryString = data.query,
                onQueryStringChanged = { argument.intent(ReviewSearchIntent.UpdateQuery(it)) },
                onMoveBackIconClicked = { navController.popBackStack() },
                onSearchButtonClicked = { argument.intent(ReviewSearchIntent.Search) }
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (data.isSearchResultMode) {
                ReviewListContent(
                    data = ReviewData(
                        reviews = data.searchResults,
                        selectedRegion = null,
                        selectedDistrict = null,
                        selectedAnimalType = null,
                        isLoadingNextPage = data.isLoadingNextPage,
                        selectedSort = com.example.domain.model.type.ReviewSortType.LATEST,
                        isReceiptVerified = false,
                        isPhotoReview = false
                    ),
                    onLoadMore = { argument.intent(ReviewSearchIntent.LoadMore) },
                    onFilterClick = { tab ->
                        startTab = tab
                        showBottomSheet = true
                    },
                    onSortClick = { showSortingDialog = true },
                    onReceiptToggle = { argument.intent(ReviewSearchIntent.ToggleReceipt) },
                    onPhotoToggle = { argument.intent(ReviewSearchIntent.TogglePhotoReview) },
                    emptyView = {
                        ReviewEmptyView(
                            title = "'${data.query}'에 대한 결과가 없어요.",
                            description = "오타가 있는지 확인하거나 다른 검색어를 입력해보세요."
                        )
                    }
                )
            } else {
                RecentSearchList(
                    keywords = data.recentKeywords,
                    onKeywordClick = { keyword ->
                        argument.intent(ReviewSearchIntent.UpdateQuery(keyword))
                        argument.intent(ReviewSearchIntent.Search)
                    },
                    onDeleteClick = { keyword ->
                        argument.intent(ReviewSearchIntent.DeleteRecentKeyword(keyword))
                    }
                )
            }
        }
    }

    if (showBottomSheet) {
        FilterBottomSheet(
            currentQuery = HospitalSearchQueryUiModel.empty.copy(
                region = data.selectedRegion,
                district = data.selectedDistrict,
                species = data.selectedAnimalType
            ),
            startTab = startTab,
            showBottomSheet = showBottomSheet,
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false },
            onQuerySet = { query ->
                query.region?.let {
                    argument.intent(ReviewSearchIntent.ChangeRegion(it, query.district ?: ""))
                }
                query.species?.let {
                    argument.intent(ReviewSearchIntent.ChangeAnimalType(it))
                }
                showBottomSheet = false
            },
            onResetFilterClicked = {
                argument.intent(ReviewSearchIntent.Refresh)
            },
            onSearchButtonClicked = {
                showBottomSheet = false
            }
        )
    }

    if (showSortingDialog) {
        ReviewSortTypeDialog(
            selectedSortType = data.selectedSort,
            onDismissRequest = { showSortingDialog = false },
            onSortTypeSelected = { sortType ->
                argument.intent(ReviewSearchIntent.ChangeSort(sortType))
                showSortingDialog = false
            }
        )
    }

}

@Composable
private fun RecentSearchList(
    keywords: List<RecentSearchKeyword>,
    onKeywordClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacingMedium, vertical = spacingXS)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacingXS),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "최근 검색어",
                style = MaterialTheme.typography.bodyLarge.emp(),
                color = colorScheme.text.primary,
            )
            Text(
                text = "전체 삭제",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.text.secondary,
                modifier = Modifier.clickable {

                }
            )
        }

        if (keywords.isEmpty()) {
            Text(
                text = "검색 내역이 없습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.text.caption,
                modifier = Modifier.padding(start = spacingXXXS, top = spacingXS)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(spacingXXXS)) {
                keywords.forEach { item ->
                    RecentSearchChip(
                        text = item.keyword,
                        onClick = { onKeywordClick(item.keyword) },
                        onDelete = { onDeleteClick(item.keyword) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentSearchChip(
    text: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorScheme.bg.frame.default)
            .padding(vertical = spacingXS)
    ) {
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.AccessTime),
            contentDescription = "History time",
            size = iconSizeMS,
            tint = colorScheme.icon.light
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.text.secondary,
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
        )

        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Clear),
            contentDescription = "Delete history",
            size = iconSizeMS,
            tint = colorScheme.icon.light,
            modifier = Modifier.clickable { onDelete() }
        )
    }
}

@Preview
@Composable
private fun ReviewSearchScreenPreview() {
    PetbulanceTheme {
        ReviewSearchScreen(
            navController = rememberNavController(),
            argument = ReviewSearchArgument(
                state = ReviewSearchState.Init,
                intent = {},
                event = MutableSharedFlow()
            ),
            data = ReviewSearchData(
                query = "햄스터",
                recentKeywords = listOf(
                    RecentSearchKeyword(1, "강남 동물병원", "2024.01.01"),
                    RecentSearchKeyword(2, "골절", "2024.01.02")
                ),
                searchResults = emptyList(),
                isSearchResultMode = false,
                isLoadingNextPage = false
            )
        )
    }
}

@Preview
@Composable
private fun ReviewSearchResultPreview() {
    PetbulanceTheme {
        ReviewSearchScreen(
            navController = rememberNavController(),
            argument = ReviewSearchArgument(
                state = ReviewSearchState.Init,
                intent = {},
                event = MutableSharedFlow()
            ),
            data = ReviewSearchData(
                query = "강남",
                recentKeywords = emptyList(),
                searchResults = listOf(
                    HospitalReview(
                        id = 1,
                        isReceiptVerified = true,
                        treatment = "슬개골 탈구 수술",
                        animalType = "강아지",
                        detailAnimalType = "말티즈",
                        content = "친절하고 꼼꼼하게 봐주셔서 좋았습니다. 수술 경과도 매우 좋아요!",
                        rating = 4.5,
                        date = "2024.01.15",
                        likeCount = 12,
                        isLiked = true,
                        imageUrls = emptyList(),
                        author = "멍멍이맘",
                        price = 1500000,
                        hospitalName = "행복동물병원"
                    ),
                    HospitalReview(
                        id = 2,
                        isReceiptVerified = false,
                        treatment = "종합 백신 접종",
                        animalType = "고양이",
                        detailAnimalType = "코숏",
                        content = "대기 시간이 좀 길었지만 선생님은 친절하셨어요.",
                        rating = 3.5,
                        date = "2024.01.10",
                        likeCount = 3,
                        isLiked = false,
                        imageUrls = emptyList(),
                        author = "냥냥펀치",
                        price = 50000,
                        hospitalName = "므와므와"
                    )
                ),
                isSearchResultMode = true,
                isLoadingNextPage = false,
                selectedRegion = null,
                selectedDistrict = null,
                selectedAnimalType = null,
                selectedSort = ReviewSortType.LATEST,
                isReceiptVerified = false,
                isPhotoReview = false
            )
        )
    }
}