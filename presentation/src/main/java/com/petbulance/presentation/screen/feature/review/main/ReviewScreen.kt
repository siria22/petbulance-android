package com.petbulance.presentation.screen.feature.review.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.BasicFabIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheet
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheetTab
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var startTab by remember { mutableStateOf(FilterBottomSheetTab.REGION) }

    var showInfoDialog by remember { mutableStateOf(false) }
    var showSortingDialog by remember { mutableStateOf(false) }

    var showReceiptDialog by remember { mutableStateOf(false) }

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
                            navController.safeNavigate(ScreenDestinations.Review.Search.route)
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
                iconResource = IconResource.Drawable(R.drawable.ic_write),
                onClick = { showReceiptDialog = true }
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
                onPhotoToggle = { argument.intent(ReviewIntent.TogglePhotoReview) },
            )
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

    if (showReceiptDialog) {
        ReceiptDialog(
            onDismissRequest = { showReceiptDialog = false },
            onConfirm = {
                showReceiptDialog = false
                navController.safeNavigate(ScreenDestinations.Review.Create.route)
            },
            onConfirmWithoutReceipt = {
                showReceiptDialog = false
                navController.safeNavigate(ScreenDestinations.Review.ReceiptCamera.route)
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
                reviews = listOf(
                    HospitalReview.stub,
                    HospitalReview.stub.copy(id = 2)
                ),
//                reviews = emptyList(),
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