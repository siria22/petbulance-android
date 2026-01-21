package com.petbulance.presentation.screen.feature.search.info.views

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.model.feature.hospital.hospital.OpenHour
import com.petbulance.presentation.component.theme.PetbulanceTheme.colorScheme
import com.petbulance.presentation.component.theme.emp
import com.petbulance.presentation.component.ui.CommonDivider
import com.petbulance.presentation.component.ui.atom.BasicButton
import com.petbulance.presentation.component.ui.atom.BasicButtonSize
import com.petbulance.presentation.component.ui.atom.BasicButtonType
import com.petbulance.presentation.component.ui.atom.BasicIcon
import com.petbulance.presentation.component.ui.atom.IconResource
import com.petbulance.presentation.utils.NaverMapView
import com.naver.maps.map.NaverMap

@Composable
fun DetailTab(
    hospitalDetail: HospitalDetail?,
    currentLocation: Location,
    onNavigateButtonClicked: () -> Unit
) {
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }
    val commonPadding = 16.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(commonPadding)
    ) {
        Text(
            text = "병원 위치",
            color = colorScheme.text.secondary,
            style = typography.titleSmall.emp()
        )
        if (hospitalDetail != null) {
            Text(
                text = hospitalDetail.address,
                color = colorScheme.text.tertiary,
                style = typography.bodySmall,
            )
        } else {
            Text(
                text = "위치를 찾을 수 없어요",
                color = colorScheme.text.caption,
                style = typography.bodySmall,
            )
        }
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            NaverMapView(
                currentLocation = currentLocation,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                places = if (hospitalDetail != null) listOf(hospitalDetail.toMarker()) else null,
                onMapReady = { map -> naverMap = map },
                onMapBoundsChange = {},
                selectedHospitalId = hospitalDetail?.hospitalId,
                onMarkerClicked = { },
                cameraPosition = Location("").apply {
                    latitude = hospitalDetail?.lat ?: currentLocation.latitude
                    longitude = hospitalDetail?.lng ?: currentLocation.longitude
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Consume touch events */ }
            )
        }
        BasicButton(
            text = "병원 길 찾기",
            buttonType = BasicButtonType.DEFAULT,
            size = BasicButtonSize.M,
            onClicked = onNavigateButtonClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }

    HorizontalDivider(thickness = 12.dp, color = colorScheme.bg.frame.subtle)

    OpenInfo(hospitalDetail?.openHours)

    HorizontalDivider(thickness = 12.dp, color = colorScheme.bg.frame.subtle)

    ProposeModificationsCard()

    CommonDivider(color = colorScheme.border.subtle)
}

@Composable
private fun ProposeModificationsCard() {
    val commonPadding = 16.dp
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(commonPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicIcon(
                iconResource = IconResource.Vector(Icons.Default.Info),
                contentDescription = "Propose Modification",
                size = 28.dp,
                tint = Color.Gray
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "알고 계신 정보와 다른가요?",
                    color = colorScheme.text.primary,
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = "잘못된 정보를 알려주시면 빠르게 반영할게요",
                    color = colorScheme.text.caption,
                    style = typography.bodySmall,
                )
            }
        }
        BasicIcon(
            iconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowRight),
            contentDescription = "navigate to page",
            size = 28.dp,
            tint = colorScheme.icon.basic
        )
    }
}

@Composable
private fun OpenInfo(data: List<OpenHour>?) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "영업 정보",
            color = colorScheme.text.primary,
            style = typography.titleSmall.emp()
        )
        if (data.isNullOrEmpty()) {
            Text(
                text = "영업 정보가 없습니다.",
                color = colorScheme.text.caption,
                style = typography.bodySmall,
            )
            return
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.chunked(2).forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { item ->
                        HourInfoItem(
                            day = item.day,
                            hours = item.hours,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun HourInfoItem(day: String, hours: String, modifier: Modifier = Modifier) {
    val dayColor = when (day) {
        "토" -> colorScheme.tag.blue.strong
        "일", "공휴일" -> colorScheme.tag.red.strong
        else -> colorScheme.text.caption
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = if (day === "공휴일") day else day + "요일",
            color = dayColor,
            style = typography.bodyMedium.emp(),
            modifier = Modifier.weight(0.3f),
            textAlign = TextAlign.Start
        )
        Text(
            text = hours,
            color = colorScheme.text.secondary,
            style = typography.bodyMedium.emp(),
            modifier = Modifier.weight(0.7f),
            textAlign = TextAlign.Start
        )
    }
}