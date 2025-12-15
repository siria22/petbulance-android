package com.example.presentation.screen.feature.search.views.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospital.recent.ContentAsString
import com.example.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.example.domain.model.feature.hospital.recent.ViewedHospital
import com.example.domain.model.feature.hospital.recent.ViewedHospitalList
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXXS
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
import com.example.presentation.screen.feature.search.views.common.RowChipFilters
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
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

    val recentSearchKeywords = hospitalSearchData.recentSearchKeywords

    Scaffold(
        topBar = {
            SearchBar(
                queryString = currentQuery.query ?: "",
                onQueryStringChanged = { currentQuery = currentQuery.copy(query = it) },
                onMoveBackIconClicked = {
                    commonSearchArgument.intent(
                        SearchIntent.ChangeScreenState(
                            SearchScreenState.Hospitals.MapView
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
            SearchViewContents(
                onFilterButtonClicked = {
                    currentSelectedFilterBottomSheet = it
                    isFilterBottomSheetVisible = true
                },
                recentQueryList = recentSearchKeywords,
                recentViewedHospital = hospitalSearchData.viewedHospitals,
                onChipClicked = { query ->
                    currentQuery = currentQuery.copy(query = query)
                },
                onDeleteRecentKeyword = { keyword ->
                    hospitalSearchArgument.intent(HospitalSearchIntent.DeleteRecentKeyword(keyword))
                },
                onDeleteRecentViewedHospital = {
                    hospitalSearchArgument.intent(HospitalSearchIntent.DeleteViewedHospital(it.hospitalId))
                },
            )
        }
    }

    FilterBottomSheet(
        currentQuery = hospitalSearchData.hospitalSearchQuery,
        startTab = currentSelectedFilterBottomSheet,
        showBottomSheet = isFilterBottomSheetVisible,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        onDismissRequest = { isFilterBottomSheetVisible = false },
        onQuerySet = {
            val originalQueryString = currentQuery.query
            currentQuery = it.copy(query = originalQueryString)
        },
    )

    BackHandler {
        commonSearchArgument.intent(
            SearchIntent.ChangeScreenState(
                SearchScreenState.Hospitals.MapView
            )
        )
    }
}

@Composable
private fun SearchViewContents(
    recentQueryList: List<RecentSearchKeyword>,
    recentViewedHospital: ViewedHospitalList,
    onFilterButtonClicked: (FilterBottomSheetTab) -> Unit,
    onChipClicked: (String) -> Unit,
    onDeleteRecentKeyword: (String) -> Unit,
    onDeleteRecentViewedHospital: (ViewedHospital) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = spacingMedium)
    ) {
        RowChipFilters(onFilterButtonClicked = onFilterButtonClicked)

        RecentHistorySection(
            title = "최근 검색어",
            items = recentQueryList,
            onItemClick = { onChipClicked(it.keyword) },
            onDeleteClick = { onDeleteRecentKeyword(it.keyword) }
        )

        RecentHistorySection(
            title = "최근 본 병원",
            items = recentViewedHospital.items,
            onItemClick = { onChipClicked(it.hospitalName) },
            onDeleteClick = { onDeleteRecentViewedHospital(it) }
        )
    }
}

@Composable
private fun <T : ContentAsString> RecentHistorySection(
    title: String,
    items: List<T>,
    onItemClick: (T) -> Unit,
    onDeleteClick: (T) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXXS),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = spacingXS,
                bottom = spacingMedium
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.emp(),
            color = colorScheme.text.primary,
            modifier = Modifier.padding(vertical = spacingXS)
        )

        if (items.isEmpty()) {
            Text(
                text = "검색 시 자동으로 검색어가 저장돼요",
                style = MaterialTheme.typography.bodySmall.emp(),
                color = colorScheme.text.caption,
            )
        } else {
            items.forEach { item ->
                HistoryChip(
                    content = item,
                    onChipClicked = { onItemClick(item) },
                    onDeleteIconClicked = { onDeleteClick(item) }
                )
            }
        }
    }
}

@Composable
private fun <T : ContentAsString> HistoryChip(
    content: T,
    onChipClicked: (T) -> Unit,
    onDeleteIconClicked: (T) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.medium,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                vertical = spacingXXXS,
                horizontal = spacingXS
            )
    ) {
        Text(
            text = content.getContentAsString(),
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.text.secondary,
            modifier = Modifier.clickable {
                onChipClicked(content)
            }
        )
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Clear),
            contentDescription = "Delete history",
            size = iconSizeMs,
            modifier = Modifier.clickable {
                onDeleteIconClicked(content)
            }
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun SearchViewPreview() {
    PetbulanceTheme {
        SearchView(
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