package com.example.presentation.screen.feature.search.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.type.HospitalSortType
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.screen.feature.search.main.views.list.ListView
import com.example.presentation.screen.feature.search.main.views.map.MapView
import com.example.presentation.screen.feature.search.main.views.result.ResultView
import com.example.presentation.screen.feature.search.main.views.result.SelectSortTypeDialog
import com.example.presentation.screen.feature.search.main.views.search.SearchView
import com.example.presentation.utils.error.collectCustomErrors
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    commonSearchArgument: CommonSearchArgument,
    userLocationArgument: UserLocationArgument,
    hospitalSearchArgument: HospitalSearchArgument,
    locationData: UserLocationData,
    hospitalSearchData: HospitalSearchData
) {
    val screenState = commonSearchArgument.screenState

    // --- Hoisted State ---
    var isFilterBottomSheetVisible by remember { mutableStateOf(false) }
    var currentSelectedFilterBottomSheet by remember { mutableStateOf(FilterBottomSheetTab.REGION) }
    var selectedSortType by remember { mutableStateOf(HospitalSortType.DISTANCE) }
    var isOpenNowOnly by remember { mutableStateOf(false) }
    var isSelectSortTypeDialogVisible by remember { mutableStateOf(false) }

    // BottomSheet 등에서 수정 중인 쿼리 상태 (검색 확정 전)
    var currentDraftQuery by remember(hospitalSearchData.hospitalSearchQuery) {
        mutableStateOf(hospitalSearchData.hospitalSearchQuery)
    }

    // --- UI State ---
    val searchUiState = SearchUiState(
        hospitalList = hospitalSearchData.hospitalList,
        currentQuery = currentDraftQuery,
        selectedSortType = selectedSortType,
        isOpenNowOnly = isOpenNowOnly,
        isFilterBottomSheetVisible = isFilterBottomSheetVisible,
        currentSelectedFilterBottomSheet = currentSelectedFilterBottomSheet,
        isSelectSortTypeDialogVisible = isSelectSortTypeDialogVisible,
        currentUserLocation = locationData.currentUserLocation
    )

    LaunchedEffect(Unit) {
        userLocationArgument.intent(UserLocationIntent.RequestLocation)
    }

    // --- Event Handler ---
    val onEvent: (SearchUiEvent) -> Unit = { event ->
        when (event) {
            // UI State Changes
            is SearchUiEvent.OnFilterButtonClicked -> {
                currentSelectedFilterBottomSheet = event.tab
                isFilterBottomSheetVisible = true
            }

            is SearchUiEvent.OnSortTypeClicked -> isSelectSortTypeDialogVisible = event.isVisible
            is SearchUiEvent.OnSortTypeSelected -> {
                selectedSortType = event.sortType
                isSelectSortTypeDialogVisible = false
            }

            is SearchUiEvent.OnDismissFilterBottomSheet -> isFilterBottomSheetVisible = false
            is SearchUiEvent.OnOpenNowOnlyClicked -> isOpenNowOnly = !isOpenNowOnly
            is SearchUiEvent.OnQuerySet -> {
                val originalQueryString = currentDraftQuery.query
                currentDraftQuery = event.query.copy(query = originalQueryString)
            }

            is SearchUiEvent.OnQueryChanged -> {
                currentDraftQuery = currentDraftQuery.copy(query = event.query)
            }

            is SearchUiEvent.OnResetFilterClicked -> {
                currentDraftQuery =
                    currentDraftQuery.copy(region = null, district = null, species = null)
            }

            // Search Intents
            is SearchUiEvent.OnSearchButtonClicked -> {
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery,
                        currentUserLocation = locationData.currentUserLocation
                    )
                )
                isFilterBottomSheetVisible = false
                commonSearchArgument.intent(SearchIntent.ChangeScreenState(SearchScreenState.OnSearch.ResultView))
            }

            is SearchUiEvent.OnSearchNearby -> {
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchNearByHospitals(
                        bounds = event.bounds,
                        query = currentDraftQuery,
                        currentUserLocation = locationData.currentUserLocation
                    )
                )
            }

            is SearchUiEvent.OnRecentKeywordClicked -> {
                currentDraftQuery = currentDraftQuery.copy(query = event.keyword)
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery.copy(query = event.keyword),
                        currentUserLocation = locationData.currentUserLocation
                    )
                )
                commonSearchArgument.intent(SearchIntent.ChangeScreenState(SearchScreenState.OnSearch.ResultView))
            }

            is SearchUiEvent.OnRecentHospitalClicked -> {
                currentDraftQuery = currentDraftQuery.copy(query = event.hospitalName)
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery.copy(query = event.hospitalName),
                        currentUserLocation = locationData.currentUserLocation
                    )
                )
                commonSearchArgument.intent(SearchIntent.ChangeScreenState(SearchScreenState.OnSearch.ResultView))
            }

            is SearchUiEvent.OnDeleteRecentKeyword -> {
                hospitalSearchArgument.intent(HospitalSearchIntent.DeleteRecentKeyword(event.keyword))
            }

            is SearchUiEvent.OnDeleteRecentViewedHospital -> {
                hospitalSearchArgument.intent(HospitalSearchIntent.DeleteViewedHospital(event.hospitalId))
            }

            // Navigation Intents
            is SearchUiEvent.OnCurrentLocationClicked -> {
                userLocationArgument.intent(UserLocationIntent.RequestLocation)
            }

            is SearchUiEvent.OnListViewClicked -> commonSearchArgument.intent(
                SearchIntent.ChangeScreenState(
                    SearchScreenState.Hospitals.ListView
                )
            )

            is SearchUiEvent.OnSearchViewClicked -> commonSearchArgument.intent(
                SearchIntent.ChangeScreenState(
                    SearchScreenState.OnSearch.SearchView
                )
            )

            is SearchUiEvent.OnNavigateToMapView -> commonSearchArgument.intent(
                SearchIntent.ChangeScreenState(
                    SearchScreenState.Hospitals.MapView
                )
            )

            is SearchUiEvent.OnNavigateToResultView -> commonSearchArgument.intent(
                SearchIntent.ChangeScreenState(
                    SearchScreenState.OnSearch.ResultView
                )
            )

            // Map Specific
            is SearchUiEvent.OnMapReady -> { /* Handled in MapView */
            }
        }
    }

    LaunchedEffect(commonSearchArgument.event) {
        commonSearchArgument.event.collectCustomErrors { event ->
            when (event) {
                is SearchEvent.DataFetch.Error -> {}
            }
        }
    }

    // --- Content ---
    when (screenState) {
        is SearchScreenState.Hospitals.MapView -> {
            MapView(
                navController = navController,
                userLocationArgument = userLocationArgument,
                searchUiState = searchUiState,
                onEvent = onEvent
            )
        }

        is SearchScreenState.Hospitals.ListView -> {
            ListView(
                navController = navController,
                searchUiState = searchUiState,
                onEvent = onEvent
            )
        }

        is SearchScreenState.OnSearch.SearchView -> {
            SearchView(
                navController = navController,
                searchUiState = searchUiState,
                recentSearchKeywords = hospitalSearchData.recentSearchKeywords,
                viewedHospitals = hospitalSearchData.viewedHospitals,
                onEvent = onEvent
            )
        }

        is SearchScreenState.OnSearch.ResultView -> {
            ResultView(
                navController = navController,
                searchUiState = searchUiState,
                onEvent = onEvent
            )
        }
    }

    // --- Overlays (Global) ---
    FilterBottomSheet(
        currentQuery = currentDraftQuery,
        startTab = currentSelectedFilterBottomSheet,
        showBottomSheet = isFilterBottomSheetVisible,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = { onEvent(SearchUiEvent.OnDismissFilterBottomSheet(false)) },
        onQuerySet = { onEvent(SearchUiEvent.OnQuerySet(it)) },
        onResetFilterClicked = { onEvent(SearchUiEvent.OnResetFilterClicked) },
        onSearchButtonClicked = { onEvent(SearchUiEvent.OnSearchButtonClicked) },
    )

    if (isSelectSortTypeDialogVisible) {
        SelectSortTypeDialog(
            selectedSortType = selectedSortType,
            onDismissRequest = { onEvent(SearchUiEvent.OnSortTypeClicked(false)) },
            onSortTypeSelected = { onEvent(SearchUiEvent.OnSortTypeSelected(it)) }
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    PetbulanceTheme {
        SearchScreen(
            navController = rememberNavController(),
            userLocationArgument = UserLocationArgument(
                intent = { },
                locationState = UserLocationState.Init,
                event = MutableSharedFlow(),
            ),
            hospitalSearchArgument = HospitalSearchArgument(
                intent = { },
                hospitalDataState = HospitalSearchDataState.Init
            ),
            commonSearchArgument = CommonSearchArgument(
                intent = {},
                screenState = SearchScreenState.OnSearch.SearchView,
                event = MutableSharedFlow()
            ),
            locationData = UserLocationData.empty,
            hospitalSearchData = HospitalSearchData.empty,
        )
    }
}