package com.example.presentation.screen.feature.search.views.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.screen.feature.search.SearchUiEvent
import com.example.presentation.screen.feature.search.SearchUiState
import com.example.presentation.screen.feature.search.views.common.HospitalCard
import com.example.presentation.screen.feature.search.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.views.common.RowResultControlChips
import com.example.presentation.screen.feature.search.views.map.MapViewToggleButton

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
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RowChipFilters(onFilterButtonClicked = {
                        onEvent(
                            SearchUiEvent.OnFilterButtonClicked(
                                it
                            )
                        )
                    })
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
                            onCardClick = {
                                // TODO: 추후 상세 화면 이동 구현 필요
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
                    com.example.domain.model.feature.hospital.hospital.Hospital(
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
                currentQuery = com.example.presentation.screen.feature.search.views.search.HospitalSearchQueryUiModel.empty,
            ),
            onEvent = {}
        )
    }
}