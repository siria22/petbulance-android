package com.example.presentation.screen.feature.search.main.views.map

import android.location.Location
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
import com.example.domain.model.feature.hospital.hospital.MapBounds
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BaseCarousel
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.screen.feature.search.main.CommonSearchArgument
import com.example.presentation.screen.feature.search.main.SearchEvent
import com.example.presentation.screen.feature.search.main.SearchScreenState
import com.example.presentation.screen.feature.search.main.SearchUiEvent
import com.example.presentation.screen.feature.search.main.SearchUiState
import com.example.presentation.screen.feature.search.main.views.common.HospitalCard
import com.example.presentation.screen.feature.search.main.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.main.views.common.RowResultControlChips
import com.example.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
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
    searchUiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit
) {
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    LaunchedEffect(Unit) {
        commonSearchArgument.event.collect { event ->
            if (event is SearchEvent.UserLocation.MoveCamera) {
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(event.location.latitude, event.location.longitude)
                )
                naverMap?.moveCamera(cameraUpdate)
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
            if (searchUiState.hospitalList.isEmpty()) {
                NoResult()
            } else {
                MapLayer(
                    state = searchUiState,
                    onMapReady = { naverMap = it }
                )
                MapUiLayer(
                    state = searchUiState,
                    onEvent = onEvent,
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
                    }
                )
            }
        }
    }
}

@Composable
fun MapLayer(
    state: SearchUiState,
    onMapReady: (NaverMap) -> Unit
) {
    var currentSelectedHospitalId by remember { mutableStateOf<Long?>(null) }
    val defaultLocation = Location("Default").apply { latitude = 37.57; longitude = 126.98 }

    NaverMapView(
        currentLocation = state.currentUserLocation ?: defaultLocation,
        cameraPosition = state.currentUserLocation ?: defaultLocation,
        places = state.filteredHospitalList.map { it.toMarker() },
        selectedHospitalId = currentSelectedHospitalId,
        onMapReady = onMapReady,
        onMapBoundsChange = { },
        onMarkerClicked = { currentSelectedHospitalId = it },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun MapUiLayer(
    state: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit,
    onRecenterClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // TOP Controls
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
        }

        // BOTTOM Controls
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                CurrentLocationFab(onClicked = { onEvent(SearchUiEvent.OnCurrentLocationClicked) })
            }
            BaseCarousel(
                modifier = Modifier.fillMaxWidth(),
                items = state.filteredHospitalList,
                contentPadding = PaddingValues(spacingMedium),
                itemSpacing = spacingXS
            ) { _, item ->
                HospitalCard(
                    item,
                    /* TODO : on click = if isSelected => 화면 이동, else => 지도 상에서 강조 */
                )
            }
        }
    }
}

@Composable
private fun NoResult() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = spacingXL)

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally
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
}

@Preview
@Composable
private fun MapViewPreview() {
    PetbulanceTheme {
        MapView(
            navController = rememberNavController(),
            commonSearchArgument = CommonSearchArgument(
                screenState = SearchScreenState.Hospitals.MapView,
                event = MutableSharedFlow(),
                intent = {}
            ),
            searchUiState = SearchUiState(
                hospitalList = emptyList(),
                currentQuery = HospitalSearchQueryUiModel.empty,
            ),
            onEvent = {}
        )
    }
}