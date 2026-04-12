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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.model.feature.home.HomeScreenReview
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.PostCategory
import com.petbulance.domain.utils.LOGGER_TAG
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.Space16
import com.petbulance.presentation.component.ui.atom.BaseCarousel
import com.petbulance.presentation.component.ui.atom.BasicBottomSheet
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.StarRatingView
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.molecule.ComingSoonPlaceholder
import com.petbulance.presentation.component.ui.molecule.SectionErrorView
import com.petbulance.presentation.component.ui.molecule.SectionLoadingPlaceholder
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.PullToRefreshContainer
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingSmall
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.screen.feature.home.composables.HomeScreenEmptyStateUi
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsContent
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsData
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsDetailOverlay
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsEvent
import com.petbulance.presentation.screen.nonfeature.login.terms.TermsIntent
import com.petbulance.presentation.utils.SectionLoadState
import com.petbulance.presentation.analytics.AnalyticsEvents
import com.petbulance.presentation.analytics.LocalAnalyticsTracker
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

                else -> {}
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
                argument = argument,
                data = data,
                navigateToReviewDetail = { reviewId ->
                    navController.safeNavigate(ScreenDestinations.Review.Detail.createRoute(reviewId))
                },
                onNavigateToSearch = { navController.safeNavigate(ScreenDestinations.Search.route) },
                navigateToHospitalSearchPageWithAnimalType = { animalCategory ->
                    navController.safeNavigate(ScreenDestinations.Search.createRoute(animalCategory))
                },
                onNavigateToReview = { navController.safeNavigate(ScreenDestinations.Review.route) },
                onNavigateToCommunity = { navController.safeNavigate(ScreenDestinations.Community.route) },
                onPostClick = { postId ->
                    navController.safeNavigate(
                        ScreenDestinations.Community.PostDetail.createRoute(
                            postId
                        )
                    )
                },
                onBannerClick = { noticeId ->
                    navController.safeNavigate(
                        ScreenDestinations.MyPage.Help.Notice.Detail.createRoute(
                            noticeId
                        )
                    )
                }
            )
        }
    }

    val onDismissRequest = {
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
    argument: HomeArgument,
    data: HomeData,
    onNavigateToSearch: () -> Unit,
    navigateToReviewDetail: (Long) -> Unit,
    navigateToHospitalSearchPageWithAnimalType: (AnimalCategory) -> Unit,
    onNavigateToReview: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onPostClick: (Long) -> Unit,
    onBannerClick: (Long) -> Unit
) {
    val scrollState = rememberScrollState()

    PullToRefreshContainer(
        scrollState = scrollState,
        onRefresh = {
            argument.intent(HomeIntent.RetryReviews)
            argument.intent(HomeIntent.RetryBanners)
            argument.intent(HomeIntent.RetryHotArticles)
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            HospitalShortcut(
                onClicked = onNavigateToSearch,
                banners = data.homeBanners,
                bannerState = argument.bannerState,
                onRetryBanners = { argument.intent(HomeIntent.RetryBanners) },
                navigateToHospitalSearchPageWithAnimalType = navigateToHospitalSearchPageWithAnimalType,
                onBannerClick = onBannerClick
            )

            HospitalReviewShortcut(
                reviews = data.recentReviews,
                reviewState = argument.reviewState,
                onRetryReviews = { argument.intent(HomeIntent.RetryReviews) },
                onClicked = onNavigateToReview,
                onReviewItemClicked = { reviewId ->
                    navigateToReviewDetail(reviewId)
                }
            )

            HotArticlesShortcut(
                posts = data.hotArticles,
                hotArticleState = argument.hotArticleState,
                onRetryHotArticles = { argument.intent(HomeIntent.RetryHotArticles) },
                onClicked = onNavigateToCommunity,
                onPostClick = onPostClick
            )
        }
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
            .clickable(onClick = onClicked)
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
            style = typography.bodyLarge.emp(),
        )
        BasicIcon(
            iconResource = IconResource.Vector(Icons.Default.ChevronRight),
            contentDescription = "Move to Hospital Search Page",
            size = iconSizeMedium,
            tint = colorScheme.icon.dark
        )
    }
}

@Composable
private fun HospitalShortcut(
    onClicked: () -> Unit,
    navigateToHospitalSearchPageWithAnimalType: (AnimalCategory) -> Unit,
    banners: List<HomeBanner>,
    bannerState: SectionLoadState,
    onRetryBanners: () -> Unit,
    onBannerClick: (Long) -> Unit
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
        when (bannerState) {
            is SectionLoadState.Loading -> {
                SectionLoadingPlaceholder(height = 180.dp)
            }

            is SectionLoadState.Error -> {
                SectionErrorView(
                    message = bannerState.message,
                    onRetry = onRetryBanners,
                    height = 180.dp
                )
            }

            else -> {
                HospitalNoticeSlider(banners, onBannerClick)
            }
        }
    }
}

@Composable
private fun HospitalShortcutAnimalRow(
    navigateToHospitalSearchPageWithAnimalType: (AnimalCategory) -> Unit
) {
    val analyticsTracker = LocalAnalyticsTracker.current
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
                    analyticsTracker.trackEvent(
                        AnalyticsEvents.SELECT_PET_CATEGORY_HOME,
                        mapOf(AnalyticsEvents.Params.PET_TYPE to category[idx].korean)
                    )
                    navigateToHospitalSearchPageWithAnimalType(category[idx])
                }
            ) {
                AnimalCategoryCircle(resourceId = image)
                Text(
                    text = category[idx].korean,
                    color = colorScheme.text.primary,
                    style = typography.bodySmall.emp(),
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
private fun HospitalNoticeSlider(
    banners: List<HomeBanner>,
    onBannerClick: (Long) -> Unit
) {
    if (banners.isEmpty()) return

    BaseCarousel(
        items = banners,
        contentPadding = PaddingValues(horizontal = spacingMedium, vertical = spacingSmall),
        itemSpacing = 16.dp,
        isIndicatorVisible = true,
        autoScroll = true,
        autoScrollInterval = 3000L
    ) { _, item ->
        BasicImageBox(
            uri = item.imageUrl.toUri(),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 180.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onBannerClick(item.noticeId) },
        )
    }
}

@Composable
private fun HospitalReviewShortcut(
    reviews: List<HomeScreenReview>,
    reviewState: SectionLoadState,
    onRetryReviews: () -> Unit,
    onClicked: () -> Unit,
    onReviewItemClicked: (Long) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "최신 영수증 후기",
            onClicked = onClicked
        )
        when (reviewState) {
            is SectionLoadState.Loading -> {
                SectionLoadingPlaceholder(height = 150.dp)
            }

            is SectionLoadState.Error -> {
                SectionErrorView(
                    message = reviewState.message,
                    onRetry = onRetryReviews,
                    height = 150.dp
                )
            }

            else -> {
                RecentReviewSlider(reviews, onReviewItemClicked)
            }
        }
    }
}

@Composable
private fun RecentReviewSlider(
    reviews: List<HomeScreenReview>,
    onReviewItemClicked: (Long) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (reviews.isEmpty()) {
            HomeScreenEmptyStateUi()
        } else {
            BaseCarousel(
                items = reviews.take(3),
                contentPadding = PaddingValues(horizontal = spacingMedium, vertical = spacingXS),
                itemSpacing = spacingXS
            ) { _, item ->
                RecentReviewSliderItem(item, onReviewItemClicked)
            }
        }
    }
}

@Composable
private fun RecentReviewSliderItem(
    item: HomeScreenReview,
    onReviewItemClicked: (Long) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.subtle,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onReviewItemClicked(item.id) }
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
                style = typography.bodyMedium.emp(),
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
                    style = typography.labelMedium,
                    color = colorScheme.text.tertiary,
                )
                Text(
                    text = "${item.reviewCount}",
                    style = typography.labelMedium,
                    color = colorScheme.text.tertiary,
                )
            }
            Text(
                text = item.content,
                style = typography.labelSmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = colorScheme.text.primary,
            )
        }
    }
}

@Composable
private fun HotArticlesShortcut(
    posts: List<PostSummary>,
    hotArticleState: SectionLoadState,
    onRetryHotArticles: () -> Unit,
    onClicked: () -> Unit,
    onPostClick: (Long) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "커뮤니티 인기 게시글",
            onClicked = onClicked
        )

        when (hotArticleState) {
            is SectionLoadState.Loading -> {
                SectionLoadingPlaceholder(height = 200.dp)
            }

            is SectionLoadState.Error -> {
                SectionErrorView(
                    message = hotArticleState.message,
                    onRetry = onRetryHotArticles,
                    height = 200.dp
                )
            }

            is SectionLoadState.Success -> {
                if (posts.isEmpty()) {
                    ComingSoonPlaceholder()
                } else {
                    Column {
                        posts.forEachIndexed { index, post ->
                            HotArticleItem(
                                post = post,
                                onClick = { onPostClick(post.id) }
                            )
                            if (index < posts.lastIndex) {
                                CommonDivider()
                            }
                        }
                    }
                }
            }

            else -> {
                ComingSoonPlaceholder()
            }
        }
    }
}

@Composable
private fun HotArticleItem(
    post: PostSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animalKorean = AnimalCategory.entries.find { it.name == post.type }?.korean ?: post.type
    val topicKorean = PostCategory.entries.find { it.name == post.topic }?.korean ?: post.topic

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = spacingXL, vertical = spacingSmall),
        verticalArrangement = Arrangement.spacedBy(spacingXXS)
    ) {
        Text(
            text = post.title,
            style = typography.bodyMedium.emp(),
            color = colorScheme.text.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = animalKorean,
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
            Dot(dotColor = colorScheme.icon.veryLight)
            Text(
                text = topicKorean,
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
            Dot(dotColor = colorScheme.icon.veryLight)
            Text(
                text = post.createdAt,
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "좋아요 ${post.likeCount}",
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
            Dot(dotColor = colorScheme.icon.veryLight)
            Text(
                text = "댓글 ${post.commentCount}",
                style = typography.labelLarge,
                color = colorScheme.text.caption
            )
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
                reviewState = SectionLoadState.Success,
                bannerState = SectionLoadState.Success,
                hotArticleState = SectionLoadState.Success,
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