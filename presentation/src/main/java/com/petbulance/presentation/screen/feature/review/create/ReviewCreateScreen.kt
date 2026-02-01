package com.petbulance.presentation.screen.feature.review.create

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
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
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.molecule.WarningDialog
import com.petbulance.presentation.screen.feature.review.create.composables.ReceiptVerifiedCard
import com.petbulance.presentation.screen.feature.review.create.composables.ReviewProgressBar
import com.petbulance.presentation.screen.feature.review.create.composables.ReviewSubmitCompleteDialog
import com.petbulance.presentation.screen.feature.review.create.views.Step1HospitalContent
import com.petbulance.presentation.screen.feature.review.create.views.Step2AnimalContent
import com.petbulance.presentation.screen.feature.review.create.views.Step3ReviewContent
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReviewCreateScreen(
    navController: NavController,
    argument: ReviewCreateArgument
) {
    val context = LocalContext.current
    val isVerified = argument.state.step1.isReceiptVerified
    val maxImages = 5

    var isVerifiedCardVisible by remember { mutableStateOf(argument.state.step1.isReceiptVerified) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showSubmitCompleteDialog by remember { mutableStateOf(false) }
    var showDetailAnimalBottomSheet by remember { mutableStateOf(false) }
    var submittedReviewId by remember { mutableStateOf<Long?>(null) }

    BackHandler {
        when (argument.state.currentStep) {
            ReviewCreateStep.HOSPITAL_AND_COST -> {
                argument.intent(ReviewCreateIntent.OnCloseClicked)
            }

            ReviewCreateStep.ANIMAL_AND_RATING -> {
                argument.intent(ReviewCreateIntent.OnPreviousStep)
            }

            ReviewCreateStep.REVIEW_CONTENT -> {
                argument.intent(ReviewCreateIntent.OnPreviousStep)
            }
        }
    }

    LaunchedEffect(isVerified) {
        if (isVerified) {
            isVerifiedCardVisible = true
            delay(3000L)
            isVerifiedCardVisible = false
        }
    }

    // Photo Picker Launcher 설정
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxImages)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val currentImages = argument.state.step3.images
            val remaining = (maxImages - currentImages.size).coerceAtLeast(0)
            if (remaining == 0) return@rememberLauncherForActivityResult

            val selected = uris.map { it.toString() }
            val accepted = selected.take(remaining)
            val combinedImages = currentImages + accepted

            if (selected.size > remaining) {
                Toast.makeText(
                    context,
                    "이미지는 최대 ${maxImages}장까지 첨부 가능해요. ${remaining}장만 추가됩니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            argument.intent(ReviewCreateIntent.OnImagesChanged(combinedImages))
        }
    }

    // 권한 상태 관리
    val mediaPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val mediaPermissionState = rememberPermissionState(mediaPermission)

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is ReviewCreateEvent.NavigateBack -> navController.safePopBackStack()
                is ReviewCreateEvent.NavigateToHome -> {
                    navController.safePopBackStack()
                }

                is ReviewCreateEvent.OnSubmitSuccess -> {
                    submittedReviewId = event.reviewId
                    showSubmitCompleteDialog = true
                }

                is ReviewCreateEvent.ShowExitDialog -> showExitDialog = true
                is ReviewCreateEvent.ShowToast -> {
                    Toast.makeText(navController.context, event.message, Toast.LENGTH_SHORT).show()
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
                    onLeadingIconClicked = { argument.intent(ReviewCreateIntent.OnBackClicked) },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Outlined.Info)) {
                            argument.intent(ReviewCreateIntent.OnCloseClicked)
                        }
                    )
                )
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                ReviewProgressBar(
                    modifier = Modifier.padding(horizontal = spacingXL, vertical = spacingXS),
                    currentStep = argument.state.currentStep
                )

                when (argument.state.currentStep) {
                    ReviewCreateStep.HOSPITAL_AND_COST -> {
                        Step1HospitalContent(
                            state = argument.state.step1,
                            intent = argument.intent,
                            onDetailAnimalInputClicked = { showDetailAnimalBottomSheet = true }
                        )
                    }

                    ReviewCreateStep.ANIMAL_AND_RATING -> {
                        Step2AnimalContent(
                            state = argument.state.step2,
                            intent = argument.intent
                        )
                    }

                    ReviewCreateStep.REVIEW_CONTENT -> {
                        Step3ReviewContent(
                            state = argument.state.step3,
                            intent = argument.intent,
                            onImageAddClicked = {
                                if (argument.state.step3.images.size >= maxImages) {
                                    Toast.makeText(
                                        context,
                                        "최대 ${maxImages}장까지만 첨부 가능합니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (mediaPermissionState.status.isGranted) {
                                        multiplePhotoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    } else {
                                        mediaPermissionState.launchPermissionRequest()
                                        Toast.makeText(
                                            context,
                                            "사진을 첨부하려면 권한이 필요합니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }

            val buttonText =
                if (argument.state.currentStep == ReviewCreateStep.REVIEW_CONTENT) "후기 등록하기"
                else "다음"

            BasicButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        vertical = spacingXXL, horizontal = spacingMedium
                    ),
                text = buttonText,
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.PRIMARY,
                radius = 16.dp
            ) {
                if (argument.state.currentStep == ReviewCreateStep.REVIEW_CONTENT) {
                    argument.intent(ReviewCreateIntent.OnSubmitClicked)

                } else {
                    argument.intent(ReviewCreateIntent.OnNextClicked)
                }
            }
        }
    }

    if (showExitDialog) {
        WarningDialog(
            title = "후기 작성을 중단하고 나가시겠어요?",
            content = "지금 작성한 후기는 저장되지 않아요.",
            onDismissRequest = { showExitDialog = false },
            onExitButtonClicked = {
                showExitDialog = false
                navController.safePopBackStack()
            }
        )
    }

    if (isVerified) {
        ReceiptVerifiedCard()
    }

    if (showSubmitCompleteDialog) {
        ReviewSubmitCompleteDialog(
            onDismissRequest = {
                showSubmitCompleteDialog = false
                navController.safePopBackStack()
            },
            onNavigateToReview = {
                showSubmitCompleteDialog = false
                submittedReviewId?.let { id ->
                    navController.safeNavigate(ScreenDestinations.Review.Detail.createRoute(id)) {
                        popUpTo(ScreenDestinations.Review.Create.route) { inclusive = true }
                    }
                }
            }
        )
    }

    if (showDetailAnimalBottomSheet) {
        ReviewDetailAnimalSpeciesSelectBottomSheet(
            category = argument.state.step1.animalType,
            selectedDetail = argument.state.step1.detailAnimalType,
            onDismissRequest = { showDetailAnimalBottomSheet = false },
            onDetailSelected = {
                argument.intent(ReviewCreateIntent.OnDetailAnimalTypeChanged(it.name))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep1Preview() {
    PetbulanceTheme {
        ReviewCreateScreen(
            navController = rememberNavController(),
            argument = ReviewCreateArgument(
                state = ReviewCreateState(
                    currentStep = ReviewCreateStep.HOSPITAL_AND_COST,
                    step1 = Step1State(
                        hospitalInfoForReview = HospitalInfoForReview(1, "행복 동물병원"),
                        totalPrice = "50000",
                    )
                ),
                intent = {},
                event = MutableSharedFlow()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep2Preview() {
    PetbulanceTheme {
        ReviewCreateScreen(
            navController = rememberNavController(),
            argument = ReviewCreateArgument(
                state = ReviewCreateState(
                    currentStep = ReviewCreateStep.ANIMAL_AND_RATING,
                    step1 = Step1State(
                        hospitalInfoForReview = HospitalInfoForReview(1, "행복 동물병원"),
                        totalPrice = "50000",
                    ),
                    step2 = Step2State(
                        ratings = ReviewRating(4.0, 5.0, 3.0)
                    ),
                ),
                intent = {},
                event = MutableSharedFlow()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCreateScreenStep3Preview() {
    PetbulanceTheme {
        ReviewCreateScreen(
            navController = rememberNavController(),
            argument = ReviewCreateArgument(
                state = ReviewCreateState(
                    currentStep = ReviewCreateStep.REVIEW_CONTENT,
                    step1 = Step1State(
                        hospitalInfoForReview = HospitalInfoForReview(1, "행복 동물병원"),
                        totalPrice = "50000",
                    ),
                    step3 = Step3State(
                        content = "선생님이 정말 친절하시고 설명도 잘 해주셨어요. 수술 경과도 좋아서 만족합니다."
                    ),
                ),
                intent = {},
                event = MutableSharedFlow()
            )
        )
    }
}