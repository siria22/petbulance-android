package com.petbulance.presentation.screen.feature.search.main.views.common

import android.content.ClipData
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.presentation.component.theme.PetbulanceTheme
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.Dot
import com.petbulance.presentation.component.ui.atom.BasicChip
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.component.ui.spacingLarge
import com.petbulance.presentation.component.ui.spacingXL
import com.petbulance.presentation.component.ui.spacingXS
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HospitalCard(
    modifier: Modifier = Modifier,
    hospital: Hospital?,
    borderColor: Color? = colorScheme.border.subtle,
    isShadowed: Boolean = false,
    onCardClick: () -> Unit = {},
    onCopyPhoneClick: (String) -> Unit = {}
) {
    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .let { modifier ->
                if (isShadowed) {
                    modifier.shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Black.copy(alpha = 0.1f),
                        ambientColor = Color.Black.copy(alpha = 0.1f)
                    )
                } else {
                    modifier
                }
            }
            .background(
                color = colorScheme.bg.frame.default,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onCardClick() }
            .let { modifier ->
                if (borderColor != null) {
                    modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    modifier
                }
            }
            .padding(horizontal = spacingLarge, vertical = spacingXL)
            .heightIn(max = 152.dp),
        verticalArrangement = Arrangement.spacedBy(spacingXS)
    ) {
        if (hospital == null) {
            Text(
                text = "검색된 병원이 없어요.",
                style = typography.titleMedium,
                color = colorScheme.text.primary,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "지역 범위를 넓히거나 필터를 조정해주세요.",
                style = typography.bodyMedium,
                color = colorScheme.text.tertiary,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
            )
            return
        } else {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = hospital.name,
                    style = typography.titleMedium.emp(),
                    color = colorScheme.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Rounded.Star),
                        contentDescription = "Rating star",
                        size = 16.dp,
                        tint = colorScheme.icon.rating
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", hospital.rating),
                        style = typography.labelLarge,
                        color = colorScheme.text.secondary
                    )

                    val reviewCount = hospital.reviewCount ?: 0
                    Text(
                        text = "($reviewCount)",
                        style = typography.labelLarge,
                        color = colorScheme.text.caption
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val isOpen = hospital.isOpenNow
                Text(
                    text = if (isOpen) "진료 중" else "진료 종료",
                    style = typography.labelLarge.emp(),
                    color = if (isOpen) colorScheme.tag.blue.medium else colorScheme.text.caption
                )

                Dot()

                hospital.openHours?.let { hours ->
                    Text(
                        text = hours,
                        style = typography.labelLarge,
                        color = colorScheme.text.secondary
                    )
                }

                Dot()

                hospital.distanceMeters?.let { distance ->
                    val distanceText = if (distance >= 1000) {
                        String.format(Locale.getDefault(), "%.1fkm", distance / 1000)
                    } else {
                        "${distance.toInt()}m"
                    }
                    Text(
                        text = distanceText,
                        style = typography.labelLarge,
                        color = colorScheme.text.caption
                    )
                }
            }

            if (!hospital.phone.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onCopyPhoneClick(hospital.phone!!) }
                ) {
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Call),
                        contentDescription = "Phone",
                        size = 16.dp,
                        tint = colorScheme.tag.blue.medium,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = "tel:${hospital.phone!!}".toUri()
                            }
                            // 3. Activity 시작
                            context.startActivity(intent)
                        }
                    )
                    Text(
                        text = hospital.phone!!,
                        style = typography.labelLarge.emp(),
                        color = colorScheme.tag.blue.medium
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Outlined.ContentCopy),
                        contentDescription = "Copy Phone",
                        size = 16.dp,
                        tint = colorScheme.icon.light,
                        modifier = Modifier.clickable {
                            scope.launch {
                                val clipData = ClipData.newPlainText("Phone", hospital.phone!!)
                                val clipEntry = ClipEntry(clipData)
                                clipboardManager.setClipEntry(clipEntry)
                            }
                        }
                    )
                }
            }

            if (hospital.types.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    hospital.types.forEach { type ->
                        BasicChip(text = type)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HospitalCardPreview() {
    PetbulanceTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(32.dp)
        ) {
            HospitalCard(
                hospital = Hospital.stub,
                isShadowed = true
            )

            HospitalCard(
                hospital = null,
                isShadowed = true
            )
        }
    }
}