package com.petbulance.presentation.screen.feature.community.search.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.PostCategory
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicFabIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.screen.feature.community.CommunityArgument
import com.petbulance.presentation.screen.feature.community.CommunityData
import com.petbulance.presentation.screen.feature.community.CommunityDataState
import com.petbulance.presentation.screen.feature.community.CommunityIntent
import com.petbulance.presentation.screen.feature.community.NoticeBannerItem
import com.petbulance.presentation.screen.feature.community.components.CommunityTopBar
import com.petbulance.presentation.screen.feature.community.components.SortDropdown
import com.petbulance.presentation.screen.feature.community.components.TopicFilterChips
import com.petbulance.presentation.screen.feature.community.search.components.PostListItem

@Composable
fun CommunityMainView(
    navController: NavController,
    argument: CommunityArgument,
    data: CommunityData,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem != null && lastVisibleItem.index >= totalItems - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && data.hasNext && argument.dataState != CommunityDataState.LoadingMore) {
            argument.intent(CommunityIntent.LoadMorePosts)
        }
    }

    Scaffold(
        topBar = {
            CommunityTopBar(
                selectedAnimalType = data.currentType?.let { type ->
                    AnimalCategory.entries.find { it.name == type }
                },
                onAnimalTypeSelected = { category ->
                    argument.intent(CommunityIntent.FilterByType(category?.name))
                },
                onSearchClick = {
                    argument.intent(CommunityIntent.NavigateToSearch)
                },
                onNotificationClick = {
                    argument.intent(CommunityIntent.NavigateToNotifications)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.COMMUNITY,
                navController = navController
            )
        },
        floatingActionButton = {
            BasicFabIcon(
                iconResource = IconResource.Vector(Icons.Default.Add),
                onClick = {
                    argument.intent(CommunityIntent.NavigateToCreatePost)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = colorScheme.bg.frame.default,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CommunityScreenContents(
                argument = argument,
                data = data,
                listState = listState
            )
        }
    }
}

@Composable
private fun CommunityScreenContents(
    argument: CommunityArgument,
    data: CommunityData,
    listState: LazyListState,
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.bg.frame.default)
        ) {
            TopicFilterChips(
                selectedTopic = data.currentTopic?.let { topic ->
                    PostCategory.entries.find { it.name == topic }
                },
                onTopicSelected = { category ->
                    argument.intent(CommunityIntent.FilterByTopic(category?.name))
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = spacingXS),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortDropdown(
                    selectedSort = data.currentSort,
                    onSortSelected = { sort ->
                        argument.intent(CommunityIntent.ChangeSort(sort))
                    }
                )
            }
        }

        if (data.noticeBanner != null) {
            NoticeBannerItem(
                banner = data.noticeBanner,
                onClick = {
                    argument.intent(
                        CommunityIntent.NavigateToNotice(
                            data.noticeBanner.noticeId
                        )
                    )
                }
            )
        }

        when {
            argument.dataState == CommunityDataState.Loading && data.posts.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            data.posts.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 게시글이 없어요",
                        style = typography.bodyLarge,
                        color = colorScheme.text.tertiary
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = data.posts,
                        key = { it.id }
                    ) { post ->
                        PostListItem(
                            post = post,
                            onPostClick = {
                                argument.intent(
                                    CommunityIntent.NavigateToPostDetail(post.id)
                                )
                            },
                            onLikeClick = { argument.intent(CommunityIntent.ToggleLike(post.id)) },
                        )
                        CommonDivider()
                    }

                    if (argument.dataState == CommunityDataState.LoadingMore) {
                        item(key = "loading_more") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}