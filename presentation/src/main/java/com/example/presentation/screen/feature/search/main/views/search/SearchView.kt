package com.example.presentation.screen.feature.search.main.views.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.Composable
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
import com.example.presentation.component.ui.iconSizeMS
import com.example.presentation.component.ui.molecule.FilterBottomSheetTab
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXXS
import com.example.presentation.screen.feature.search.main.SearchUiEvent
import com.example.presentation.screen.feature.search.main.SearchUiState
import com.example.presentation.screen.feature.search.main.views.common.RowChipFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    navController: NavController,
    searchUiState: SearchUiState,
    recentSearchKeywords: List<RecentSearchKeyword>,
    viewedHospitals: ViewedHospitalList,
    onEvent: (SearchUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            SearchBar(
                queryString = searchUiState.currentQuery.query ?: "",
                onQueryStringChanged = { onEvent(SearchUiEvent.OnQueryChanged(it)) },
                onMoveBackIconClicked = { onEvent(SearchUiEvent.OnNavigateToMapView) },
                onSearchButtonClicked = { onEvent(SearchUiEvent.OnSearchButtonClicked) }
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
                recentQueryList = recentSearchKeywords,
                recentViewedHospital = viewedHospitals,
                uiModel = searchUiState.currentQuery,
                onFilterButtonClicked = { onEvent(SearchUiEvent.OnFilterButtonClicked(it)) },
                onChipClicked = { onEvent(SearchUiEvent.OnRecentKeywordClicked(it)) },
                onRecentHospitalClicked = { onEvent(SearchUiEvent.OnRecentHospitalClicked(it)) },
                onDeleteRecentKeyword = { onEvent(SearchUiEvent.OnDeleteRecentKeyword(it)) },
                onDeleteRecentViewedHospital = {
                    onEvent(
                        SearchUiEvent.OnDeleteRecentViewedHospital(
                            it.hospitalId
                        )
                    )
                },
            )
        }
    }

    BackHandler {
        onEvent(SearchUiEvent.OnNavigateToMapView)
    }
}

@Composable
private fun SearchViewContents(
    recentQueryList: List<RecentSearchKeyword>,
    recentViewedHospital: ViewedHospitalList,
    uiModel: HospitalSearchQueryUiModel,
    onFilterButtonClicked: (FilterBottomSheetTab) -> Unit,
    onChipClicked: (String) -> Unit,
    onRecentHospitalClicked: (String) -> Unit,
    onDeleteRecentKeyword: (String) -> Unit,
    onDeleteRecentViewedHospital: (ViewedHospital) -> Unit,
) {
    Column(modifier = Modifier.padding(spacingMedium)) {
        RowChipFilters(
            uiModel = uiModel,
            onFilterButtonClicked = onFilterButtonClicked
        )

        RecentHistorySection(
            title = "최근 검색어",
            items = recentQueryList,
            onItemClick = { onChipClicked(it.keyword) },
            onDeleteClick = { onDeleteRecentKeyword(it.keyword) }
        )

        RecentHistorySection(
            title = "최근 본 병원",
            items = recentViewedHospital.items,
            onItemClick = { onRecentHospitalClicked(it.hospitalName) },
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
            .padding(top = spacingXS, bottom = spacingMedium)
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
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
            ){
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
            .background(color = colorScheme.bg.frame.medium, shape = RoundedCornerShape(8.dp))
            .padding(vertical = spacingXXXS, horizontal = spacingXS)
    ) {
        Text(
            text = content.getContentAsString(),
            style = MaterialTheme.typography.labelLarge,
            color = colorScheme.text.secondary,
            modifier = Modifier.clickable { onChipClicked(content) }
        )
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.Clear),
            contentDescription = "Delete history",
            size = iconSizeMS,
            modifier = Modifier.clickable { onDeleteIconClicked(content) }
        )
    }
}

@Preview
@Composable
private fun SearchViewPreview() {
    PetbulanceTheme {
        SearchView(
            navController = rememberNavController(),
            searchUiState = SearchUiState(
                hospitalList = emptyList(),
                currentQuery = HospitalSearchQueryUiModel.empty,
            ),
            recentSearchKeywords = listOf(
                RecentSearchKeyword(1, "도마뱀", "2023-10-27"),
                RecentSearchKeyword(2, "햄스터", "2023-10-26"),
                RecentSearchKeyword(1, "합정", "2023-10-27"),
                RecentSearchKeyword(1, "친칠라", "2023-10-27"),
            ),
            viewedHospitals = ViewedHospitalList(
                items = listOf(
                    ViewedHospital(1, "연세동물병원", "2023-10-27"),
                    ViewedHospital(2, "에코동물병원", "2023-10-26"),
                    ViewedHospital(2, "서울응급동물병원", "2023-10-26"),
                    ViewedHospital(2, "행복동물병원", "2023-10-26"),
                ),
                totalCount = 2
            ),
            onEvent = {}
        )
    }
}