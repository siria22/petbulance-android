package com.petbulance.presentation.screen.feature.community.write

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.petbulance.presentation.utils.nav.ScreenDestinations

@Composable
fun WritePostScreen(
    navController: NavController,
    argument: WritePostArgument,
    data: WritePostData,
    onSubmit: (android.content.Context) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        argument.event.collect { event ->
            when (event) {
                is WritePostEvent.NavigateToPostDetail -> {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("showPostCreatedSnackbar", true)
                    
                    navController.navigate(
                        ScreenDestinations.Community.PostDetail.createRoute(event.postId)
                    ) {
                        popUpTo(ScreenDestinations.Community.WritePost.route) { inclusive = true }
                    }
                }

                is WritePostEvent.NavigateUp -> {
                    navController.popBackStack()
                }

                is WritePostEvent.SubmitError -> {
                    // ErrorEvent는 BaseViewModel에서 처리
                }
            }
        }
    }

    WritePostView(
        navController = navController,
        argument = argument,
        data = data,
        onSubmit = { onSubmit(context) }
    )
}
