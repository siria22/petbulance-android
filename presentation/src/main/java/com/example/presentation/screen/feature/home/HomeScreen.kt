package com.example.presentation.screen.feature.home

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.domain.model.feature.community.post.PostDetail
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.Dot
import com.example.presentation.component.ui.Space16
import com.example.presentation.component.ui.atom.BaseCarousel
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.BasicImageBox
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.atom.RatingBar
import com.example.presentation.component.ui.iconSizeMedium
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.utils.error.collectCustomErrors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun HomeScreen(
    navController: NavController,
    argument: HomeArgument,
    data: HomeData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dataState = argument.dataState
    val screenState = argument.screenState

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

            )
        }
    }
}

@Composable
private fun HomeScreenContents(

) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HospitalShortcut()

        HospitalReviewShortcut(
            reviews = listOf(HospitalReview.stub)
        )

        HotArticlesShortcut(
            post = PostDetail.stub
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
private fun HospitalShortcut() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "내 주변 병원 바로가기",
            onClicked = { } // TODO : Navigate to Hospital Search Page
        )
        HospitalShortcutAnimalRow()
        HospitalNoticeSlider()
    }
}

@Composable
private fun HospitalShortcutAnimalRow() {
    val images = listOf(
        painterResource(R.drawable.img_all),
        painterResource(R.drawable.img_small_mammals),
        painterResource(R.drawable.img_birds),
        painterResource(R.drawable.img_reptiles),
        painterResource(R.drawable.img_amphibias),
        painterResource(R.drawable.img_fishes)
    )

    val category = listOf(
        "전체",
        "소형포유류",
        "조류",
        "파충류",
        "양서류",
        "어류"
    )

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
            ) {
                AnimalCategoryCircle(resourceId = image)
                Text(
                    text = category[idx],
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
private fun HospitalReviewShortcut(reviews: List<HospitalReview>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "최신 영수증 후기",
            onClicked = { } // TODO : Navigate to Review Page
        )
        RecentReviewSlider(reviews)
    }
}

@Composable
private fun RecentReviewSlider(reviews: List<HospitalReview>) {
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
private fun RecentReviewSliderItem(item: HospitalReview) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier
            .background(
                color = colorScheme.bg.frame.subtle,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = spacingMedium, horizontal = spacingSmall)
    ) {
        BasicImageBox(
            uri = item.imageUrls.firstOrNull()?.toUri(),
            size = 100.dp,
            modifier = Modifier.clip(RoundedCornerShape(4.dp))
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(spacingXXS),
        ) {
            Text(
                text = "item.hospitalName", // TODO : Missing field - 병원 이름
                style = MaterialTheme.typography.bodyMedium.emp(),
                color = colorScheme.text.primary,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RatingBar(rating = item.rating)
                Text(
                    text = "(${item.rating})",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.text.tertiary,
                )
                Text(
                    text = "후기 124", // TODO : Missing field - 후기 개수
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
private fun HotArticlesShortcut(post: PostDetail?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonHeader(
            headerText = "최신 영수증 후기",
            onClicked = { } // TODO : Navigate to CommunityPage
        )

        if (post != null) {
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
        } else {
            Text(
                text = "내용이 없습니다.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall.emp()
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
                dataState = HomeDataState.Init,
                screenState = HomeScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = HomeData.stub
        )
    }
}