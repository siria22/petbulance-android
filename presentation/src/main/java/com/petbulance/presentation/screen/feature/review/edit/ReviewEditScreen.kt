package com.petbulance.presentation.screen.feature.review.edit

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.screen.feature.review.common.ReviewAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewContentInput
import com.petbulance.presentation.screen.feature.review.common.ReviewDetailAnimalTypeInput
import com.petbulance.presentation.screen.feature.review.common.ReviewHospitalNameInput
import com.petbulance.presentation.screen.feature.review.common.ReviewImageSection
import com.petbulance.presentation.screen.feature.review.common.ReviewInfoDialog
import com.petbulance.presentation.screen.feature.review.common.ReviewRatingsSection
import com.petbulance.presentation.screen.feature.review.common.ReviewTotalCostInput
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.screen.feature.review.create.ReviewDetailAnimalSpeciesSelectBottomSheet
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReviewEditScreen(
    navController: NavController,
    argument: ReviewEditArgument,
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }

    var showDetailAnimalBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is ReviewEditEvent.NavigateBack -> navController.safePopBackStack()
                is ReviewEditEvent.ShowExitDialog -> showExitDialog = true
                is ReviewEditEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "후기 작성",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { showExitDialog = true },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Outlined.Info)) {
                            showInfoDialog = true
                        }
                    )
                )
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ReviewEditScreenContents(
                context = context,
                state = argument.state,
                intent = argument.intent,
                onDetailAnimalInputClicked = { showDetailAnimalBottomSheet = true }
            )
        }
    }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        WarningDialog(
            title = "후기 수정을 중단하고 나가시겠어요?",
            content = "수정된 후기는 저장되지 않아요.",
            onDismissRequest = { showExitDialog = false },
            onExitButtonClicked = {
                showExitDialog = false
                navController.safePopBackStack()
            }
        )
    }

    if (showInfoDialog) {
        ReviewInfoDialog(
            onDismissRequest = { showInfoDialog = false }
        )
    }

    if (showDetailAnimalBottomSheet) {
        ReviewDetailAnimalSpeciesSelectBottomSheet(
            category = argument.state.animalType,
            selectedDetail = argument.state.detailAnimalType,
            onDismissRequest = { showDetailAnimalBottomSheet = false },
            onDetailSelected = {
                argument.intent(ReviewEditIntent.OnDetailAnimalTypeChanged(it.name))
            }
        )
    }
}

@Composable
private fun ReviewEditScreenContents(
    context: Context,
    state: ReviewEditState,
    intent: (ReviewEditIntent) -> Unit,
    onDetailAnimalInputClicked: () -> Unit
) {

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10) // 최대 선택 가능 수
    ) { uris ->
        if (uris.isNotEmpty()) {
            val currentExistingCount = state.existingImages.size
            val currentNewImages = state.newImages

            val availableSlots = 5 - (currentExistingCount + currentNewImages.size)
            if (availableSlots > 0) {
                val addedImages = uris.take(availableSlots).map { it.toString() }
                intent(ReviewEditIntent.OnNewImagesChanged(currentNewImages + addedImages))
            } else {
                Toast.makeText(context, "이미지는 최대 5장까지 첨부 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = spacingMedium, vertical = spacingXL)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 1. 병원명
            ReviewHospitalNameInput(
                query = "펫뷸",
                selectedHospitalName = "펫뷸런",
                candidates = listOf(
                    HospitalInfoForReview(1L, "펫뷸런"),
                    HospitalInfoForReview(2L, "펫뷸런2"),
                    HospitalInfoForReview(3L, "펫뷸런3")
                ),
                onQueryChanged = {},
                onClearQuery = {},
                onCandidateClicked = {},
            )

            // 2. 총 비용
            ReviewTotalCostInput(
                cost = state.totalCost,
                onCostChanged = { intent(ReviewEditIntent.OnTotalCostChanged(it)) },
                title = "총 비용"
            )

            // 3. 동물종
            ReviewAnimalTypeInput(
                selectedAnimalType = state.animalType,
                onAnimalTypeSelected = { intent(ReviewEditIntent.OnAnimalTypeChanged(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            // 4. 세부 동물명
            val displayDetailAnimalType = if (state.detailAnimalType.isNotBlank()) {
                AnimalSpecies.fromString(state.detailAnimalType).korean
            } else {
                ""
            }

            ReviewDetailAnimalTypeInput(
                detailAnimalType = displayDetailAnimalType,
                onInputClicked = onDetailAnimalInputClicked
            )

            // 5. 별점
            ReviewRatingsSection(
                ratings = state.ratings,
                onRatingChanged = { intent(ReviewEditIntent.OnRatingChanged(it)) }
            )

            // 6. 이미지 (기존 이미지 + 새로 추가된 이미지)
            ReviewImageSection(
                images = state.existingImages + state.newImages,
                onImageAddClicked = {
                    val currentCount = state.existingImages.size + state.newImages.size
                    if (currentCount >= 10) {
                        Toast.makeText(context, "이미지는 최대 10장까지 첨부 가능합니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // 권한 체크 로직이 있다면 여기에 추가
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                },
                onImageDeleteClicked = { index ->
                    intent(ReviewEditIntent.OnRemoveImage(index))
                }
            )

            // 7. 후기 내용
            ReviewContentInput(
                content = state.content,
                onContentChanged = { intent(ReviewEditIntent.OnContentChanged(it)) }
            )
        }

        // 하단 버튼
        BasicButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spacingXXL, horizontal = spacingMedium),
            text = "수정 완료",
            size = BasicButtonSize.L,
            buttonType = BasicButtonType.PRIMARY,
            radius = 16.dp,
            onClicked = { intent(ReviewEditIntent.OnSubmitClicked) }
        )
    }
}


@Preview
@Composable
private fun ReviewEditScreenPreview() {
    PetbulanceTheme {
        ReviewEditScreen(
            navController = rememberNavController(),
            argument = ReviewEditArgument(
                intent = { },
                state = ReviewEditState(),
                event = MutableSharedFlow(),
            )
        )
    }
}