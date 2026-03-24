package com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.ReviewStatus
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulancePrimitives
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicCheckBox
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.OnContentLoadingUi
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.molecule.ReceiptVerifiedBadge
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews.composables.MyPageReviewsDeleteReviewsDialog
import com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews.composables.MyPageReviewsNoResultView
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageReviewsScreen(
    navController: NavController,
    argument: MyPageReviewsArgument,
    data: MyPageReviewsData
) {
    val dataState = argument.dataState
    val screenState = argument.screenState

    val normalScreenState = (screenState as? MyPageReviewsScreenState.Normal)
        ?: MyPageReviewsScreenState.Normal()

    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessToast by remember { mutableStateOf(false) }
    var showDeleteRevertedToast by remember { mutableStateOf(false) }
    var pendingDeleteIds by remember { mutableStateOf<Set<Long>?>(null) }

    BackHandler {
        if (normalScreenState.isSelectionMode) {
            argument.intent(MyPageReviewsIntent.ToggleSelectionMode(false))
        } else {
            navController.safePopBackStack()
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is MyPageReviewsEvent.Review.DeleteSuccess -> {
                    argument.intent(MyPageReviewsIntent.ToggleSelectionMode(false))
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
                argument.intent(MyPageReviewsIntent.DeleteSelected)
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
                    text = "후기 관리",
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
                is MyPageReviewsDataState.Loading -> {
                    OnContentLoadingUi(text = "잠시만 기다려주세요...")
                }

                is MyPageReviewsDataState.Loaded -> {
                    if (dataState.reviews.isEmpty()) {
                        MyPageReviewsNoResultView(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateToHospitalReviewScreen = {
                                navController.safeNavigate(ScreenDestinations.Review.route)
                            }
                        )
                    } else {
                        MyPageMyReviewsListView(
                            reviews = dataState.reviews,
                            hasNext = dataState.hasNext,
                            screenState = normalScreenState,
                            onLoadMore = { argument.intent(MyPageReviewsIntent.LoadMore) },
                            onReviewClick = { id ->
                                if (normalScreenState.isSelectionMode) {
                                    argument.intent(MyPageReviewsIntent.ToggleReviewSelection(id))
                                } else {
                                    navController.navigate(
                                        ScreenDestinations.Review.Detail.createRoute(
                                            id
                                        )
                                    )
                                }
                            },
                            onSelectAllClick = { argument.intent(MyPageReviewsIntent.SelectAll) },
                            onDeleteClick = { showDeleteDialog = true }
                        )
                    }
                }

                else -> {}
            }

            if (showDeleteSuccessToast) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = spacingXL, horizontal = spacingMedium)
                        .background(
                            Color(0xFF222222).copy(alpha = 0.9f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(spacingSmall),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "선택한 후기를 삭제했어요",
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

            if (showDeleteRevertedToast) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = spacingXL, horizontal = spacingMedium)
                        .background(
                            Color(0xFF222222).copy(alpha = 0.9f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(spacingSmall),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "후기 삭제를 취소했어요",
                        style = typography.bodySmall,
                        color = colorScheme.text.inverse
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        WarningDialog(
            title = "주의",
            content = "후기를 삭제하면 되돌릴 수 없어요.\n그래도 삭제하시겠어요?",
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
        MyPageReviewsDeleteReviewsDialog(
            onReportOptionClicked = {
                showMenu = false
                argument.intent(MyPageReviewsIntent.ToggleSelectionMode(true))
            },
            onDismissRequest = { showMenu = false }
        )
    }

}

@Composable
private fun MyPageMyReviewsListView(
    reviews: List<MyReview>,
    hasNext: Boolean,
    screenState: MyPageReviewsScreenState.Normal,
    onLoadMore: () -> Unit,
    onReviewClick: (Long) -> Unit,
    onSelectAllClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val listState = rememberLazyListState()

    // 무한 스크롤 감지
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
                SelectionControlBar(
                    isAllSelected = screenState.selectedIds.size == reviews.size && reviews.isNotEmpty(),
                    hasSelection = screenState.selectedIds.isNotEmpty(),
                    onSelectAllClick = onSelectAllClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }

        items(reviews, key = { it.id }) { review ->
            MyPageReviewsItem(
                review = review,
                isSelectionMode = screenState.isSelectionMode,
                isSelected = screenState.selectedIds.contains(review.id),
                onClick = { onReviewClick(review.id) }
            )
        }
    }
}

@Composable
private fun SelectionControlBar(
    isAllSelected: Boolean,
    hasSelection: Boolean,
    onSelectAllClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingMedium, vertical = spacingXS),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onSelectAllClick() }
        ) {
            BasicCheckBox(
                checkState = isAllSelected,
                onCheckedChange = onSelectAllClick
            )
            Text(
                text = "전체선택",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
        }

        BasicButton(
            text = "삭제",
            size = BasicButtonSize.S,
            buttonType = if (hasSelection) BasicButtonType.DEFAULT else BasicButtonType.DISABLED,
            onClicked = { if (hasSelection) onDeleteClick() }
        )
    }
}

@Composable
private fun MyPageReviewsItem(
    review: MyReview,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (review.status == ReviewStatus.UNDER_REVIEW) colorScheme.bg.frame.subtle
        else colorScheme.bg.frame.default

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colorScheme.border.verySubtle
            )
            .background(backgroundColor)
            .padding(spacingMedium)
            .clickable { onClick() },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ){
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
                ReviewStatusChip(review.status)
                Text(
                    text = review.hospitalName,
                    style = typography.bodyMedium.emp(),
                    color = colorScheme.text.secondary
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS)
                ) {
                    Text(
                        text = review.date,
                        color = colorScheme.text.caption,
                        style = typography.labelMedium
                    )
                    Dot(dotColor = PetbulancePrimitives.Gray.p300)
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.ic_thumbs_up_double_filled),
                        contentDescription = "Like counts",
                        size = iconSizeSmall,
                        tint = colorScheme.icon.light
                    )
                    Text(
                        text = review.likeCount.toString(),
                        color = colorScheme.text.caption,
                        style = typography.bodySmall
                    )
                }
                ReceiptVerifiedBadge()
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (review.representativeImage != null) {
                BasicImageBox(
                    size = 72.dp,
                    uri = review.representativeImage!!.toUri(),
                )
            }
            Text(
                text = review.content,
                style = typography.labelLarge,
                color = colorScheme.text.secondary
            )
        }
    }
}

@Composable
private fun ReviewStatusChip(status: ReviewStatus) {
    val contentColor = when (status) {
        ReviewStatus.REGISTERED -> colorScheme.tag.trust.medium
        ReviewStatus.HIDDEN -> colorScheme.tag.red.strong
        ReviewStatus.UNDER_REVIEW -> colorScheme.text.caption
    }

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = contentColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.korean,
            style = typography.labelMedium,
            color = contentColor
        )
    }
}

@Preview
@Composable
private fun MyPageReviewsScreenPreview() {
    PetbulanceTheme {
        MyPageReviewsScreen(
            navController = rememberNavController(),
            argument = MyPageReviewsArgument(
                intent = { },
                dataState = MyPageReviewsDataState.Loaded(
                    reviews = MyReview.stubs(),
                    nextCursorId = 10,
                    hasNext = false
                ),
                screenState = MyPageReviewsScreenState.Normal(
                    isSelectionMode = true,
                    selectedIds = setOf(1L, 2L)
                ),
                event = MutableSharedFlow()
            ),
            data = MyPageReviewsData(
                data = ""
            )
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun SelectionControlBarPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(colorScheme.bg.frame.default)
        ) {
            SelectionControlBar(
                isAllSelected = false,
                hasSelection = false,
                onSelectAllClick = {},
                onDeleteClick = {}
            )
            SelectionControlBar(
                isAllSelected = false,
                hasSelection = true,
                onSelectAllClick = {},
                onDeleteClick = {}
            )
            SelectionControlBar(
                isAllSelected = true,
                hasSelection = true,
                onSelectAllClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun MyPageReviewsItemPreview() {
    PetbulanceTheme {
        Column {
            MyPageReviewsItem(
                review = MyReview.stub(),
                isSelectionMode = false,
                isSelected = false,
                onClick = {}
            )
            MyPageReviewsItem(
                review = MyReview.stub(),
                isSelectionMode = true,
                isSelected = false,
                onClick = {}
            )
            MyPageReviewsItem(
                review = MyReview.stub(),
                isSelectionMode = true,
                isSelected = true,
                onClick = {}
            )
        }
    }
}