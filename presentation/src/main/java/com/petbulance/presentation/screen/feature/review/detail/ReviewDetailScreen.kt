package com.petbulance.presentation.screen.feature.review.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicChip
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.StarRatingView
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
import com.petbulance.presentation.screen.feature.review.common.ReviewReportReasonDialog
import com.petbulance.presentation.screen.feature.review.detail.composables.DeleteOrEdit
import com.petbulance.presentation.screen.feature.review.detail.composables.ReportOptionDialog
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Locale

// TODO: 비로그인 사용자가 이 화면에 접근하면 로그인 페이지로 이동시키거나 LoginRequiredDialog를 표시해야 함
//  - CheckLoginStatusUseCase로 로그인 여부 확인 후 처리
//  - 또는 Destination 단에서 진입 전 로그인 체크
@Composable
fun ReviewDetailScreen(
    navController: NavController,
    argument: ReviewDetailArgument,
    data: ReviewDetailData
) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var showMoreOption by remember { mutableStateOf(false) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showReportReasonDialog by remember { mutableStateOf(false) }

    var showReportSuccessToast by remember { mutableStateOf(false) }

    var selectedReason by remember { mutableStateOf("") }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is ReviewDetailEvent.DataFetch.Error -> {
                    if (event.displayType == ErrorDisplayType.Custom) {
                        showErrorDialog = true
                    }
                }

                is ReviewDetailEvent.DeleteSuccess -> {
                    // TODO : Delete Success
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                    navController.safePopBackStack()
                }

                is ReviewDetailEvent.ReportSuccess -> {
                    showReportReasonDialog = false
                    showReportSuccessToast = true
                }
            }
        }
    }

    LaunchedEffect(showReportSuccessToast) {
        if (showReportSuccessToast) {
            delay(3000)
            showReportSuccessToast = false
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = {
                        navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                        navController.safePopBackStack()
                    },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Default.MoreVert)) {
                            showMoreOption = true
                        }
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
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ReviewDetailScreenContents(
                data = data,
                onLikeClicked = { argument.intent(ReviewDetailIntent.ToggleLike) }
            )

            if (showReportSuccessToast) {
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
                        text = "[신고 완료] 운영자 검토 후 조치 예정입니다",
                        style = typography.bodySmall,
                        color = colorScheme.text.inverse
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Close),
                        contentDescription = "Close toast",
                        size = iconSizeSmall,
                        tint = colorScheme.icon.inverse,
                        modifier = Modifier.clickable {
                            showReportSuccessToast = false
                        }
                    )
                }
            }
        }
    }

    if (showMoreOption) {
        if (data.isAuthor) {
            DeleteOrEdit(
                onDeleteOptionClicked = {
                    showMoreOption = false
                    showDeleteConfirmDialog = true
                },
                onEditOptionClicked = {
                    showMoreOption = false
                    navController.navigate(ScreenDestinations.Review.Edit.createRoute(data.id))
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
        WarningDialog(
            title = "후기를 삭제할까요?",
            content = "후기를 삭제하면 모든 데이터가 삭제되고 다시 볼 수 없어요.",
            confirmText = "삭제",
            onDismissRequest = { showDeleteConfirmDialog = false },
            onExitButtonClicked = {
                showDeleteConfirmDialog = false
                argument.intent(ReviewDetailIntent.DeleteReview)
            }
        )
    }

    if (showReportReasonDialog) {
        ReviewReportReasonDialog(
            selectedReason = selectedReason,
            onReasonClicked = { reason ->
                selectedReason = reason
            },
            onSubmitClicked = {
                showReportReasonDialog = false
                argument.intent(ReviewDetailIntent.ReportReview(selectedReason))
            },
            onDismissRequest = { showReportReasonDialog = false }
        )
    }

    if (showErrorDialog) {
        BasicDialog(
            backHandler = {
                showErrorDialog = false
                navController.safePopBackStack()
            },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "리뷰를 불러오는데 실패했어요.",
                    style = typography.titleSmall.emp(),
                    color = colorScheme.text.secondary
                )

                BasicButton(
                    text = "이전으로",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 16.dp,
                    onClicked = {
                        showErrorDialog = false
                        navController.safePopBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
private fun ReviewDetailScreenContents(
    data: ReviewDetailData, onLikeClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        ReviewDetailHeaderSection(
            data.animalType,
            data.userNickname,
            data.visitDate
        )

        ReviewDetailSummarySection(
            hospitalName = data.hospitalName,
            rating = data.rating,
            animalType = data.animalType,
            detailAnimalType = data.detailAnimalType,
            price = data.price,
            isReceiptVerified = data.isReceiptVerified
        )

        ReviewDetailImagesSection(images = data.images)

        ReviewDetailContent(data.content)

        ReviewDetailLikesSection(
            isLiked = data.isLiked,
            likeCount = data.likeCount,
            onLikeClick = { onLikeClicked() }
        )
    }
}

@Composable
private fun ReviewDetailHeaderSection(
    animalType: AnimalCategory,
    userNickname: String,
    visitDate: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicChip(text = animalType.korean)

        Dot()

        Text(
            text = userNickname,
            style = typography.labelMedium,
            color = colorScheme.text.caption,
        )

        Dot()

        Text(
            text = visitDate,
            style = typography.labelMedium,
            color = colorScheme.text.caption,
        )
    }
}

@Composable
private fun ReviewDetailSummarySection(
    hospitalName: String,
    rating: Double,
    animalType: AnimalCategory,
    detailAnimalType: AnimalSpecies,
    price: Int,
    isReceiptVerified: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingXS)) {
        Text(
            text = hospitalName,
            color = colorScheme.text.secondary,
            style = typography.titleSmall.emp()
        )

        StarRatingView(rating = rating)

        Text(
            text = "${animalType.korean} > ${detailAnimalType.korean}",
            color = colorScheme.text.secondary,
            style = typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "결제금액 ${String.format(Locale.KOREA, "%,d", price)}원",
                color = colorScheme.text.secondary,
                style = typography.bodySmall
            )
            if (isReceiptVerified) {
                ReceiptVerifiedBadge()
            }
        }
    }
}

@Composable
private fun ReviewDetailImagesSection(images: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        images.forEach { img ->
            BasicImageBox(
                modifier = Modifier.fillMaxWidth(),
                uri = img.toUri(),
            )
        }
    }
}

@Composable
private fun ReviewDetailContent(contents: String) {
    Text(
        text = contents,
        style = typography.bodyMedium,
        color = colorScheme.text.secondary
    )
}

@Composable
private fun ReviewDetailLikesSection(
    isLiked: Boolean,
    likeCount: Int,
    onLikeClick: () -> Unit
) {
    val contentColor = if (isLiked) colorScheme.status.success.default else colorScheme.text.caption
    val iconResource =
        if (isLiked) IconResource.Drawable(R.drawable.ic_thumbs_up_double_filled)
        else IconResource.Drawable(R.drawable.ic_thumbs_up_double)

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onLikeClick)
            .fillMaxWidth()
    ) {
        Text(
            text = "도움이 됐어요",
            style = typography.labelMedium,
            color = contentColor
        )
        BasicIcon(
            iconResource = iconResource,
            contentDescription = "Thumbs up",
            size = iconSizeSmall,
            tint = contentColor
        )
        Text(
            text = "$likeCount",
            style = typography.labelMedium,
            color = contentColor
        )
    }
}

@Preview
@Composable
private fun ReviewDetailScreenPreview() {
    PetbulanceTheme {
        ReviewDetailScreen(
            navController = rememberNavController(),
            argument = ReviewDetailArgument(
                intent = { },
                dataState = ReviewDetailDataState.Init,
                event = MutableSharedFlow()
            ),
            data = ReviewDetailData.empty
        )
    }
}