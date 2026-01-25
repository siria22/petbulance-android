package com.petbulance.presentation.screen.feature.search.main.views.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.search.main.SearchUiEvent
import com.petbulance.presentation.screen.feature.search.main.SearchUiState
import com.petbulance.presentation.screen.feature.search.main.views.common.HospitalCard
import com.petbulance.presentation.screen.feature.search.main.views.common.RowChipFilters
import com.petbulance.presentation.screen.feature.search.main.views.common.RowResultControlChips
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel
import com.petbulance.presentation.screen.feature.search.main.views.search.SearchBar

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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = spacingMedium)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingXXS, Alignment.Start),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacingMedium)
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
        }

        if (searchUiState.filteredHospitalList.isNotEmpty()) {
            items(searchUiState.filteredHospitalList) { hospital ->
                HospitalCard(hospital = hospital)
                Spacer(modifier = Modifier.height(spacingMedium))
            }
        } else {
            item {
                NoResult(keywordName = searchUiState.currentQuery.query)
            }
        }
    }
}

@Composable
private fun NoResult(keywordName: String?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = spacingXL)
    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.img_no_result_2),
            contentDescription = "No result",
            size = 160.dp,
            tint = Color.Unspecified
        )
        val text = if (keywordName == null) "해당 조건의 병원을 찾을 수 없어요."
        else "$keywordName(을)를 찾을 수 없어요."

        Text(
            text = text,
            style = typography.titleSmall,
            color = colorScheme.text.tertiary
        )
        Text(
            text = "일부 키워드만 다시 입력하시거나,\n아래 방법을 이용해보세요.",
            textAlign = Center,
            style = typography.bodySmall,
            color = colorScheme.text.tertiary
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXS),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = spacingMedium, vertical = spacingLarge)
    ) {
        BasicButton(
            modifier = Modifier.fillMaxWidth(),
            text = "지도에서 찾아보기",
            size = BasicButtonSize.M,
            buttonType = BasicButtonType.SECONDARY,
            radius = 12.dp,
            onClicked = {
                /* TODO : 지도 페이지로 이동 */
            }
        )
        BasicButton(
            modifier = Modifier.fillMaxWidth(),
            text = "커뮤니티에 질문하기",
            size = BasicButtonSize.M,
            buttonType = BasicButtonType.SECONDARY,
            radius = 12.dp,
            onClicked = {
                /* TODO : 지도 페이지로 이동 */
            }
        )
        BasicButton(
            modifier = Modifier.fillMaxWidth(),
            text = "새로운 병원 제보하기",
            size = BasicButtonSize.M,
            buttonType = BasicButtonType.SECONDARY,
            radius = 12.dp,
            onClicked = {
                /* TODO : 지도 페이지로 이동 */
            }
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
                        types = listOf("댕댕이", "킹갓냥이"),
                        isOpenNow = true,
                        openHours = "20:00 종료",
                        thumbnailUrl = null,
                        rating = 4.5,
                        reviewCount = 100
                    ),
                    Hospital(
                        hospitalId = 1,
                        name = "행복 ^o^ 동물병원",
                        lat = 37.5,
                        lng = 127.0,
                        distanceMeters = 500.0,
                        phone = "02-123-4567",
                        types = listOf("멍멍", "야옹야옹"),
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