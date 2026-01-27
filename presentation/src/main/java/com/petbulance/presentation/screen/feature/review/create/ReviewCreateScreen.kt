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
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
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
import com.petbulance.presentation.screen.feature.review.create.views.ExitDialog
import com.petbulance.presentation.screen.feature.review.create.views.ReceiptVerifiedCard
import com.petbulance.presentation.screen.feature.review.create.views.ReviewProgressBar
import com.petbulance.presentation.screen.feature.review.create.views.Step1HospitalContent
import com.petbulance.presentation.screen.feature.review.create.views.Step2AnimalContent
import com.petbulance.presentation.screen.feature.review.create.views.Step3ReviewContent
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
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isVerified = argument.state.step1.isReceiptVerified
    var isVerifiedCardVisible by remember { mutableStateOf(argument.state.step1.isReceiptVerified) }

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
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val currentImages = argument.state.step3.images
            val newImages = uris.map { it.toString() }
            val combinedImages = (currentImages + newImages).take(10)
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
                            intent = argument.intent
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
                                if (argument.state.step3.images.size >= 10) {
                                    Toast.makeText(
                                        context,
                                        "최대 10장까지만 첨부 가능합니다.",
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
        ExitDialog(
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
                        hospitalInfo = HospitalInfo(1, "행복 동물병원"),
                        totalPrice = "50000",
                        treatments = listOf("중성화 수술")
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
                        hospitalInfo = HospitalInfo(1, "행복 동물병원"),
                        totalPrice = "50000",
                        treatments = listOf("중성화 수술")
                    ),
                    step2 = Step2State(
                        animalType = "강아지",
                        detailAnimalType = "말티즈",
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
                        hospitalInfo = HospitalInfo(1, "행복 동물병원"),
                        totalPrice = "50000",
                        treatments = listOf("중성화 수술")
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