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
import androidx.compose.runtime.Composable
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
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.screen.feature.search.SearchUiEvent
import com.example.presentation.screen.feature.search.SearchUiState
import com.example.presentation.screen.feature.search.views.common.HospitalCard
import com.example.presentation.screen.feature.search.views.common.RowChipFilters
import com.example.presentation.screen.feature.search.views.common.RowResultControlChips
import com.example.presentation.screen.feature.search.views.search.HospitalSearchQueryUiModel
import com.example.presentation.screen.feature.search.views.search.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultView(
    navController: NavController,
    searchUiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            SearchBar(
                queryString = searchUiState.currentQuery.query ?: "",
                onQueryStringChanged = { /* 결과창에서는 입력 불가 */ },
                onMoveBackIconClicked = { onEvent(SearchUiEvent.OnNavigateToMapView) },
                onSearchButtonClicked = { /* 동작 불필요 */ }
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
                searchUiState = searchUiState,
                onEvent = onEvent
            )
        }
    }

    BackHandler {
        onEvent(SearchUiEvent.OnNavigateToMapView)
    }
}

@Composable
private fun ResultViewContents(
    searchUiState: SearchUiState,
    onEvent: (SearchUiEvent) -> Unit
) {
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
        }

        if (searchUiState.filteredHospitalList.isNotEmpty()) {
            items(searchUiState.filteredHospitalList) { hospital ->
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

@Preview
@Composable
private fun ResultViewPreview() {
    PetbulanceTheme {
        ResultView(
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
                currentQuery = HospitalSearchQueryUiModel.empty.copy(query = "동물병원"),
            ),
            onEvent = {}
        )
    }
}