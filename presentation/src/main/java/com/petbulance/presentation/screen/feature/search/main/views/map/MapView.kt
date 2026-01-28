package com.petbulance.presentation.screen.feature.search.main.views.map

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BaseCarousel
import com.petbulance.presentation.component.ui.atom.CustomGreenLoader
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.molecule.LocationPermissionDialog
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.search.main.SearchEvent
import com.petbulance.presentation.screen.feature.search.main.SearchUiEvent
import com.petbulance.presentation.screen.feature.search.main.SearchUiState
import com.petbulance.presentation.screen.feature.search.main.UserLocationArgument
import com.petbulance.presentation.screen.feature.search.main.UserLocationIntent
import com.petbulance.presentation.screen.feature.search.main.UserLocationState
import com.petbulance.presentation.screen.feature.search.main.views.common.HospitalCard
import com.petbulance.presentation.screen.feature.search.main.views.common.RowChipFilters
import com.petbulance.presentation.screen.feature.search.main.views.common.RowResultControlChips
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.petbulance.presentation.utils.NaverMapView
import com.petbulance.presentation.utils.nav.ScreenDestinations
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    navController: NavController,
    userLocationArgument: UserLocationArgument,
    searchUiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit
) {
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }
    var selectedHospitalId by remember { mutableStateOf<Long?>(null) }

    var locationPermissionState by remember { mutableStateOf(LocationPermissionState.NO_PERMISSION) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isFine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val isCoarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        when {
            isFine -> {
                locationPermissionState = LocationPermissionState.FINE
                userLocationArgument.intent(UserLocationIntent.PermissionResult(true))
            }

            isCoarse -> {
                locationPermissionState = LocationPermissionState.COARSE
                userLocationArgument.intent(UserLocationIntent.PermissionResult(true))
            }

            else -> {
                locationPermissionState = LocationPermissionState.NO_PERMISSION
                userLocationArgument.intent(UserLocationIntent.PermissionResult(false))
            }
        }
        showPermissionDialog = false
    }

    LaunchedEffect(userLocationArgument.locationState) {
        if (userLocationArgument.locationState is UserLocationState.PermissionRequired) {
            showPermissionDialog = true
        }
    }

    LaunchedEffect(Unit) {
        userLocationArgument.event.collect { event ->
            if (event is SearchEvent.UserLocation.MoveCamera) {
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(event.location.latitude, event.location.longitude)
                ).animate(CameraAnimation.Easing)
                    .finishCallback {
                        val bounds = naverMap?.contentBounds
                        if (bounds != null) {
                            val mapBounds = MapBounds(
                                minLat = bounds.southWest.latitude,
                                minLng = bounds.southWest.longitude,
                                maxLat = bounds.northEast.latitude,
                                maxLng = bounds.northEast.longitude
                            )
                            onEvent(SearchUiEvent.OnSearchNearby(mapBounds))
                        }
                    }

                naverMap?.moveCamera(cameraUpdate)
            } else if (event is SearchEvent.UserLocation.CheckPermission.Error) {
                showPermissionDialog = true
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
                            onEvent(SearchUiEvent.OnSearchViewClicked)
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
            val isLocationLoading = userLocationArgument.locationState is UserLocationState.Init ||
                    userLocationArgument.locationState is UserLocationState.Finding

            if (isLocationLoading) {
                MapLayer(
                    state = searchUiState,
                    selectedHospitalId = null,
                    onHospitalSelected = {},
                    onMapReady = { naverMap = it }
                )
                Ready()
            } else {
                MapLayer(
                    state = searchUiState,
                    selectedHospitalId = selectedHospitalId,
                    onHospitalSelected = { selectedHospitalId = it },
                    onMapReady = { map ->
                        naverMap = map
                        searchUiState.currentUserLocation.let { loc ->
                            val cameraUpdate = CameraUpdate.scrollTo(
                                LatLng(loc.latitude, loc.longitude)
                            ).animate(CameraAnimation.Easing)
                            map.moveCamera(cameraUpdate)
                        }
                    }
                )
                MapUiLayer(
                    state = searchUiState,
                    onEvent = onEvent,
                    locationPermissionState = locationPermissionState,
                    onRecenterClick = {
                        val bounds = naverMap?.contentBounds
                        if (bounds != null) {
                            val domainBounds = MapBounds(
                                minLat = bounds.southWest.latitude,
                                minLng = bounds.southWest.longitude,
                                maxLat = bounds.northEast.latitude,
                                maxLng = bounds.northEast.longitude
                            )
                            onEvent(SearchUiEvent.OnSearchNearby(domainBounds))
                        }
                    },
                    onHospitalClick = { hospital ->
                        if (selectedHospitalId == hospital.hospitalId) {
                            navController.navigate(
                                ScreenDestinations.Search.HospitalInfo.createRoute(
                                    hospital.hospitalId
                                )
                            )
                        } else {
                            selectedHospitalId = hospital.hospitalId
                            val cameraUpdate = CameraUpdate.scrollTo(
                                LatLng(hospital.lat, hospital.lng)
                            )
                            naverMap?.moveCamera(cameraUpdate)
                        }
                    }
                )
            }
        }
    }

    if (showPermissionDialog) {
        LocationPermissionDialog(
            onDismiss = {
                showPermissionDialog = false
                locationPermissionState = LocationPermissionState.NO_PERMISSION
            },
            onAgree = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                showPermissionDialog = false
            },
            onTermsClick = {
                /* TODO : 약관 보여주는 어쩌고 */
            }
        )
    }
}

@Composable
private fun Ready() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        CustomGreenLoader(size = 48.dp)

        Text(
            text = "지도를 불러오고 있어요...",
            color = colorScheme.text.inverse,
            style = typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun MapLayer(
    state: SearchUiState,
    selectedHospitalId: Long?,
    onHospitalSelected: (Long?) -> Unit,
    onMapReady: (NaverMap) -> Unit
) {
    NaverMapView(
        currentLocation = state.currentUserLocation,
        cameraPosition = null,
        places = state.filteredHospitalList.map { it.toMarker() },
        selectedHospitalId = selectedHospitalId,
        onMapReady = onMapReady,
        onMapBoundsChange = { },
        onMarkerClicked = { onHospitalSelected(it) },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun MapUiLayer(
    state: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    locationPermissionState: LocationPermissionState,
    onRecenterClick: () -> Unit,
    onHospitalClick: (Hospital) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // TOP Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    spacingXS,
                    Alignment.CenterHorizontally
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacingMedium)
            ) {
                RowChipFilters(
                    uiModel = state.currentQuery,
                    onFilterButtonClicked = {
                        onEvent(SearchUiEvent.OnFilterButtonClicked(it))
                    }
                )
                RowResultControlChips(
                    selectedSortType = state.selectedSortType,
                    isOpenNowOnly = state.isOpenNowOnly,
                    onSortTypeClicked = { onEvent(SearchUiEvent.OnSortTypeClicked(true)) },
                    onOpenNowOnlyClicked = { onEvent(SearchUiEvent.OnOpenNowOnlyClicked) }
                )
            }
            RecenterSearchButton(onClick = onRecenterClick)

            if (locationPermissionState == LocationPermissionState.COARSE) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .background(
                            colorScheme.text.secondary.copy(alpha = 0.7f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = spacingXXS, horizontal = spacingXS),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "더 정확한 병원 안내를 위해 '정확한 위치' 권한을 허용해 주세요.",
                        style = typography.labelLarge.emp(),
                        color = colorScheme.text.inverse
                    )
                }
            }
        }

        // BOTTOM Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
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
                    onClicked = { onEvent(SearchUiEvent.OnListViewClicked) })

                if (locationPermissionState != LocationPermissionState.NO_PERMISSION) {
                    CurrentLocationFab(onClicked = { onEvent(SearchUiEvent.OnCurrentLocationClicked) })
                } else {
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }
            if (state.filteredHospitalList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacingMedium)
                ) {
                    HospitalCard(
                        hospital = null
                    )
                }
            } else {
                BaseCarousel(
                    modifier = Modifier.fillMaxWidth(),
                    items = state.filteredHospitalList,
                    contentPadding = PaddingValues(spacingMedium),
                    itemSpacing = spacingXS
                ) { _, item ->
                    HospitalCard(
                        hospital = item,
                        isShadowed = true,
                        onCardClick = { onHospitalClick(item) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MapViewPreview() {
    PetbulanceTheme {
        MapView(
            navController = rememberNavController(),
            userLocationArgument = UserLocationArgument(
                intent = {},
                locationState = UserLocationState.Success(
                    Location("").apply {
                        latitude = 37.5665
                        longitude = 126.9780
                    }
                ),
                event = MutableSharedFlow()
            ),
            searchUiState = SearchUiState(
                hospitalList = listOf(
                    Hospital.stub()
                ),
//                hospitalList = emptyList(),
                currentQuery = HospitalSearchQueryUiModel.empty,
            ),
            onEvent = {}
        )
    }
}
