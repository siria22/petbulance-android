package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.utils.error.collectCustomErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageProfileScreen(
    navController: NavController,
    argument: MyPageProfileArgument,
    data: MyPageProfileData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dataState = argument.dataState
    val screenState = argument.screenState

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is MyPageProfileEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "Profile",
                    isLeadingIconAvailable = false,
                    onLeadingIconClicked = {},
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = false,
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.MY,
                navController = navController
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ProfileScreenContents(

            )
        }
    }

    // BackHandler {  }
}

@Composable
private fun ProfileScreenContents(

) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {

    }
}


@Preview
@Composable
private fun MyPageProfileScreenPreview() {
    PetbulanceTheme {
        MyPageProfileScreen(
            navController = rememberNavController(),
            argument = MyPageProfileArgument(
                intent = { },
                dataState = MyPageProfileDataState.Init,
                screenState = MyPageProfileScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageProfileData(
                data = ""
            )
        )
    }
}