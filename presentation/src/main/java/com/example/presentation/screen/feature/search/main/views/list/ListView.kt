package com.example.presentation.screen.feature.search.main.views.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.hospital.hospital.Hospital
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.Space8
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.screen.feature.search.main.SearchUiEvent
import com.example.presentation.screen.feature.search.main.SearchUiState
import com.example.presentation.screen.feature.search.main.views.common.HospitalCard
import com.example.presentation.screen.feature.search.main.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.main.views.common.RowResultControlChips
import com.example.presentation.screen.feature.search.main.views.map.MapViewToggleButton
import com.example.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListView(
    navController: NavController,
    searchUiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit
) {
    BackHandler {
        onEvent(SearchUiEvent.OnNavigateToMapView)
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RowChipFilters(
                        uiModel = searchUiState.currentQuery,
                        onFilterButtonClicked = {
                            onEvent(SearchUiEvent.OnFilterButtonClicked(it))
                        }
                    )
                    RowResultControlChips(
                        selectedSortType = searchUiState.selectedSortType,
                        isOpenNowOnly = searchUiState.isOpenNowOnly,
                        onSortTypeClicked = { onEvent(SearchUiEvent.OnSortTypeClicked(true)) },
                        onOpenNowOnlyClicked = { onEvent(SearchUiEvent.OnOpenNowOnlyClicked) }
                    )
                }

                // List
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = spacingMedium,
                        vertical = spacingXXS
                    ),
                    verticalArrangement = Arrangement.spacedBy(spacingMedium),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(searchUiState.filteredHospitalList) { hospital ->
                        HospitalCard(
                            hospital = hospital,
                            borderColor = colorScheme.border.subtle,
                            onCardClick = {
                                navController.safeNavigate(
                                    route = ScreenDestinations.Search.HospitalInfo.createRoute(
                                        id = hospital.hospitalId
                                    )
                                )
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = spacingXL)
            ) {
                MapViewToggleButton(
                    isToggleToListView = false,
                    onClicked = { onEvent(SearchUiEvent.OnNavigateToMapView) }
                )
                Space8()
            }
        }
    }
}

@Preview
@Composable
private fun ListViewPreview() {
    PetbulanceTheme {
        ListView(
            navController = rememberNavController(),
            searchUiState = SearchUiState(
                hospitalList = listOf(
                    Hospital(
                        hospitalId = 1,
                        name = "행복 동물병원",
                        lat = 37.5,
                        lng = 127.0,
                        distanceMeters = 500.0,
                        phone = "02-123-4567",
                        types = listOf("강아지", "고양이"),
                        isOpenNow = true,
                        openHours = "20:00 종료",
                        thumbnailUrl = null,
                        rating = 4.5,
                        reviewCount = 100
                    )
                ),
                currentQuery = HospitalSearchQueryUiModel.empty,
            ),
            onEvent = {}
        )
    }
}