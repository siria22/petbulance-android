package com.example.presentation.screen.feature.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.type.ReviewSortType
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicFabIcon
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.molecule.ReviewCard
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavController,
    argument: ReviewArgument,
    data: ReviewData
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var startTab by remember { mutableStateOf(FilterBottomSheetTab.REGION) }

    var showInfoDialog by remember { mutableStateOf(false) }
    var showSortingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "병원 후기",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Filled.Search)) {
                            /* TODO : Search Screen */
                        },
                        Pair(IconResource.Vector(Icons.Outlined.Info)) {
                            showInfoDialog = true
                        },
                    )
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.REVIEW,
                navController = navController
            )
        },
        floatingActionButton = {
            BasicFabIcon(
                iconResource = IconResource.Vector(Icons.Default.Edit), // TODO : 아이콘 교체
                onClick = { /* TODO : Edit Review */ }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ReviewListContent(
                data = data,
                onLoadMore = { argument.intent(ReviewIntent.LoadMore) },
                onFilterClick = { tab ->
                    startTab = tab
                    showBottomSheet = true
                },
                onSortClick = { showSortingDialog = true },
                onReceiptToggle = { argument.intent(ReviewIntent.ToggleReceipt) },
                onPhotoToggle = { argument.intent(ReviewIntent.TogglePhotoReview) }
            )
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
                        argument.intent(ReviewIntent.ChangeRegion(it, query.district ?: ""))
                    }
                    query.species?.let {
                        argument.intent(ReviewIntent.ChangeAnimalType(it))
                    }
                    showBottomSheet = false
                },
                onResetFilterClicked = {
                    argument.intent(ReviewIntent.Refresh)
                },
                onSearchButtonClicked = {
                    showBottomSheet = false
                }
            )
        }
    }

    if (showInfoDialog) {
        ReviewInfoDialog(onDismissRequest = { showInfoDialog = false })
    }

    if (showSortingDialog) {
        ReviewSortTypeDialog(
            selectedSortType = data.selectedSort,
            onDismissRequest = { showSortingDialog = false },
            onSortTypeSelected = { sortType ->
                argument.intent(ReviewIntent.ChangeSort(sortType))
                showSortingDialog = false
            }
        )
    }

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is ReviewEvent.ShowErrorToast -> {

                }
            }
        }
    }
}

@Composable
private fun ReviewListContent(
    data: ReviewData,
    onLoadMore: () -> Unit,
    onFilterClick: (FilterBottomSheetTab) -> Unit,
    onSortClick: () -> Unit,
    onReceiptToggle: () -> Unit,
    onPhotoToggle: () -> Unit
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
        modifier = Modifier
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
            NoResult()
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

@Composable
private fun NoResult() {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight().padding(vertical = spacingXL)
    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.img_no_result_3),
            contentDescription = "No result",
            size = 160.dp,
            tint = Color.Unspecified
        )
        Text(
            text = "해당 조건의 병원 후기가 없어요.",
            style = typography.titleSmall,
            color = colorScheme.text.tertiary
        )
        Text(
            text = "ㅈ필터를 조정해서 다시 검색해주세요.",
            textAlign = Center,
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
                text = "새로운 병원 제보하기",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp,
                onClicked = {
                    /* TODO : 지도 페이지로 이동 */
                }
            )
            BasicButton(
                modifier = Modifier.fillMaxWidth(),
                text = "커뮤니티에 질문하기",
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.SECONDARY,
                radius = 12.dp,
                onClicked = {
                    /* TODO : 지도 페이지로 이동 */
                }
            )
        }
    }
}

@Preview
@Composable
private fun ReviewScreenPreview() {
    PetbulanceTheme {
        ReviewScreen(
            navController = rememberNavController(),
            argument = ReviewArgument(
                state = ReviewState.Init,
                intent = {},
                event = MutableSharedFlow()
            ),
            data = ReviewData(
//                reviews = listOf(
//                    HospitalReview.stub,
//                    HospitalReview.stub.copy(id = 2)
//                ),
                reviews = emptyList(),
                selectedRegion = null,
                selectedDistrict = null,
                selectedAnimalType = null,
                isLoadingNextPage = false,
                selectedSort = ReviewSortType.LATEST,
                isReceiptVerified = true,
                isPhotoReview = false
            )
        )
    }
}