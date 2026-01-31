package com.petbulance.presentation.screen.feature.review.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicChip
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.StarRatingView
import com.petbulance.presentation.component.ui.iconSizeSmall
import com.petbulance.presentation.component.ui.organism.AppTopBar
import com.petbulance.presentation.component.ui.organism.BottomNavigationBar
import com.petbulance.presentation.component.ui.organism.CurrentBottomNav
import com.petbulance.presentation.component.ui.organism.TopBarAlignment
import com.petbulance.presentation.component.ui.organism.TopBarInfo
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import com.petbulance.presentation.utils.error.collectCustomErrors
import com.petbulance.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Locale

@Composable
fun ReviewDetailScreen(
    navController: NavController,
    argument: ReviewDetailArgument,
    data: ReviewDetailData
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val dataState = argument.dataState
    val screenState = argument.screenState

    LaunchedEffect(argument.event) {
        argument.event.collectCustomErrors { event ->
            when (event) {
                is ReviewDetailEvent.DataFetch.Error -> {

                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "",
                    textAlignment = TopBarAlignment.CENTER,
                    isLeadingIconAvailable = true,
                    onLeadingIconClicked = { navController.safePopBackStack() },
                    isTrailingIconAvailable = true,
                    trailingIcons = listOf(
                        Pair(IconResource.Vector(Icons.Outlined.Info)) {
                            // TODO : interaction
                        }
                    )
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.REVIEW,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.default
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ReviewDetailScreenContents(data = data)
        }
    }

    // BackHandler {  }
}

@Composable
private fun ReviewDetailScreenContents(data: ReviewDetailData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        ReviewDetailHeaderSection(
            data.animalType,
            data.userNickname,
            data.visitDate
        )

        ReviewDetailSummarySection(
            hospitalName = data.hospitalName,
            rating = data.rating,
            animalType = data.animalType,
            detailAnimalType = data.detailAnimalType,
            price = data.price
        )

        ReviewDetailImagesSection(images = data.images)

        ReviewDetailContent(data.content)

        ReviewDetailLikesSection(
            isLiked = data.isLiked,
            likeCount = data.likeCount,
            onLikeClick = { /* TODO : 좋아요 or 취소 */ }
        )
    }
}

@Composable
private fun ReviewDetailHeaderSection(
    animalType: AnimalCategory,
    userNickname: String,
    visitDate: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicChip(text = animalType.korean)

        Dot()

        Text(
            text = userNickname,
            style = typography.labelMedium,
            color = colorScheme.text.caption,
        )

        Dot()

        Text(
            text = visitDate,
            style = typography.labelMedium,
            color = colorScheme.text.caption,
        )
    }
}

@Composable
private fun ReviewDetailSummarySection(
    hospitalName: String,
    rating: Double,
    animalType: AnimalCategory,
    detailAnimalType: AnimalSpecies,
    price: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacingXS)) {
        Text(
            text = hospitalName,
            color = colorScheme.text.secondary,
            style = typography.titleSmall.emp()
        )

        StarRatingView(rating = rating)

        Text(
            text = "${animalType.korean} > ${detailAnimalType.korean}",
            color = colorScheme.text.secondary,
            style = typography.bodySmall
        )
        Text(
            text = "결제금액 ${String.format(Locale.KOREA, "%,d", price)}원",
            color = colorScheme.text.secondary,
            style = typography.bodySmall
        )
    }
}

@Composable
private fun ReviewDetailImagesSection(images: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium)
    ) {
        images.forEach { img ->
            BasicImageBox(
                modifier = Modifier.fillMaxWidth(),
                uri = img.toUri(),
            )
        }
    }
}

@Composable
private fun ReviewDetailContent(contents: String) {
    Text(
        text = contents,
        style = typography.bodyMedium,
        color = colorScheme.text.secondary
    )
}

@Composable
private fun ReviewDetailLikesSection(
    isLiked: Boolean,
    likeCount: Int,
    onLikeClick: () -> Unit
) {
    val contentColor = if (isLiked) colorScheme.status.success.default else colorScheme.text.caption
    val iconResource =
        if (isLiked) IconResource.Drawable(R.drawable.ic_thumbs_up_double_filled)
        else IconResource.Drawable(R.drawable.ic_thumbs_up_double)

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXS, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onLikeClick).fillMaxWidth()
    ) {
        Text(
            text = "도움이 됐어요",
            style = typography.labelMedium,
            color = contentColor
        )
        BasicIcon(
            iconResource = iconResource,
            contentDescription = "Thumbs up",
            size = iconSizeSmall,
            tint = contentColor
        )
        Text(
            text = "$likeCount",
            style = typography.labelMedium,
            color = contentColor
        )
    }
}

@Preview
@Composable
private fun ReviewDetailScreenPreview() {
    PetbulanceTheme {
        ReviewDetailScreen(
            navController = rememberNavController(),
            argument = ReviewDetailArgument(
                intent = { },
                dataState = ReviewDetailDataState.Init,
                screenState = ReviewDetailScreenState.Init,
                event = MutableSharedFlow()
            ),
            data = ReviewDetailData.empty
        )
    }
}