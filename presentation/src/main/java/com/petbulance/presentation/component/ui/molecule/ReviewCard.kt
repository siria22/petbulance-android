package com.petbulance.presentation.component.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.presentation.R
import com.petbulance.presentation.component.theme.PetbulancePrimitives
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicChip
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.BasicImageBox
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.atom.StarRatingView
import com.petbulance.presentation.component.ui.iconSizeMedium
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingMedium
import com.petbulance.presentation.component.ui.spacingXS
import com.petbulance.presentation.component.ui.spacingXXS
import java.util.Locale

@Composable
fun ReviewCard(review: HospitalReview, onReviewClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXS),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacingLarge, horizontal = spacingMedium)
            .clickable { onReviewClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingXXS)
            ) {
                BasicChip(
                    text = review.detailAnimalType.korean,
                )
                Dot(dotColor = PetbulancePrimitives.Gray.p300)
                Text(
                    text = "review.author",
                    color = colorScheme.text.caption,
                    style = typography.labelMedium
                )
                Dot(dotColor = PetbulancePrimitives.Gray.p300)
                Text(
                    text = "review.date",
                    color = colorScheme.text.caption,
                    style = typography.bodySmall
                )
            }
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.MoreVert),
                size = iconSizeMedium,
                contentDescription = "More",
                tint = colorScheme.icon.light
                //TODO : More 버튼 클릭 시
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.TopStart) {
                BasicImageBox(
                    size = 90.dp,
                    uri = review.imageUrls.firstOrNull()?.toUri(),
                    errorImageResource = R.drawable.img_checker,
                    placeholderImageResource = R.drawable.img_checker,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                )

                if (review.imageUrls.size > 1) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.imageUrls.size.toString(),
                            color = Color.White,
                            style = typography.labelMedium
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(spacingXS)) {
                Text(
                    text = review.hospitalName,
                    color = colorScheme.text.secondary,
                    style = typography.titleSmall.emp()
                )

                StarRatingView(rating = review.rating)

                Text(
                    text = "${review.animalType.korean} > ${review.detailAnimalType.korean}",
                    color = colorScheme.text.secondary,
                    style = typography.bodySmall
                )
                Text(
                    text = "결제금액 ${String.format(Locale.KOREA, "%,d", review.price)}원",
                    color = colorScheme.text.secondary,
                    style = typography.bodySmall
                )
            }
        }

        Text(
            text = review.content,
            color = colorScheme.text.secondary,
            style = typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (review.isReceiptVerified) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = colorScheme.text.caption,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "영수증인증 완료",
                        color = colorScheme.tag.trust.medium,
                        style = typography.labelSmall
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.CheckCircle),
                        contentDescription = "verified",
                        size = 12.dp,
                        tint = colorScheme.tag.trust.medium,
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(4.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val color =
                    if (review.isLiked) colorScheme.status.success.default else colorScheme.text.caption
                Text(
                    text = "도움이 됐어요",
                    color = color,
                    style = typography.labelMedium
                )
                if (review.isLiked) {
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.ic_thumbs_up_double_filled),
                        contentDescription = "Helpful_filled",
                        size = 16.dp,
                        tint = color
                    )
                } else {
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.ic_thumbs_up_double),
                        contentDescription = "Helpful",
                        size = 16.dp,
                        tint = color
                    )
                }
                Text(
                    text = review.likeCount.toString(),
                    color = color,
                    style = typography.labelMedium
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ReviewCardPreview() {
    PetbulanceTheme {
        ReviewCard(HospitalReview.stub(), {})
    }
}