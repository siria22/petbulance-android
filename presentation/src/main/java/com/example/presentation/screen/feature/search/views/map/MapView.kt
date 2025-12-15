package com.example.presentation.screen.feature.search.views.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.domain.model.feature.hospital.hospital.Hospital
import com.example.domain.model.feature.hospital.hospital.MapBounds
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BaseCarousel
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.molecule.FilterBottomSheet
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.screen.feature.search.CommonSearchArgument
import com.example.presentation.screen.feature.search.HospitalSearchArgument
import com.example.presentation.screen.feature.search.HospitalSearchData
import com.example.presentation.screen.feature.search.HospitalSearchDataState
import com.example.presentation.screen.feature.search.HospitalSearchIntent
import com.example.presentation.screen.feature.search.SearchEvent
import com.example.presentation.screen.feature.search.SearchIntent
import com.example.presentation.screen.feature.search.SearchScreenState
import com.example.presentation.screen.feature.search.UserLocationArgument
import com.example.presentation.screen.feature.search.UserLocationData
import com.example.presentation.screen.feature.search.UserLocationIntent
import com.example.presentation.screen.feature.search.UserLocationState
import com.example.presentation.screen.feature.search.views.common.HospitalCard
import com.example.presentation.screen.feature.search.views.common.HospitalSortType
import com.example.presentation.screen.feature.search.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.views.common.RowResultControlChips
import com.example.presentation.screen.feature.search.views.result.SelectSortTypeDialog
import com.example.presentation.utils.NaverMapView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
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

    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    LaunchedEffect(Unit) {
        commonSearchArgument.event.collect { event ->
            when (event) {
                is SearchEvent.UserLocation.MoveCamera -> {
                    val cameraUpdate = CameraUpdate.scrollTo(
                        LatLng(
                            event.location.latitude,
                            event.location.longitude
                        )
                    )
                    naverMap?.moveCamera(cameraUpdate)
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "병원 검색",
                    textAlignment = TopBarAlignment.START,
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Default.Search)) {
                            commonSearchArgument.intent(
                                SearchIntent.ChangeScreenState(
                                    SearchScreenState.OnSearch.SearchView
                                )
                            )
                        }
                    )
                )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapViewContents(
                hospitalList = hospitalSearchData.hospitalList,
                locationData = locationData,
                selectedSortType = selectedSortType,
                isOpenNowOnly = isOpenNowOnly,
                onFilterButtonClicked = { tab ->
                    currentSelectedFilterBottomSheet = tab
                    isFilterBottomSheetVisible = true
                },
                onMapReady = { map -> naverMap = map },
                onSortTypeClicked = { isSelectSortTypeDialogVisible = true },
                onOpenNowOnlyClicked = { isOpenNowOnly = !isOpenNowOnly },
                onCurrentLocationClicked = { userLocationArgument.intent(UserLocationIntent.RequestLocation) },
                onListViewClicked = {
                    commonSearchArgument.intent(
                        SearchIntent.ChangeScreenState(
                            SearchScreenState.Hospitals.ListView
                        )
                    )
                },
                onRecenterSearchButtonClicked = {
                    val bounds = naverMap?.contentBounds
                    if (bounds != null) {
                        val domainBounds = MapBounds(
                            minLat = bounds.southWest.latitude,
                            minLng = bounds.southWest.longitude,
                            maxLat = bounds.northEast.latitude,
                            maxLng = bounds.northEast.longitude
                        )
                        hospitalSearchArgument.intent(
                            HospitalSearchIntent.SearchNearByHospitals(
                                bounds = domainBounds,
                                query = currentQuery,
                                currentUserLocation = locationData.currentUserLocation
                            )
                        )
                    }
                }
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
        onResetFilterClicked = {
            currentQuery = currentQuery.copy(
                region = null,
                district = null,
                species = null
            )
        },
        onSearchButtonClicked = {
            hospitalSearchArgument.intent(
                HospitalSearchIntent.SearchHospitalWithCurrentParams(
                    query = currentQuery,
                    currentUserLocation = locationData.currentUserLocation
                )
            )
        },
    )

    if (isSelectSortTypeDialogVisible) {
        SelectSortTypeDialog(
            onDismissRequest = { isSelectSortTypeDialogVisible = false },
            onSortTypeSelected = {
                selectedSortType = it
                isSelectSortTypeDialogVisible = false
            }
        )
    }
}

@Composable
private fun MapViewContents(
    hospitalList: List<Hospital>,
    locationData: UserLocationData,
    onFilterButtonClicked: (FilterBottomSheetTab) -> Unit,
    selectedSortType: HospitalSortType,
    isOpenNowOnly: Boolean,
    onSortTypeClicked: () -> Unit,
    onOpenNowOnlyClicked: (Boolean) -> Unit,
    onCurrentLocationClicked: () -> Unit,
    onListViewClicked: () -> Unit,
    onMapReady: (NaverMap) -> Unit,
    onRecenterSearchButtonClicked: () -> Unit
) {
    var currentSelectedHospitalId by remember { mutableStateOf<Long?>(null) }

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NaverMapView(
            currentLocation = locationData.currentUserLocation,
            cameraPosition = locationData.currentUserLocation,
            places = filteredHospitalList.map { it.toMarker() },
            selectedHospitalId = currentSelectedHospitalId,
            onMapReady = onMapReady,
            onMapBoundsChange = { },
            onMarkerClicked = {
                currentSelectedHospitalId = it
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // TOP
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                RecenterSearchButton(
                    onClick = { onRecenterSearchButtonClicked() }
                )
            }

            // BOTTOM
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacingMedium)
                ) {
                    Spacer(modifier = Modifier.width(40.dp))

                    MapViewToggleButton(
                        isToggleToListView = true,
                        onClicked = { onListViewClicked() }
                    )

                    CurrentLocationFab(onClicked = { onCurrentLocationClicked() })
                }
                BaseCarousel(
                    modifier = Modifier.fillMaxWidth(),
                    items = filteredHospitalList,
                    contentPadding = PaddingValues(spacingMedium),
                    itemSpacing = spacingXS
                ) { _, item ->
                    HospitalCard(item) // TODO : onCardClicked
                }
            }
        }
    }

}


@Preview(apiLevel = 34)
@Composable
private fun MapViewPreview() {
    PetbulanceTheme {
        MapView(
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
            hospitalSearchData = HospitalSearchData.empty.copy(
                hospitalList = listOf(
                    Hospital(
                        hospitalId = 1,
                        name = "서울대학교병원",
                        lat = 37.57988,
                        lng = 126.9996,
                        distanceMeters = 500.0,
                        phone = "02-2072-2114",
                        types = listOf("종합병원", "상급종합병원"),
                        isOpenNow = true,
                        openHours = "09:00 - 18:00",
                        thumbnailUrl = null,
                        rating = 4.5,
                        reviewCount = 120
                    ),
                    Hospital(
                        hospitalId = 2,
                        name = "세브란스병원",
                        lat = 37.5629,
                        lng = 126.9463,
                        distanceMeters = 1500.0,
                        phone = "1599-1004",
                        types = listOf("종합병원", "상급종합병원"),
                        isOpenNow = false,
                        openHours = "08:30 - 17:30",
                        thumbnailUrl = null,
                        rating = 4.7,
                        reviewCount = 250
                    )
                )
            )
        )
    }
}