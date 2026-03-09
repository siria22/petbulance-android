package com.petbulance.presentation.screen.feature.review.main

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
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
import com.petbulance.presentation.screen.feature.review.common.ReviewInfoDialog
import com.petbulance.presentation.screen.feature.review.common.ReviewReportReasonDialog
import com.petbulance.presentation.screen.feature.review.detail.composables.DeleteOrEdit
import com.petbulance.presentation.screen.feature.review.detail.composables.ReportOptionDialog
import com.petbulance.presentation.screen.feature.review.main.composables.CreateReceiptDialog
import com.petbulance.presentation.screen.feature.review.main.composables.ReviewListContent
import com.petbulance.presentation.screen.feature.review.main.composables.ReviewSortTypeDialog
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
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

    var showMoreOption by remember { mutableStateOf(false) }
    var selectedReviewId by remember { mutableStateOf<Long?>(null) }
    var showReportReasonDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("") }

    val context = LocalContext.current

    // 1. 카메라 권한 상태 관리
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

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
                onReviewClick = {
                    navController.safeNavigate(
                        ScreenDestinations.Review.Detail.createRoute(it)
                    )
                },
                onMoreClick = { reviewId ->
                    selectedReviewId = reviewId
                    showMoreOption = true
                },
            )
        }
    }

    if (showBottomSheet) {
        FilterBottomSheet(
            currentQuery = HospitalSearchQueryUiModel.empty.copy(
                region = data.selectedRegion,
                district = data.selectedDistrict,
                animalCategories = data.selectedAnimalType?.let { listOf(it) } ?: emptyList()
            ),
            startTab = startTab,
            showBottomSheet = showBottomSheet,
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false },
            onQuerySet = { query ->
                query.region?.let {
                    argument.intent(ReviewIntent.ChangeRegion(it, query.district ?: ""))
                }
                if (query.animalCategories.isNotEmpty()) {
                    argument.intent(ReviewIntent.ChangeAnimalType(query.animalCategories.first()))
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
        CreateReceiptDialog(
            onDismissRequest = { showReceiptDialog = false },
            onConfirm = {
                // "영수증 없이 쓰기" -> 바로 Create 화면 이동
                showReceiptDialog = false
                navController.safeNavigate(ScreenDestinations.Review.Create.route)
            },
            onConfirmWithoutReceipt = {
                // "영수증 인증하고 쓰기" (이름이 반대같지만 로직상 여기) -> 권한 체크 후 이동
                if (cameraPermissionState.status.isGranted) {
                    showReceiptDialog = false
                    navController.safeNavigate(ScreenDestinations.Review.ReceiptCamera.route)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                    Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showMoreOption) {
        val selectedReview = selectedReviewId?.let { id ->
            data.reviews.find { it.id == id }
        }
        
        if (selectedReview?.isAuthor == true) {
            DeleteOrEdit(
                onDeleteOptionClicked = {
                    showMoreOption = false
                    showDeleteConfirmDialog = true
                },
                onEditOptionClicked = {
                    showMoreOption = false
                    selectedReviewId?.let { reviewId ->
                        navController.safeNavigate(
                            ScreenDestinations.Review.Edit.createRoute(reviewId)
                        )
                    }
                },
                onDismissRequest = { showMoreOption = false }
            )
        } else {
            ReportOptionDialog(
                onReportOptionClicked = {
                    showMoreOption = false
                    showReportReasonDialog = true
                },
                onDismissRequest = { showMoreOption = false }
            )
        }
    }

    if (showDeleteConfirmDialog) {
        // TODO: 삭제 확인 다이얼로그 추가 필요
        // WarningDialog 또는 유사한 컴포넌트 사용
        showDeleteConfirmDialog = false
        selectedReviewId?.let { reviewId ->
            // argument.intent(ReviewIntent.DeleteReview(reviewId))
        }
        selectedReviewId = null
    }

    if (showReportReasonDialog) {
        ReviewReportReasonDialog(
            selectedReason = selectedReason,
            onReasonClicked = { reason ->
                selectedReason = reason
            },
            onSubmitClicked = {
                showReportReasonDialog = false
                selectedReviewId?.let { reviewId ->
                    argument.intent(ReviewIntent.ReportReview(reviewId, selectedReason))
                    Toast.makeText(context, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                }
                selectedReviewId = null
                selectedReason = ""
            },
            onDismissRequest = {
                showReportReasonDialog = false
                selectedReviewId = null
                selectedReason = ""
            }
        )
    }

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {

                else -> {}
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
                reviews = HospitalReview.stubs(),
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