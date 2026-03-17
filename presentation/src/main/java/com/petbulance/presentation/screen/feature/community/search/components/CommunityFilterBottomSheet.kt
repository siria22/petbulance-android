package com.petbulance.presentation.screen.feature.community.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.PostCategory
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.spacingXXS

enum class FilterTab(val korean: String) {
    LOUNGE("라운지 선택"),
    CATEGORY("카테고리 선택")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityFilterBottomSheet(
    selectedAnimalCategory: String?,
    selectedPostCategory: String?,
    onApply: (animalCategory: String?, postCategory: String?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var currentTab by remember { mutableStateOf(FilterTab.LOUNGE) }
    var tempAnimalCategory by remember { mutableStateOf(selectedAnimalCategory) }
    var tempPostCategory by remember { mutableStateOf(selectedPostCategory) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colorScheme.bg.frame.default,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // 탭
            TabRow(
                selectedTabIndex = currentTab.ordinal,
                containerColor = colorScheme.bg.frame.default,
                contentColor = colorScheme.text.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
                        color = colorScheme.text.primary
                    )
                }
            ) {
                FilterTab.entries.forEach { tab ->
                    Tab(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        text = {
                            Text(
                                text = tab.korean,
                                style = typography.bodySmall.emp(),
                                color = if (currentTab == tab) {
                                    colorScheme.text.primary
                                } else {
                                    colorScheme.text.tertiary
                                }
                            )
                        }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXS, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingLarge)
                    .clickable {
                        tempAnimalCategory = null
                        tempPostCategory = null
                    }
            ) {
                Text(
                    text = "선택 초기화",
                    color = colorScheme.text.tertiary,
                    style = typography.labelLarge
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Refresh),
                    contentDescription = "Clear filter",
                    size = iconSizeSmall,
                    tint = colorScheme.icon.medium
                )
            }

            // 컨텐츠 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                when (currentTab) {
                    FilterTab.LOUNGE -> {
                        SpeciesSelectColumn(
                            selectedAnimalCategory = tempAnimalCategory,
                            onCategoryChanged = { tempAnimalCategory = it }
                        )
                    }

                    FilterTab.CATEGORY -> {
                        CategorySelectColumn(
                            selectedPostCategory = tempPostCategory,
                            onCategoryChanged = { tempPostCategory = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacingMedium))

            BasicButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacingXL),
                text = "검색",
                size = BasicButtonSize.XL,
                buttonType = BasicButtonType.PRIMARY,
                radius = 16.dp
            ) {
                onApply(tempAnimalCategory, tempPostCategory)
            }
        }
    }
}

@Composable
private fun SpeciesSelectColumn(
    selectedAnimalCategory: String?,
    onCategoryChanged: (String?) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = spacingXXL, horizontal = spacingXL)
    ) {
        AnimalCategory.entries.forEach { animalCategory ->
            val isSelected = if (animalCategory == AnimalCategory.ALL) {
                selectedAnimalCategory == null
            } else {
                selectedAnimalCategory == animalCategory.name
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        onCategoryChanged(
                            if (animalCategory == AnimalCategory.ALL) {
                                null
                            } else {
                                animalCategory.name
                            }
                        )
                    })
            ) {
                Text(
                    text = animalCategory.korean,
                    style = typography.bodyLarge.emp(),
                    color = colorScheme.text.secondary,
                )
                if (isSelected) {
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.ic_bottomsheet_checked),
                        contentDescription = "Selected animal category",
                        size = iconSizeSmall,
                        tint = colorScheme.action.primary.default
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySelectColumn(
    selectedPostCategory: String?,
    onCategoryChanged: (String?) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = spacingXXL, horizontal = spacingXL)
    ) {
        // "전체" 옵션
        val isAllSelected = selectedPostCategory == null
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onCategoryChanged(null) })
        ) {
            Text(
                text = "전체",
                style = typography.bodyLarge.emp(),
                color = colorScheme.text.secondary,
            )
            if (isAllSelected) {
                BasicIcon(
                    iconResource = IconResource.Drawable(R.drawable.ic_bottomsheet_checked),
                    contentDescription = "Selected category",
                    size = iconSizeSmall,
                    tint = colorScheme.action.primary.default
                )
            }
        }

        // 카테고리 목록
        PostCategory.entries.forEach { category ->
            val isSelected = selectedPostCategory == category.name

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onCategoryChanged(category.name) })
            ) {
                Text(
                    text = category.korean,
                    style = typography.bodyLarge.emp(),
                    color = colorScheme.text.secondary,
                )
                if (isSelected) {
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.ic_bottomsheet_checked),
                        contentDescription = "Selected category",
                        size = iconSizeSmall,
                        tint = colorScheme.action.primary.default
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommunityFilterBottomSheetPreview() {
    PetbulanceTheme {
        CommunityFilterBottomSheet(
            selectedAnimalCategory = "SMALLMAMMALS",
            selectedPostCategory = "HEALTH",
            onApply = { _, _ -> },
            onDismiss = {}
        )
    }
}
