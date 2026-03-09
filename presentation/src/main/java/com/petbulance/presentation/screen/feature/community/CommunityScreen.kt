package com.petbulance.presentation.screen.feature.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.utils.error.collectCustomErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun CommunityScreen(
    navController: NavController,
    argument: CommunityArgument,
    data: CommunityData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dataState = argument.dataState
    val screenState = argument.screenState

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is CommunityEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "Community",
                    isLeadingIconAvailable = false,
                    onLeadingIconClicked = {},
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false,
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.COMMUNITY,
                navController = navController
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            CommunityScreenContents()
        }
    }

    // BackHandler {  }
}

@Composable
private fun CommunityScreenContents(

) {
    Column(
        modifier = Modifier.padding(vertical = spacingXL),
        verticalArrangement = Arrangement.spacedBy(spacingLarge)
    ) {
        Text(
            text = "커뮤니티 기능 준비중이에요!",
            color = colorScheme.action.primary.default,
            style = typography.titleSmall.emp()
        )

        Text(
            text = "빠른 시일 내에 찾아뵐게요.",
            color = colorScheme.text.tertiary,
            style = typography.bodySmall
        )

        BasicImageBox(
            modifier = Modifier.fillMaxWidth(),
            imageResource = R.drawable.ic_comming_soon,
        )
    }
}


@Preview
@Composable
private fun CommunityScreenPreview() {
    PetbulanceTheme {
        CommunityScreen(
            navController = rememberNavController(),
            argument = CommunityArgument(
                intent = { },
                dataState = CommunityDataState.Init,
                screenState = CommunityScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = CommunityData(
                data = ""
            )
        )
    }
}