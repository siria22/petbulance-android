@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.screen.feature.search.views.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospital.hospital.Hospital
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.screen.feature.search.CommonSearchArgument
import com.example.presentation.screen.feature.search.HospitalSearchArgument
import com.example.presentation.screen.feature.search.HospitalSearchData
import com.example.presentation.screen.feature.search.HospitalSearchDataState
import com.example.presentation.screen.feature.search.HospitalSearchIntent
import com.example.presentation.screen.feature.search.SearchIntent
import com.example.presentation.screen.feature.search.SearchScreenState
import com.example.presentation.screen.feature.search.UserLocationArgument
import com.example.presentation.screen.feature.search.UserLocationData
import com.example.presentation.screen.feature.search.UserLocationState
import com.example.presentation.screen.feature.search.views.common.HospitalCard
import com.example.presentation.screen.feature.search.views.common.HospitalSortType
import com.example.presentation.screen.feature.search.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.views.common.RowResultControlChips
import com.example.presentation.screen.feature.search.views.search.SearchBar
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun ResultView(
    navController: NavController,
    commonSearchArgument: CommonSearchArgument,
    userLocationArgument: UserLocationArgument,
    hospitalSearchArgument: HospitalSearchArgument,
    locationData: UserLocationData,
    hospitalSearchData: HospitalSearchData
) {
    var isFilterBottomSheetVisible by remember { mutableStateOf(false) }
    var currentSelectedFilterBottomSheet by remember { mutableStateOf(FilterBottomSheetTab.REGION) }
    var currentQuery by remember { mutableStateOf(hospitalSearchData.hospitalSearchQuery) }

    var selectedSortType by remember { mutableStateOf(HospitalSortType.DISTANCE) }
    var isOpenNowOnly by remember { mutableStateOf(false) }

    var isSelectSortTypeDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SearchBar(
                queryString = currentQuery.query ?: "",
                onQueryStringChanged = { /* nop */ },
                onMoveBackIconClicked = {
                    commonSearchArgument.intent(
                        SearchIntent.ChangeScreenState(
                            SearchScreenState.OnSearch.SearchView
                        )
                    )
                },
                onSearchButtonClicked = {
                    hospitalSearchArgument.intent(
                        HospitalSearchIntent.OnQueryChanged(
                            query = currentQuery,
                            currentUserLocation = locationData.currentUserLocation
                        )
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.SEARCH,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ResultViewContents(
                hospitalList = hospitalSearchData.hospitalList,
                onFilterButtonClicked = { tab ->
                    currentSelectedFilterBottomSheet = tab
                    isFilterBottomSheetVisible = true
                },
                selectedSortType = selectedSortType,
                isOpenNowOnly = isOpenNowOnly,
                onSortTypeClicked = { isSelectSortTypeDialogVisible = true },
                onOpenNowOnlyClicked = { isOpenNowOnly = !isOpenNowOnly }
            )
        }
    }

    FilterBottomSheet(
        currentQuery = currentQuery,
        startTab = currentSelectedFilterBottomSheet,
        showBottomSheet = isFilterBottomSheetVisible,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = { isFilterBottomSheetVisible = false },
        onQuerySet = { updatedQuery ->
            val originalQueryString = currentQuery.query
            currentQuery = updatedQuery.copy(query = originalQueryString)
        },
    )

    if (isSelectSortTypeDialogVisible) {
        SelectSortTypeDialog(
            onDismissRequest = { isSelectSortTypeDialogVisible = false },
            onSortTypeSelected = { selectedSortType = it }
        )
    }

    BackHandler {
        commonSearchArgument.intent(
            SearchIntent.ChangeScreenState(
                SearchScreenState.OnSearch.SearchView
            )
        )
    }
}

@Composable
private fun ResultViewContents(
    hospitalList: List<Hospital>,
    onFilterButtonClicked: (FilterBottomSheetTab) -> Unit,
    selectedSortType: HospitalSortType,
    isOpenNowOnly: Boolean,
    onSortTypeClicked: () -> Unit,
    onOpenNowOnlyClicked: (Boolean) -> Unit
) {
    val filteredHospitalList = remember(hospitalList, isOpenNowOnly, selectedSortType) {
        hospitalList
            .asSequence()
            .filter { if (isOpenNowOnly) it.isOpenNow else true }
            .sortedByDescending {
                when (selectedSortType) {
                    HospitalSortType.DISTANCE -> it.distanceMeters
                    HospitalSortType.REVIEW -> it.reviewCount?.toDouble()
                    HospitalSortType.RATING -> it.rating
                }
            }
            .toList()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(spacingXS),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = spacingMedium)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RowChipFilters(onFilterButtonClicked = onFilterButtonClicked)
                RowResultControlChips(
                    selectedSortType = selectedSortType,
                    isOpenNowOnly = isOpenNowOnly,
                    onSortTypeClicked = onSortTypeClicked,
                    onOpenNowOnlyClicked = onOpenNowOnlyClicked
                )
            }
        }

        if (filteredHospitalList.isNotEmpty()) {
            items(hospitalList) { hospital ->
                HospitalCard(hospital = hospital)
            }
        } else {
            item {
                NoResult()
            }
        }
    }
}

@Composable
private fun NoResult() {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = spacingXL)
    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.img_no_result),
            contentDescription = "No result",
            size = 160.dp,
            tint = Color.Unspecified
        )
        Text(
            text = "주변 병원을 찾을 수 없어요.",
            style = typography.titleSmall,
            color = colorScheme.text.tertiary
        )
        Text(
            text = "더 넓은 지역에서 검색해주세요.",
            style = typography.bodySmall,
            color = colorScheme.text.tertiary
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ResultViewPreview() {
    PetbulanceTheme {
        ResultView(
            navController = rememberNavController(),
            commonSearchArgument = CommonSearchArgument(
                screenState = SearchScreenState.OnSearch.SearchView,
                event = MutableSharedFlow(),
                intent = { }
            ),
            userLocationArgument = UserLocationArgument(
                intent = { },
                locationState = UserLocationState.Init
            ),
            hospitalSearchArgument = HospitalSearchArgument(
                intent = { },
                hospitalDataState = HospitalSearchDataState.Init
            ),
            locationData = UserLocationData.empty,
            hospitalSearchData = HospitalSearchData.empty
        )
    }
}