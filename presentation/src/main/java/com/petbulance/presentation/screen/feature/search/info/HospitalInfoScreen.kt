package com.petbulance.presentation.screen.feature.search.info

import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.molecule.ReviewCard
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.screen.feature.search.info.views.DetailTab
import com.petbulance.presentation.screen.feature.search.info.views.EmptyReviewView
import com.petbulance.presentation.screen.feature.search.info.views.ReviewHeader
import com.petbulance.presentation.screen.feature.search.main.views.common.HospitalCard
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.ScreenDestinations
import com.petbulance.presentation.utils.nav.safeNavigate
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.flow.MutableSharedFlow

private const val HOSPITAL_SHARE_URL_PREFIX = "https://petbulance.com/hospital/"

@Composable
fun HospitalInfoScreen(
    navController: NavController,
    argument: HospitalInfoArgument,
    data: HospitalInfoData,
    currentLocation: Location?,
    isLoggedIn: Boolean
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val hospitalData = data.hospitalUiData
    val reviewData = data.reviewUiData

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is HospitalInfoEvent.DataFetch.Error -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "병원 상세 정보",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    leadingIconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Outlined.Share)) {
                            hospitalData.hospital?.let { hospital ->
                                try {
                                    val shareText = buildString {
                                        append("${hospital.name}\n")
                                        hospitalData.hospitalDetail?.address?.let { append("주소: $it\n") }
                                        append("$HOSPITAL_SHARE_URL_PREFIX${hospital.hospitalId}")
                                    }
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                } catch (e: Exception) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("공유하기 기능을 사용할 수 없습니다")
                                    }
                                }
                            }
                        }
                    ),
                    isShadowed = true
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.SEARCH,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HospitalInfoScreenContents(
                navController = navController,
                hospital = hospitalData.hospital,
                hospitalDetail = hospitalData.hospitalDetail,
                reviewUiData = reviewData,
                currentLocation = currentLocation,
                onIntent = argument.intent,
                snackbarHostState = snackbarHostState,
                onNavigateButtonClicked = {
                    hospitalData.hospital?.let { hospital ->
                        navController.safePopBackStack()
                        navController.safeNavigate(
                            ScreenDestinations.Search.createRoute(initialHospitalId = hospital.hospitalId)
                        )
                    }
                },
                onReviewClicked = { reviewId ->
                    navController.safeNavigate(ScreenDestinations.Review.Detail.createRoute(reviewId))
                },
                isLoggedIn = isLoggedIn
            )
        }
    }
}

private enum class TabType(val title: String) {
    DETAILS("상세정보"),
    REVIEWS("방문 후기")
}

@Composable
private fun HospitalInfoScreenContents(
    navController: NavController,
    hospital: Hospital?,
    hospitalDetail: HospitalDetail?,
    reviewUiData: ReviewUiData,
    onIntent: (HospitalInfoIntent) -> Unit,
    currentLocation: Location?,
    snackbarHostState: SnackbarHostState,
    onNavigateButtonClicked: () -> Unit,
    onReviewClicked: (Long) -> Unit,
    isLoggedIn: Boolean
) {
    var selectedTab by remember { mutableStateOf(TabType.DETAILS) }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val commonPadding = 16.dp

    // 무한 스크롤 트리거
    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            selectedTab == TabType.REVIEWS &&
                    totalItems > 0 &&
                    lastVisibleItem >= totalItems - 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onIntent(HospitalInfoIntent.LoadMoreReviews)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                HospitalCard(
                    hospital = hospital,
                    borderColor = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                InfoTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.padding(horizontal = commonPadding)
                )
            }

            item {
                CommonDivider()
            }

            if (selectedTab == TabType.DETAILS) {
                item {
                    DetailTab(
                        hospitalDetail = hospitalDetail,
                        currentLocation = currentLocation ?: Location("").apply {
                            latitude = 37.5; longitude = 127.0
                        },
                        onNavigateButtonClicked = onNavigateButtonClicked
                    )
                }
            } else {
                item {
                    ReviewHeader(
                        reviewData = reviewUiData,
                        onIntent = onIntent
                    )
                }

                if (reviewUiData.reviews.isEmpty()) {
                    item {
                        EmptyReviewView()
                    }
                } else {
                    items(
                        items = reviewUiData.reviews,
                        key = { it.id }
                    ) { review ->
                        CommonDivider(colorScheme.border.subtle)
                        ReviewCard(
                            review = review,
                            onReviewClicked = { onReviewClicked(review.id) }
                        )
                    }
                }
            }
        }

        BottomActionButton(
            text = if (selectedTab == TabType.DETAILS) "전화 문의하기" else "병원 후기 작성하기",
            onClicked = {
                if (selectedTab == TabType.DETAILS) {
                    val phone = hospital?.phone
                    if (phone.isNullOrBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("전화번호 정보가 없습니다")
                        }
                    } else {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$phone")
                        }
                        context.startActivity(intent)
                    }
                } else {
                    if (isLoggedIn) {
                        navController.safeNavigate(ScreenDestinations.Review.route)
                    } else {
                        navController.safeNavigate(ScreenDestinations.Login.route)
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun InfoTabRow(
    selectedTab: TabType,
    onTabSelected: (TabType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TabType.entries.forEach { tab ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(tab) }
                    .drawBehind {
                        if (selectedTab == tab) {
                            val strokeWidth = 2.dp.toPx()
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, size.height - strokeWidth / 2),
                                end = Offset(size.width, size.height - strokeWidth / 2),
                                strokeWidth = strokeWidth
                            )
                        }
                    }
            ) {
                Text(
                    text = tab.title,
                    color = if (selectedTab == tab) colorScheme.text.primary else colorScheme.text.disabled,
                    style = typography.bodySmall.emp(),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomActionButton(
    text: String,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(colorScheme.bg.frame.default)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicButton(
            text = text,
            buttonType = BasicButtonType.PRIMARY,
            size = BasicButtonSize.XL,
            onClicked = onClicked,
            modifier = Modifier.fillMaxWidth(),
            radius = 16.dp
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HospitalInfoScreenPreview() {
    PetbulanceTheme {
        HospitalInfoScreen(
            navController = rememberNavController(),
            argument = HospitalInfoArgument(
                intent = { },
                state = HospitalInfoDataState.Init,
                event = MutableSharedFlow(),
            ),
            data = HospitalInfoData.stub(),
            currentLocation = null,
            isLoggedIn = true
        )
    }
}