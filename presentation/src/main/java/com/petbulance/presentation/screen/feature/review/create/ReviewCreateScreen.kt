package com.petbulance.presentation.screen.feature.review.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.ReviewRating
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.RatingBar
import com.petbulance.presentation.component.ui.iconSizeLarge
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReviewCreateScreen(
    navController: NavController,
    argument: ReviewCreateArgument
) {
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(argument.event) {
        argument.event.collectLatest { event ->
            when (event) {
                is ReviewCreateEvent.NavigateBack -> navController.safePopBackStack()
                is ReviewCreateEvent.NavigateToHome -> {
                    navController.popBackStack() // TODO: Adjust pop logic to go Home
                }

                is ReviewCreateEvent.ShowExitDialog -> showExitDialog = true
                is ReviewCreateEvent.ShowToast -> {
                    // Show Toast via context or snackbar host
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
                when (argument.state.currentStep) {
                    ReviewCreateStep.HOSPITAL_AND_RATING -> {
                        Step1HospitalContent(
                            state = argument.state.step1,
                            intent = argument.intent
                        )
                    }

                    ReviewCreateStep.ANIMAL_AND_TREATMENT -> {
                        Step2AnimalContent(
                            state = argument.state.step2,
                            intent = argument.intent
                        )
                    }

                    ReviewCreateStep.REVIEW_CONTENT -> {
                        Step3ReviewContent(
                            state = argument.state.step3,
                            intent = argument.intent
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
                size = BasicButtonSize.M,
                buttonType = BasicButtonType.PRIMARY,
                radius = 12.dp
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
}

@Composable
private fun ExitDialog(
    onDismissRequest: () -> Unit,
    onExitButtonClicked: () -> Unit
) {
    BasicDialog(
        backHandler = onDismissRequest
    ) {
        Text(
            text = "후기 작성을 중단하고 나가시겠어요?",
            style = typography.titleSmall,
            color = colorScheme.text.primary
        )
        Text(
            text = "지금 작성한 후기는 저장되지 않아요.",
            style = typography.bodySmall,
            color = colorScheme.text.caption
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicButton(
                modifier = Modifier.weight(1f),
                text = "취소",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.DEFAULT,
                radius = 28.dp,
                onClicked = onDismissRequest
            )

            BasicButton(
                modifier = Modifier.weight(1f),
                text = "나가기",
                size = BasicButtonSize.L,
                buttonType = BasicButtonType.DEFAULT,
                radius = 28.dp,
                onClicked = onExitButtonClicked
            )
        }
    }
}

@Composable
private fun Step1HospitalContent(
    state: Step1State,
    intent: (ReviewCreateIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "병원명",
                style = typography.titleSmall,
                color = colorScheme.text.secondary,
            )
            ReviewInputTextField(
                queryString = state.hospitalInfo?.name ?: "",
                placeholder = "병원명을 입력해주세요.",
                onQueryStringChanged = { name ->
                    intent(ReviewCreateIntent.OnHospitalSelected(HospitalInfo(id = 0, name = name)))
                },
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXL),
        ) {
            Text(
                text = "솔직한 리뷰를 남겨주세요",
                style = typography.titleMedium.emp(),
                color = colorScheme.text.secondary
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Step1RatingItems(
                    title = "전문성",
                    desc = "증상과 치료에 대해 자세히 설명했나요?",
                    currentRating = state.ratings.expertise,
                    onRatingChanged = { newRating ->
                        intent(ReviewCreateIntent.OnRatingChanged(state.ratings.copy(expertise = newRating)))
                    }
                )
                Step1RatingItems(
                    title = "친절도",
                    desc = "접수/수납 과정에서 충분한 안내를 받았나요?",
                    currentRating = state.ratings.kindness,
                    onRatingChanged = { newRating ->
                        intent(ReviewCreateIntent.OnRatingChanged(state.ratings.copy(kindness = newRating)))
                    }
                )
                Step1RatingItems(
                    title = "시설/환경",
                    desc = "진료실과 병원 시설이 위생적이었나요?",
                    currentRating = state.ratings.facility,
                    onRatingChanged = { newRating ->
                        intent(ReviewCreateIntent.OnRatingChanged(state.ratings.copy(facility = newRating)))
                    }
                )
            }
        }
    }
}

@Composable
private fun Step1RatingItems(
    title: String,
    desc: String,
    currentRating: Double,
    onRatingChanged: (Double) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = typography.titleSmall.emp(),
                color = colorScheme.text.secondary,
            )
            Text(
                text = desc,
                style = typography.bodySmall,
                color = colorScheme.text.caption
            )
        }

        RatingBar(
            rating = currentRating,
            onRatingChanged = onRatingChanged
        )
    }
}

@Composable
private fun Step2AnimalContent(
    state: Step2State,
    intent: (ReviewCreateIntent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "동물종",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .background(
                        color = colorScheme.bg.frame.default,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colorScheme.border.verySubtle,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val selectedCategory =
                        AnimalCategory.entries.find { it.name == state.animalType }
                    val text = selectedCategory?.korean ?: "동물종을 선택해주세요."
                    val textColor =
                        if (selectedCategory != null) colorScheme.text.secondary else colorScheme.text.disabled

                    Text(
                        text = text,
                        style = typography.bodyLarge,
                        color = textColor
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.KeyboardArrowDown),
                        contentDescription = "Expand",
                        size = iconSizeMS,
                        tint = colorScheme.icon.dark
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(colorScheme.bg.frame.default)
                ) {
                    AnimalCategory.entries.filter { it != AnimalCategory.ALL }.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category.korean,
                                    style = typography.bodyMedium,
                                    color = colorScheme.text.secondary
                                )
                            },
                            onClick = {
                                intent(ReviewCreateIntent.OnAnimalTypeChanged(category.name))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // 세부 동물명 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "세부 동물명",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                queryString = state.detailAnimalType,
                placeholder = "예: 골든햄스터",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnDetailAnimalTypeChanged(it)) },
            )
        }

        // 진료명 입력
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "진료명",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                queryString = state.treatment,
                placeholder = "예: 골절, 발톱정리",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnTreatmentChanged(it)) },
            )
        }
    }
}

@Composable
private fun Step3ReviewContent(
    state: Step3State,
    intent: (ReviewCreateIntent) -> Unit
) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris ->
        val currentImages = state.images
        val newImages = uris.map { it.toString() }

        val combinedImages = (currentImages + newImages).take(10)
        intent(ReviewCreateIntent.OnImagesChanged(combinedImages))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier.padding(vertical = spacingXL, horizontal = spacingMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .size(72.dp)
                    .background(colorScheme.bg.frame.default, RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = colorScheme.border.verySubtle
                    )
                    .clickable {
                        if (state.images.size < 10) {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        } else {
                            // TODO: Show toast "최대 10장까지만 첨부 가능합니다."
                        }
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.CameraAlt),
                    contentDescription = "Add image",
                    size = iconSizeLarge,
                    tint = colorScheme.icon.light
                )
                Text(
                    text = "${state.images.size}/10",
                    color = colorScheme.icon.light,
                    style = typography.labelMedium
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(state.images) { index, uri ->
                    ReviewImageItem(
                        uri = uri,
                        index = index + 1,
                        onDelete = {
                            val newList = state.images.toMutableList().apply { removeAt(index) }
                            intent(ReviewCreateIntent.OnImagesChanged(newList))
                        }
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS)
        ) {
            Text(
                text = "후기 내용",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )
            ReviewInputTextField(
                modifier = Modifier.height(200.dp),
                queryString = state.content,
                placeholder = "자세한 진료 및 치료 과정을 작성해주세요.",
                onQueryStringChanged = { intent(ReviewCreateIntent.OnContentChanged(it)) },
                singleLine = false
            )
        }
    }
}

@Composable
private fun ReviewImageItem(
    uri: String,
    index: Int,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier.size(72.dp)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, colorScheme.border.verySubtle, RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(topStart = 8.dp, bottomEnd = 6.dp)
                )
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(
                text = "$index",
                style = typography.labelSmall,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(18.dp)
                .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete Image",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
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
                    currentStep = ReviewCreateStep.HOSPITAL_AND_RATING,
                    step1 = Step1State(
                        hospitalInfo = HospitalInfo(1, "행복 동물병원"),
                        ratings = ReviewRating(4.5, 5.0, 4.0)
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
                    currentStep = ReviewCreateStep.ANIMAL_AND_TREATMENT,
                    step2 = Step2State(
                        animalType = "강아지",
                        detailAnimalType = "말티즈",
                        treatment = "슬개골 탈구 수술"
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
private fun ReviewCreateScreenStep3Preview() {
    PetbulanceTheme {
        ReviewCreateScreen(
            navController = rememberNavController(),
            argument = ReviewCreateArgument(
                state = ReviewCreateState(
                    currentStep = ReviewCreateStep.REVIEW_CONTENT,
                    step3 = Step3State(
                        content = "선생님이 정말 친절하시고 설명도 잘 해주셨어요. 수술 경과도 좋아서 만족합니다."
                    )
                ),
                intent = {},
                event = MutableSharedFlow()
            )
        )
    }
}