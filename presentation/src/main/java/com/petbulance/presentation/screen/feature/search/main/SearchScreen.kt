package com.petbulance.presentation.screen.feature.search.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.model.type.HospitalSortType
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheet
import com.petbulance.presentation.component.ui.molecule.FilterBottomSheetTab
import com.petbulance.presentation.screen.feature.search.main.views.list.ListView
import com.petbulance.presentation.screen.feature.search.main.views.map.MapView
import com.petbulance.presentation.screen.feature.search.main.views.result.ResultView
import com.petbulance.presentation.screen.feature.search.main.views.result.SelectSortTypeDialog
import com.petbulance.presentation.screen.feature.search.main.views.search.SearchView
import com.petbulance.presentation.analytics.AnalyticsEvents
import com.petbulance.presentation.analytics.LocalAnalyticsTracker
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    isGuest: Boolean,
    commonSearchArgument: CommonSearchArgument,
    userLocationArgument: UserLocationArgument,
    hospitalSearchArgument: HospitalSearchArgument,
    locationData: UserLocationData,
    hospitalSearchData: HospitalSearchData,
    locationTerm: Term?,
    onTermsClick: () -> Unit,
    initialHospitalId: Long? = null
) {
    val analyticsTracker = LocalAnalyticsTracker.current
    val screenState = commonSearchArgument.screenState
    val context = LocalContext.current

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
        isGuest = isGuest,
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

                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery,
                        currentUserLocation = locationData.currentUserLocation,
                        sortType = selectedSortType,
                        keepPreviousBounds = false
                    )
                )
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
                    currentDraftQuery.copy(region = null, district = null, animalCategories = emptyList())
            }

            // Search Intents
            is SearchUiEvent.OnSearchButtonClicked -> {
                // GA4: apply_search_filter (필터가 있을 때)
                val hasRegionFilter = currentDraftQuery.region != null
                val hasAnimalFilter = currentDraftQuery.animalCategories.isNotEmpty()
                val hasOpenNowFilter = currentDraftQuery.openNowOnly == true
                if (hasRegionFilter || hasAnimalFilter || hasOpenNowFilter) {
                    val filterTypes = buildList {
                        if (hasRegionFilter) add("지역")
                        if (hasAnimalFilter) add("동물종")
                        if (hasOpenNowFilter) add("진료중")
                    }
                    analyticsTracker.trackEvent(
                        AnalyticsEvents.APPLY_SEARCH_FILTER,
                        mapOf(
                            AnalyticsEvents.Params.FILTER_TYPE to filterTypes.joinToString(","),
                            AnalyticsEvents.Params.FILTER_VALUE to buildString {
                                currentDraftQuery.region?.let { append(it.displayName) }
                                if (hasAnimalFilter) {
                                    if (isNotEmpty()) append("/")
                                    append(currentDraftQuery.animalCategories.joinToString(",") { it.korean })
                                }
                            },
                            AnalyticsEvents.Params.FROM_SCREEN to "병원검색"
                        )
                    )
                }

                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery,
                        currentUserLocation = locationData.currentUserLocation,
                        sortType = selectedSortType,
                        keepPreviousBounds = false
                    )
                )
                isFilterBottomSheetVisible = false
                commonSearchArgument.intent(SearchIntent.ChangeScreenState(SearchScreenState.OnSearch.ResultView))
            }

            is SearchUiEvent.OnSearchNearby -> {
                // GA4: search_map_current_location
                analyticsTracker.trackEvent(
                    AnalyticsEvents.SEARCH_MAP_CURRENT_LOCATION,
                    mapOf(AnalyticsEvents.Params.IS_FIRST_SEARCH to hospitalSearchData.hospitalList.isEmpty())
                )
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchNearByHospitals(
                        bounds = event.bounds,
                        query = currentDraftQuery,
                        currentUserLocation = locationData.currentUserLocation,
                        sortType = selectedSortType
                    )
                )
            }

            is SearchUiEvent.OnRecentKeywordClicked -> {
                currentDraftQuery = currentDraftQuery.copy(query = event.keyword)
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery.copy(query = event.keyword),
                        currentUserLocation = locationData.currentUserLocation,
                        sortType = selectedSortType,
                        keepPreviousBounds = false
                    )
                )
                commonSearchArgument.intent(SearchIntent.ChangeScreenState(SearchScreenState.OnSearch.ResultView))
            }

            is SearchUiEvent.OnRecentHospitalClicked -> {
                currentDraftQuery = currentDraftQuery.copy(query = event.hospitalName)
                hospitalSearchArgument.intent(
                    HospitalSearchIntent.SearchHospitalWithCurrentParams(
                        query = currentDraftQuery.copy(query = event.hospitalName),
                        currentUserLocation = locationData.currentUserLocation,
                        sortType = selectedSortType,
                        keepPreviousBounds = false
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

            is SearchUiEvent.OnListViewClicked -> {
                // GA4: switch_to_list_view
                analyticsTracker.trackEvent(
                    AnalyticsEvents.SWITCH_TO_LIST_VIEW,
                    mapOf(AnalyticsEvents.Params.FROM_VIEW to "지도")
                )
                commonSearchArgument.intent(
                    SearchIntent.ChangeScreenState(
                        SearchScreenState.Hospitals.ListView
                    )
                )
            }

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

            is SearchUiEvent.OnLoadMore -> {
                hospitalSearchArgument.intent(HospitalSearchIntent.LoadNextPage)
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
                onEvent = onEvent,
                locationTerm = locationTerm,
                onTermsClick = onTermsClick,
                initialHospitalId = initialHospitalId
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
            ),
            locationData = UserLocationData.empty,
            hospitalSearchData = HospitalSearchData.empty,
            isGuest = false,
            locationTerm = null,
            onTermsClick = {}
        )
    }
}