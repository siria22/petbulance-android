package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.Region
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeLarge
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXL
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentQuery: HospitalSearchQueryUiModel,
    startTab: FilterBottomSheetTab,
    showBottomSheet: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onQuerySet: (HospitalSearchQueryUiModel) -> Unit,
    onResetFilterClicked: () -> Unit,
    onSearchButtonClicked: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(startTab) }

    LaunchedEffect(showBottomSheet, startTab) {
        if (showBottomSheet) {
            selectedTab = startTab
        }
    }

    var selectedRegion by remember {
        mutableStateOf(currentQuery.region ?: Region.ALL)
    }
    var selectedDistrict by remember {
        mutableStateOf(
            if (currentQuery.region == null) "전체" else currentQuery.district
        )
    }
    var selectedAnimalCategory by remember { mutableStateOf(currentQuery.animalCategories) }

    LaunchedEffect(showBottomSheet, currentQuery) {
        if (showBottomSheet) {
            val region = currentQuery.region ?: Region.ALL
            selectedRegion = region

            selectedDistrict =
                if (region == Region.ALL) "전체"
                else currentQuery.district

            selectedAnimalCategory = currentQuery.animalCategories
        }
    }

    BasicBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Box {
            Column(
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .padding(horizontal = spacingMedium)
                ) {
                    FilterBottomSheetTab.entries.forEach { elem ->
                        FilterBottomSheetTab(
                            modifier = Modifier.weight(1f),
                            tab = elem,
                            isSelected = selectedTab == elem,
                            onTabSelected = { selectedTab = it }
                        )
                    }
                }

                CommonDivider()

                ResetFilterRow(
                    onResetFilterClicked = {
                        selectedRegion = Region.ALL
                        selectedDistrict = "전체"
                        selectedAnimalCategory = emptyList()
                        onQuerySet(
                            currentQuery.copy(
                                region = Region.ALL,
                                district = "전체",
                                animalCategories = emptyList()
                            )
                        )
                    }
                )

                CommonDivider()

                Column(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        FilterBottomSheetTab.REGION -> {
                            RegionSelectColumn(
                                modifier = Modifier.fillMaxSize(),
                                selectedRegion = selectedRegion,
                                selectedDistrict = selectedDistrict,
                                onRegionSelected = { selectedRegion = it ?: Region.ALL },
                                onDistrictSelected = { selectedDistrict = it },
                                onQuerySet = {
                                    onQuerySet(
                                        currentQuery.copy(
                                            region = selectedRegion,
                                            district = selectedDistrict,
                                            animalCategories = selectedAnimalCategory
                                        )
                                    )
                                },
                            )
                        }

                        FilterBottomSheetTab.SPECIES -> {
                            SpeciesSelectColumn(
                                selectedAnimalCategories = selectedAnimalCategory,
                                onCategoriesChanged = { newCategories ->
                                    selectedAnimalCategory = newCategories
                                    onQuerySet(currentQuery.copy(animalCategories = newCategories))
                                }
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = spacingMedium, vertical = spacingXXL)
                    .align(Alignment.BottomCenter)
            ) {
                BasicButton(
                    modifier = Modifier.weight(2f),
                    leadingIcon = IconResource.Vector(Icons.Default.LocationSearching),
                    leadingIconSize = iconSizeLarge,
                    text = "내 위치",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 16.dp,
                    onClicked = { /* TODO : Find my location and change into region/district */ }
                )
                BasicButton(
                    modifier = Modifier.weight(3f),
                    leadingIcon = null,
                    text = "병원 보기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 16.dp,
                    onClicked = onSearchButtonClicked
                )
            }
        }
    }
}

@Composable
private fun FilterBottomSheetTab(
    modifier: Modifier,
    tab: FilterBottomSheetTab,
    isSelected: Boolean,
    onTabSelected: (FilterBottomSheetTab) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable {
                onTabSelected(tab)
            }
    ) {
        Text(
            text = tab.toKorean(),
            style = MaterialTheme.typography.bodySmall.emp(),
            color = if (isSelected) colorScheme.text.primary else colorScheme.text.disabled,
            modifier = Modifier.padding(vertical = spacingXXS),
        )
        if (isSelected) {
            HorizontalDivider(
                thickness = 2.dp,
                color = colorScheme.border.active
            )
        }
    }
}

@Composable
private fun ResetFilterRow(onResetFilterClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = spacingXS,
                horizontal = spacingLarge
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onResetFilterClicked()
            }
        ) {
            Text(
                text = "선택 초기화",
                color = colorScheme.text.tertiary,
                style = MaterialTheme.typography.labelLarge
            )
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.Refresh),
                contentDescription = "Clear filter",
                size = iconSizeSmall,
                tint = colorScheme.icon.medium
            )
        }
    }
}

@Composable
private fun RegionSelectColumn(
    modifier: Modifier = Modifier,
    selectedRegion: Region?,
    selectedDistrict: String?,
    onRegionSelected: (Region?) -> Unit,
    onDistrictSelected: (String?) -> Unit,
    onQuerySet: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(color = colorScheme.bg.frame.subtle)
        ) {
            items(Region.entries) { region ->
                RegionCategoryItem(
                    region = region,
                    isSelected = selectedRegion == region,
                    onClick = {
                        onRegionSelected(region)
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(2.2f)
                .fillMaxHeight()
                .background(colorScheme.bg.frame.default)
        ) {
            val districts = selectedRegion?.districts.orEmpty()

            val wholeOption = districts.firstOrNull()
            val isWholeSelected = wholeOption != null && selectedDistrict == wholeOption

            items(districts) { district ->
                RegionDetailItem(
                    districtName = district,
                    isSelected = if (isWholeSelected) true else (selectedDistrict == district),
                    onClick = {
                        onDistrictSelected(district)
                        onQuerySet("${selectedRegion?.displayName ?: "전체"}, ${selectedDistrict ?: ""}")
                    }
                )
            }
        }
    }
}

@Composable
private fun RegionCategoryItem(
    region: Region,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) colorScheme.bg.frame.default else colorScheme.bg.frame.subtle)
            .clickable(onClick = onClick)
            .padding(horizontal = spacingLarge, vertical = spacingSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region.displayName,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W500
            ),
            color = if (isSelected) colorScheme.text.primary else colorScheme.text.caption
        )
    }
}

@Composable
private fun RegionDetailItem(
    districtName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Text(
            text = districtName,
            style = MaterialTheme.typography.bodyMedium,
            color = if (!isSelected) colorScheme.text.primary else colorScheme.action.primary.default,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingLarge, vertical = spacingSmall)
        )

        CommonDivider()
    }
}

@Composable
private fun SpeciesSelectColumn(
    selectedAnimalCategories: List<AnimalCategory>,
    onCategoriesChanged: (List<AnimalCategory>) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXL),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = spacingXXL, horizontal = spacingXL)
    ) {
        AnimalCategory.entries.forEach { animalCategory ->
            val isSelected = selectedAnimalCategories.contains(animalCategory)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        val newList = if (animalCategory == AnimalCategory.ALL) {
                            emptyList()
                        } else {
                            if (isSelected) {
                                selectedAnimalCategories - animalCategory
                            } else {
                                selectedAnimalCategories + animalCategory
                            }
                        }
                        onCategoriesChanged(newList)
                    })
            ) {
                Text(
                    text = animalCategory.korean,
                    style = MaterialTheme.typography.bodyLarge.emp(),
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview(apiLevel = 34)
@Composable
private fun FilterBottomSheetPreview() {
    PetbulanceTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            FilterBottomSheet(
                showBottomSheet = true,
                sheetState = rememberModalBottomSheetState(),
                onDismissRequest = {},
                startTab = FilterBottomSheetTab.SPECIES,
                onQuerySet = {},
                currentQuery = HospitalSearchQueryUiModel.empty,
                onResetFilterClicked = {},
                onSearchButtonClicked = {},
            )
        }
    }
}


