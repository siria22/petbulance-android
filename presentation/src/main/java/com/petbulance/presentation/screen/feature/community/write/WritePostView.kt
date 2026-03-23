package com.petbulance.presentation.screen.feature.community.write

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.PostCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicDialog
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeLarge
import com.petbulance.presentation.component.ui.iconSizeMS
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.screen.feature.review.common.ReviewInputTextField
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun WritePostView(
    navController: NavController,
    argument: WritePostArgument,
    data: WritePostData,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAnimalTypeDialog by remember { 
        mutableStateOf(data.mode == WritePostData.WritePostMode.CREATE && data.selectedAnimalType == null) 
    }
    var tempSelectedAnimalType by remember { mutableStateOf<AnimalCategory?>(null) }

    Scaffold(
        topBar = {
            WritePostTopBar(
                selectedCategory = data.selectedAnimalType ?: "소형포유류",
                onCategorySelected = { category ->
                    argument.intent(WritePostIntent.SelectAnimalType(category))
                },
                onClose = {
                    navController.safePopBackStack()
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingXL, vertical = spacingXXL)
            ) {
                BasicButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "작성 완료",
                    size = BasicButtonSize.L,
                    buttonType = if (data.isSubmitEnabled) BasicButtonType.PRIMARY else BasicButtonType.DISABLED,
                    radius = 16.dp,
                ) {
                    if (data.isSubmitEnabled) {
                        onSubmit()
                    }
                }
            }
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            WritePostViewContents(
                data = data,
                onIntent = argument.intent
            )
        }
    }

    if (showAnimalTypeDialog) {
        AnimalTypeSelectionDialog(
            selectedAnimalType = tempSelectedAnimalType,
            onAnimalTypeSelected = { tempSelectedAnimalType = it },
            onConfirm = {
                tempSelectedAnimalType?.let {
                    argument.intent(WritePostIntent.SelectAnimalType(it.name))
                    showAnimalTypeDialog = false
                }
            }
        )
    }
}

@Composable
private fun WritePostViewContents(
    data: WritePostData,
    onIntent: (WritePostIntent) -> Unit
) {
    var topicExpanded by remember { mutableStateOf(false) }
    var topicInputWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        uris.forEach { uri ->
            onIntent(WritePostIntent.AddImage(uri))
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = spacingMedium, horizontal = spacingXL)
    ) {
        // 1. 주제 선택 섹션 (드롭다운)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .onGloballyPositioned { coordinates ->
                    topicInputWidth = with(density) { coordinates.size.width.toDp() }
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { topicExpanded = true }
            ) {
                Text(
                    text = data.selectedTopic?.let { topic ->
                        PostCategory.entries.find { it.name == topic }?.korean
                    } ?: "주제를 선택해주세요",
                    style = typography.bodySmall,
                    color = colorScheme.text.primary
                )

                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.KeyboardArrowDown),
                    contentDescription = "Open Dropdown Menu",
                    size = iconSizeMS,
                    tint = colorScheme.icon.basic
                )
            }

            DropdownMenu(
                expanded = topicExpanded,
                onDismissRequest = { topicExpanded = false },
                modifier = Modifier
                    .width(topicInputWidth)
                    .background(colorScheme.bg.frame.default)
            ) {
                PostCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.korean,
                                style = typography.bodyMedium,
                                color = colorScheme.text.secondary
                            )
                        },
                        onClick = {
                            onIntent(WritePostIntent.SelectTopic(category.name))
                            topicExpanded = false
                        }
                    )
                }
            }
        }

        // 2. 제목 입력 섹션
        Column(verticalArrangement = Arrangement.spacedBy(spacingXS)) {
            Text(
                text = "제목",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )

            ReviewInputTextField(
                queryString = data.title,
                placeholder = "글 제목",
                onQueryStringChanged = { onIntent(WritePostIntent.UpdateTitle(it)) },
                trailingIcon = if (data.title.isNotBlank()) {
                    {
                        BasicIcon(
                            iconResource = IconResource.Vector(Icons.Default.Close),
                            contentDescription = "Clear",
                            size = 20.dp,
                            tint = colorScheme.icon.medium,
                            modifier = Modifier.clickable { onIntent(WritePostIntent.UpdateTitle("")) }
                        )
                    }
                } else null
            )
        }

        // 3. 사진 첨부 섹션
        PostImageSection(
            images = data.existingImageUrls + data.newImageUris.map { it.toString() },
            onImageAddClicked = {
                val remainingSlots = 10 - data.totalImageCount
                if (remainingSlots > 0) {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            },
            onImageDeleteClicked = { index ->
                onIntent(WritePostIntent.RemoveImage(index))
            }
        )

        // 4. 내용 입력 섹션
        Column(verticalArrangement = Arrangement.spacedBy(spacingXS)) {
            Text(
                text = "내용",
                style = typography.bodyMedium,
                color = colorScheme.text.secondary
            )

            ReviewInputTextField(
                modifier = Modifier.height(200.dp),
                queryString = data.content,
                placeholder = "반려동물 자랑글 혹은 케어 방법 질문 등을 자성해보세요.",
                onQueryStringChanged = { onIntent(WritePostIntent.UpdateContent(it)) },
                singleLine = false
            )
        }
    }
}

@Composable
private fun PostImageSection(
    images: List<String>,
    onImageAddClicked: () -> Unit,
    onImageDeleteClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        if (images.size < 10) {
            Column(
                modifier = Modifier
                    .size(72.dp)
                    .background(colorScheme.bg.frame.default, RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = colorScheme.border.verySubtle
                    )
                    .clickable { onImageAddClicked() },
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
                    text = "${images.size}/10",
                    color = colorScheme.icon.light,
                    style = typography.labelMedium
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(spacingMedium),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(images) { index, uri ->
                PostImageItem(
                    uri = uri,
                    index = index + 1,
                    onDelete = { onImageDeleteClicked(index) }
                )
            }
        }
    }
}

@Composable
private fun PostImageItem(
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

@Composable
private fun AnimalTypeSelectionDialog(
    selectedAnimalType: AnimalCategory?,
    onAnimalTypeSelected: (AnimalCategory) -> Unit,
    onConfirm: () -> Unit?
) {
    BasicDialog {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXL),
        ) {
            Text(
                text = "작성할 라운지를 선택해주세요",
                style = typography.titleSmall.emp(),
                color = colorScheme.text.primary
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXS)
            ) {
                AnimalCategory.entries.filter { it != AnimalCategory.ALL }.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAnimalTypeSelected(category)
                                onConfirm()
                            }
                            .padding(vertical = spacingXS),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacingXS)
                    ) {
                        Text(
                            text = category.korean,
                            style = typography.bodySmall.emp(),
                            color = colorScheme.text.tertiary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WritePostViewPreview() {
    PetbulanceTheme {
        WritePostView(
            navController = rememberNavController(),
            argument = WritePostArgument(
                intent = {},
                dataState = WritePostDataState.Idle,
                screenState = WritePostScreenState.Idle,
                event = MutableSharedFlow()
            ),
            data = WritePostData(
                mode = WritePostData.WritePostMode.CREATE,
                selectedAnimalType = null,
                selectedTopic = null,
                title = "",
                content = "",
                newImageUris = emptyList(),
                existingImageUrls = emptyList(),
                deletedImageUrls = emptyList(),
                isSubmitting = false
            ),
            onSubmit = {}
        )
    }
}
