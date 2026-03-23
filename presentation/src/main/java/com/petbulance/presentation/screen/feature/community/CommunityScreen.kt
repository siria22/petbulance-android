package com.petbulance.presentation.screen.feature.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.community.post.NoticeBanner
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchArgument
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchData
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchDataState
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchEvent
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchIntent
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchIntent.ApplyFilter
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchIntent.SearchComments
import com.petbulance.presentation.screen.feature.community.search.CommunitySearchIntent.SearchPosts
import com.petbulance.presentation.screen.feature.community.search.components.CommunityFilterBottomSheet
import com.petbulance.presentation.screen.feature.community.search.components.SearchTab
import com.petbulance.presentation.screen.feature.community.search.components.SearchTabRow
import com.petbulance.presentation.screen.feature.community.search.components.SearchTopBar
import com.petbulance.presentation.screen.feature.community.search.views.CommentSearchResultView
import com.petbulance.presentation.screen.feature.community.search.views.CommunityMainView
import com.petbulance.presentation.screen.feature.community.search.views.PostSearchResultView
import com.petbulance.presentation.screen.feature.community.search.views.SearchInputView
import com.petbulance.presentation.utils.nav.ScreenDestinations
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun CommunityScreen(
    navController: NavController,
    argument: CommunityArgument,
    data: CommunityData,
    searchArgument: CommunitySearchArgument,
    searchData: CommunitySearchData,
    recentKeywords: List<String>,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                argument.intent(CommunityIntent.SilentRefresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        argument.event.collect { event ->
            when (event) {
                is CommunityEvent.ShowComingSoonMessage -> {
                    snackbarHostState.showSnackbar("${event.feature} 기능은 준비 중입니다")
                }

                is CommunityEvent.NavigateToPostDetail -> {
                    navController.navigate(ScreenDestinations.Community.PostDetail.createRoute(event.postId))
                }

                is CommunityEvent.NavigateToNotice -> {
                    navController.navigate(ScreenDestinations.MyPage.Help.Notice.Detail.createRoute(event.noticeId))
                }

                is CommunityEvent.DataFetch.Error -> {
                    // BaseViewModel에서 처리
                }

                is CommunityEvent.NavigateToWritePost -> {
                    navController.navigate(ScreenDestinations.Community.WritePost.createRoute())
                }
            }
        }
    }

    LaunchedEffect(searchArgument.event) {
        searchArgument.event.collect { event ->
            when (event) {
                is CommunitySearchEvent.ChangeScreenState -> {
                    argument.intent(CommunityIntent.ChangeSearchScreenState(event.state))
                }

                else -> {}
            }
        }
    }

    // 화면 상태 처리
    when (argument.screenState) {
        is CommunityScreenState.Home -> {
            CommunityMainView(
                navController = navController,
                argument = argument,
                data = data,
                snackbarHostState = snackbarHostState,
            )
        }

        is CommunityScreenState.Result,
        is CommunityScreenState.Search -> {
            CommunitySearchContent(
                navController = navController,
                screenState = argument.screenState,
                searchArgument = searchArgument,
                searchData = searchData,
                onBackToHome = {
                    argument.intent(CommunityIntent.ChangeSearchScreenState(CommunityScreenState.Home))
                },
                recentKeywords = recentKeywords
            )
        }
    }
}

@Composable
private fun CommunitySearchContent(
    navController: NavController,
    screenState: CommunityScreenState,
    searchArgument: CommunitySearchArgument,
    searchData: CommunitySearchData,
    onBackToHome: () -> Unit,
    recentKeywords: List<String>
) {
    var searchKeywordInput by remember { mutableStateOf(searchData.searchKeyword) }
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    val currentTab = when (screenState) {
        is CommunityScreenState.Result.Post -> SearchTab.POST
        is CommunityScreenState.Result.Comment -> SearchTab.COMMENT
        else -> SearchTab.POST
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                searchKeyword = searchKeywordInput,
                onSearchKeywordChange = { searchKeywordInput = it },
                onBackClick = onBackToHome,
                onSearchClick = {
                    if (searchKeywordInput.isNotBlank()) {
                        if (currentTab == SearchTab.POST) {
                            searchArgument.intent(SearchPosts(searchKeywordInput))
                        } else {
                            searchArgument.intent(
                                SearchComments(searchKeywordInput)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.COMMUNITY,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (screenState) {
                is CommunityScreenState.Search -> {


                    SearchInputView(
                        searchKeyword = searchKeywordInput,
                        recentKeywords = recentKeywords,
                        onSearchKeywordChange = { searchKeywordInput = it },
                        onSearchClick = {
                            if (searchKeywordInput.isNotBlank()) {
                                searchArgument.intent(
                                    SearchPosts(
                                        searchKeywordInput
                                    )
                                )
                            }
                        },
                        onRecentKeywordClick = { keyword ->
                            searchKeywordInput = keyword
                            searchArgument.intent(SearchPosts(keyword))
                        },
                        onDeleteKeyword = { keyword ->
                            searchArgument.intent(CommunitySearchIntent.DeleteRecentKeyword(keyword))
                        },
                        onDeleteAllKeywords = {
                            searchArgument.intent(CommunitySearchIntent.DeleteAllRecentKeywords)
                        },
                        onRestoreKeywords = { keywords ->
                            searchArgument.intent(
                                CommunitySearchIntent.RestoreRecentKeywords(
                                    keywords
                                )
                            )
                        }
                    )
                }

                is CommunityScreenState.Result.Post -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchTabRow(
                            selectedTab = currentTab,
                            onTabSelected = { tab ->
                                when (tab) {
                                    SearchTab.POST -> {}
                                    SearchTab.COMMENT -> {
                                        if (searchData.searchKeyword.isNotBlank()) {
                                            searchArgument.intent(
                                                SearchComments(
                                                    searchData.searchKeyword
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        )

                        PostSearchResultView(
                            argument = searchArgument,
                            data = searchData,
                            dataState = searchArgument.dataState,
                            onFilterClick = { showFilterBottomSheet = true },
                            onCreatePostClick = { },
                            onSortClick = { /* TODO : Sort */ },
                        )
                    }
                }

                is CommunityScreenState.Result.Comment -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchTabRow(
                            selectedTab = currentTab,
                            onTabSelected = { tab ->
                                when (tab) {
                                    SearchTab.POST -> {
                                        if (searchData.searchKeyword.isNotBlank()) {
                                            searchArgument.intent(
                                                SearchPosts(
                                                    searchData.searchKeyword
                                                )
                                            )
                                        }
                                    }

                                    SearchTab.COMMENT -> {}
                                }
                            }
                        )

                        CommentSearchResultView(
                            argument = searchArgument,
                            data = searchData,
                            dataState = searchArgument.dataState,
                            onFilterClick = { showFilterBottomSheet = true },
                            onCreatePostClick = { }
                        )
                    }
                }

                CommunityScreenState.Home -> {}
            }
        }
    }

    if (showFilterBottomSheet) {
        CommunityFilterBottomSheet(
            selectedAnimalCategory = searchData.selectedAnimalCategory,
            selectedPostCategory = searchData.selectedPostCategory,
            onApply = { animalCategory, postCategory ->
                searchArgument.intent(
                    ApplyFilter(
                        animalCategory,
                        postCategory
                    )
                )
                showFilterBottomSheet = false
            },
            onDismiss = { showFilterBottomSheet = false }
        )
    }
}

@Composable
fun NoticeBannerItem(
    banner: NoticeBanner,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = colorScheme.status.info.bg
            )
            .padding(vertical = spacingXS, horizontal = spacingXL)

    ) {
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.ic_bullhorn),
            contentDescription = "Notice",
            size = iconSizeMedium,
            tint = colorScheme.status.info.default,
            modifier = Modifier.clickable { onClick() }
        )

        Text(
            text = banner.title,
            style = typography.labelLarge,
            color = colorScheme.text.tertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CommunityScreenHomePreview() {
    PetbulanceTheme {
        CommunityScreen(
            navController = rememberNavController(),
            argument = CommunityArgument(
                intent = {},
                dataState = CommunityDataState.Init,
                screenState = CommunityScreenState.Home,
                event = MutableSharedFlow()
            ),
            data = CommunityData.stub(),
            searchArgument = CommunitySearchArgument(
                intent = {},
                dataState = CommunitySearchDataState.Init,
                event = MutableSharedFlow()
            ),
            recentKeywords = listOf(),
            searchData = CommunitySearchData.stub()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityScreenSearchPreview() {
    PetbulanceTheme {
        CommunityScreen(
            navController = rememberNavController(),
            argument = CommunityArgument(
                intent = {},
                dataState = CommunityDataState.Init,
                screenState = CommunityScreenState.Search,
                event = MutableSharedFlow()
            ),
            data = CommunityData.stub(),
            searchArgument = CommunitySearchArgument(
                intent = {},
                dataState = CommunitySearchDataState.Init,
                event = MutableSharedFlow()
            ),
            recentKeywords = listOf(),
            searchData = CommunitySearchData.stub()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityScreenResultPreview() {
    PetbulanceTheme {
        CommunityScreen(
            navController = rememberNavController(),
            argument = CommunityArgument(
                intent = {},
                dataState = CommunityDataState.Init,
                screenState = CommunityScreenState.Result.Post,
                event = MutableSharedFlow()
            ),
            data = CommunityData.stub(),
            searchArgument = CommunitySearchArgument(
                intent = {},
                dataState = CommunitySearchDataState.Init,
                event = MutableSharedFlow()
            ),
            recentKeywords = listOf(),
            searchData = CommunitySearchData.stub()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommunityScreenCommentResultPreview() {
    PetbulanceTheme {
        CommunityScreen(
            navController = rememberNavController(),
            argument = CommunityArgument(
                intent = {},
                dataState = CommunityDataState.Init,
                screenState = CommunityScreenState.Result.Comment,
                event = MutableSharedFlow()
            ),
            data = CommunityData.stub(),
            searchArgument = CommunitySearchArgument(
                intent = {},
                dataState = CommunitySearchDataState.Init,
                event = MutableSharedFlow()
            ),
            recentKeywords = listOf(),
            searchData = CommunitySearchData.stub()
        )
    }
}
