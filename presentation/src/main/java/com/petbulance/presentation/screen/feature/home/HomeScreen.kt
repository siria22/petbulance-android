package com.petbulance.presentation.screen.feature.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.home.HomeScreenReview
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.utils.LOGGER_TAG
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.Space16
import com.petbulance.presentation.component.ui.atom.BaseCarousel
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.StarRatingView
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsContent
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsData
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsDetailOverlay
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsEvent
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsIntent
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    argument: HomeArgument,
    data: HomeData,
    checkTermsInitialState: Boolean,
    termsData: TermsData,
    termsIntent: (TermsIntent) -> Unit,
    termsEvent: SharedFlow<TermsEvent>
) {
    var showTermsSheet by rememberSaveable { mutableStateOf(checkTermsInitialState) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(termsEvent) {
        termsEvent.collect { event ->
            when (event) {
                is TermsEvent.NavigateToNext -> {
                    showTermsSheet = false
                }

                is TermsEvent.DataFetch.Error -> {

                }
            }
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is HomeEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "펫뷸런스",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = false,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Filled.NotificationsNone)) {
                            /* TODO : Notification page */
                        }
                    ),
                    shouldEmphasized = true
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.HOME,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeScreenContents(
                data = data,
                onNavigateToSearch = { navController.safeNavigate(ScreenDestinations.Search.route) },
                navigateToHospitalSearchPageWithAnimalType = { animalCategory ->
                    navController.safeNavigate(ScreenDestinations.Search.createRoute(animalCategory))
                },
                onNavigateToReview = { /* TODO: navController.navigate(...) */ },
                onNavigateToCommunity = { /* TODO: navController.navigate(...) */ },
            )
        }
    }

    val onDismissRequest = {
        // TODO: 정책 확정 시 추가 처리
        Log.d("$LOGGER_TAG - HomeScreen", "Terms sheet dismissed without full agreement")
        showTermsSheet = false
    }

    if (showTermsSheet) {
        BasicBottomSheet(
            showBottomSheet = true,
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            TermsContent(
                data = termsData,
                onIntent = termsIntent,
                onCancel = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (termsData.currentTerm != null) {
            TermsDetailOverlay(
                termsData.currentTerm,
                onDismissRequest = { termsIntent(TermsIntent.OnCloseDetail) }
            )
        }
    }
}

@Composable
private fun HomeScreenContents(
    data: HomeData,
    onNavigateToSearch: () -> Unit,
    navigateToHospitalSearchPageWithAnimalType: (AnimalCategory) -> Unit,
    onNavigateToReview: () -> Unit,
    onNavigateToCommunity: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HospitalShortcut(
            onClicked = onNavigateToSearch,
            navigateToHospitalSearchPageWithAnimalType = navigateToHospitalSearchPageWithAnimalType
        )

        HospitalReviewShortcut(
            reviews = data.recentReviews,
            onClicked = onNavigateToReview
        )

        HotArticlesShortcut(
            posts = data.hotArticles,
            onClicked = onNavigateToCommunity
        )
    }
}


@Composable
private fun CommonHeader(
    headerText: String,
    onClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = spacingXS,
                bottom = spacingXS,
                start = spacingMedium,
                end = spacingXS
            )
    ) {
        Text(
            text = headerText,
            color = colorScheme.text.primary,
            style = MaterialTheme.typography.bodyLarge.emp(),
        )
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.ChevronRight),
            contentDescription = "Move to Hospital Search Page",
            size = iconSizeMedium,
            tint = colorScheme.icon.dark,
            modifier = Modifier.clickable { onClicked() }
        )
    }
}

@Composable
private fun HospitalShortcut(
    onClicked: () -> Unit,
    navigateToHospitalSearchPageWithAnimalType: (AnimalCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "내 주변 병원 바로가기",
            onClicked = onClicked
        )
        HospitalShortcutAnimalRow(
            navigateToHospitalSearchPageWithAnimalType = navigateToHospitalSearchPageWithAnimalType
        )
        HospitalNoticeSlider()
    }
}

@Composable
private fun HospitalShortcutAnimalRow(
    navigateToHospitalSearchPageWithAnimalType: (AnimalCategory) -> Unit
) {
    val images = listOf(
        painterResource(R.drawable.img_all),
        painterResource(R.drawable.img_small_mammals),
        painterResource(R.drawable.img_birds),
        painterResource(R.drawable.img_reptiles),
        painterResource(R.drawable.img_amphibias),
        painterResource(R.drawable.img_fishes)
    )

    val category = AnimalCategory.entries

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Space16()
        images.forEachIndexed { idx, image ->
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXXS),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    navigateToHospitalSearchPageWithAnimalType(category[idx])
                }
            ) {
                AnimalCategoryCircle(resourceId = image)
                Text(
                    text = category[idx].korean,
                    color = colorScheme.text.primary,
                    style = MaterialTheme.typography.bodySmall.emp(),
                )
            }
        }
        Space16()
    }
}

@Composable
private fun AnimalCategoryCircle(resourceId: Painter) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.medium,
                shape = CircleShape
            )
            .padding(spacingXXS)
    ) {
        Image(
            painter = resourceId,
            contentDescription = "Animal Category",
            alignment = Alignment.Center,
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
private fun HospitalNoticeSlider() {
    /* Mocked ads items */
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

    BaseCarousel(
        items = items,
        contentPadding = PaddingValues(horizontal = spacingMedium, vertical = spacingSmall),
        itemSpacing = 16.dp,
        isIndicatorVisible = true
    ) { _, item ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item)
        }
    }
}

@Composable
private fun HospitalReviewShortcut(
    reviews: List<HomeScreenReview>,
    onClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "최신 영수증 후기",
            onClicked = onClicked
        )
        RecentReviewSlider(reviews)
    }
}

@Composable
private fun RecentReviewSlider(reviews: List<HomeScreenReview>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (reviews.isEmpty()) {
            Text(
                text = "내용이 없습니다.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall.emp()
            )
        } else {
            BaseCarousel(
                items = reviews.take(3),
                contentPadding = PaddingValues(horizontal = spacingMedium, vertical = spacingXS),
                itemSpacing = spacingXS
            ) { _, item ->
                RecentReviewSliderItem(item)
            }
        }
    }
}

@Composable
private fun RecentReviewSliderItem(item: HomeScreenReview) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.subtle,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = spacingMedium, horizontal = spacingSmall)
    ) {
        if (item.image != null) {
            BasicImageBox(
                uri = item.image?.toUri(),
                size = 100.dp,
                modifier = Modifier.clip(RoundedCornerShape(4.dp))
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = item.hospitalName,
                style = MaterialTheme.typography.bodyMedium.emp(),
                color = colorScheme.text.primary,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                StarRatingView(rating = item.rating)
                Text(
                    text = "(${item.rating})",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.text.tertiary,
                )
                Text(
                    text = "${item.reviewCount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.text.tertiary,
                )
            }
            Text(
                text = item.content,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = colorScheme.text.primary,
            )
        }
    }
}

@Composable
private fun HotArticlesShortcut(
    posts: List<PostDetail>?,
    onClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "인기 게시글",
            onClicked = onClicked
        )

        if (posts.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(vertical = spacingSmall),
                text = "내용이 없습니다.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall.emp()
            )
        } else {
            posts.forEach { post ->
                HotArticlesItem(post = post)
            }
        }
    }
}

@Composable
private fun HotArticlesItem(post: PostDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingMedium, vertical = spacingXXS)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = colorScheme.bg.frame.subtle,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(
                    horizontal = spacingMedium,
                    vertical = spacingSmall
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = post.postInfo.title,
                    style = MaterialTheme.typography.bodySmall.emp(),
                    color = colorScheme.text.primary,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post.boardInfo.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.text.caption,
                        )
                        Dot()
                        Text(
                            text = post.boardInfo.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.text.caption,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post.postInfo.createdAt,
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.text.caption,
                        )
                        Dot()
                        Text(
                            text = "조회 ${post.postInfo.stats.viewCount}",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.text.caption,
                        )
                    }
                }

            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = colorScheme.bg.frame.default,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .size(48.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = post.postInfo.stats.commentCount.toString(),
                        style = MaterialTheme.typography.bodyMedium.emp(),
                        color = colorScheme.status.success.default,
                    )
                    Text(
                        text = "댓글",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.text.caption,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PetbulanceTheme {
        HomeScreen(
            navController = rememberNavController(),
            argument = HomeArgument(
                intent = { },
                dataState = HomeDataState.Init,
                screenState = HomeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.stub,
            checkTermsInitialState = false,
            termsData = TermsData.stub(),
            termsIntent = { },
            termsEvent = MutableSharedFlow()
        )
    }
}