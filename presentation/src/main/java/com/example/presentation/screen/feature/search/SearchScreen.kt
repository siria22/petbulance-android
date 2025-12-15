package com.example.presentation.screen.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.screen.feature.search.views.map.MapView
import com.example.presentation.screen.feature.search.views.result.ResultView
import com.example.presentation.screen.feature.search.views.search.SearchView
import com.example.presentation.utils.error.collectCustomErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun SearchScreen(
    navController: NavController,
    commonSearchArgument: CommonSearchArgument,
    userLocationArgument: UserLocationArgument,
    hospitalSearchArgument: HospitalSearchArgument,
    locationData: UserLocationData,
    hospitalSearchData: HospitalSearchData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val screenState = commonSearchArgument.screenState

    LaunchedEffect(commonSearchArgument.event) {
        commonSearchArgument.event.collectCustomErrors { event ->
            when (event) {
                is SearchEvent.DataFetch.Error -> {

                }
            }
        }
    }

    when (screenState) {
        is SearchScreenState.Hospitals.MapView -> {
            MapView(
                navController = navController,
                commonSearchArgument = commonSearchArgument,
                userLocationArgument = userLocationArgument,
                hospitalSearchArgument = hospitalSearchArgument,
                locationData = locationData,
                hospitalSearchData = hospitalSearchData
            )
        }

        is SearchScreenState.Hospitals.ListView -> {

        }

        is SearchScreenState.OnSearch.SearchView -> {
            SearchView(
                navController = navController,
                commonSearchArgument = commonSearchArgument,
                userLocationArgument = userLocationArgument,
                hospitalSearchArgument = hospitalSearchArgument,
                locationData = locationData,
                hospitalSearchData = hospitalSearchData
            )
        }

        is SearchScreenState.OnSearch.ResultView -> {
            ResultView(
                navController = navController,
                commonSearchArgument = commonSearchArgument,
                userLocationArgument = userLocationArgument,
                hospitalSearchArgument = hospitalSearchArgument,
                locationData = locationData,
                hospitalSearchData = hospitalSearchData
            )
        }
    }

    // BackHandler {  }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    PetbulanceTheme {
        SearchScreen(
            navController = rememberNavController(),
            userLocationArgument = UserLocationArgument(
                intent = { },
                locationState = UserLocationState.Init
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