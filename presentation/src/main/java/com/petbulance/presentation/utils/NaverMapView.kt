package com.petbulance.presentation.utils

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.petbulance.domain.model.feature.hospital.hospital.HospitalMarker
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.presentation.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun NaverMapView(
    currentLocation: Location?,
    cameraPosition: Location?,
    places: List<HospitalMarker>?,
    selectedHospitalId: Long?,
    onMapReady: (NaverMap) -> Unit,
    onMapBoundsChange: (MapBounds) -> Unit,
    onMarkerClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        Box(
            modifier = modifier
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Map Preview")
        }
        return
    }

    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }
    var currentZoomLevel by remember { mutableStateOf(13.0) }

    val markers = remember { mutableStateListOf<Marker>() }

    LaunchedEffect(naverMap, cameraPosition) {
        if (cameraPosition != null) {
            naverMap?.let { map ->
                val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                    LatLng(cameraPosition.latitude, cameraPosition.longitude),
                    15.0
                ).animate(CameraAnimation.Easing)
                map.moveCamera(cameraUpdate)
            }
        }
    }

    // currentLocation이 null이 아닐 때만 위치 오버레이 업데이트
    LaunchedEffect(naverMap, currentLocation) {
        if (currentLocation != null) {
            naverMap?.let { map ->
                val locationOverlay = map.locationOverlay
                locationOverlay.isVisible = true
                locationOverlay.position =
                    LatLng(currentLocation.latitude, currentLocation.longitude)
            }
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    ) {
        it.getMapAsync { map ->
            map.mapType = NaverMap.MapType.Basic
            map.uiSettings.isZoomControlEnabled = true

            // 초기 설정
            with(map.locationOverlay) {
                isVisible = currentLocation != null // 위치 정보 없으면 숨김
                icon = OverlayImage.fromResource(R.drawable.marker_user)
                circleColor = android.graphics.Color.parseColor("#33FF0000")
            }

            map.addOnCameraIdleListener {
                currentZoomLevel = map.cameraPosition.zoom
                val bounds = map.contentBounds
                val mapBounds = MapBounds(
                    minLat = bounds.southWest.latitude,
                    minLng = bounds.southWest.longitude,
                    maxLat = bounds.northEast.latitude,
                    maxLng = bounds.northEast.longitude
                )
                onMapBoundsChange(mapBounds)
            }

            if (naverMap == null) {
                naverMap = map
                onMapReady(map)
            }
        }
    }

    naverMap?.let { map ->
        LaunchedEffect(places, selectedHospitalId, currentZoomLevel) {
            markers.forEach { it.map = null }
            markers.clear()

            if (places.isNullOrEmpty()) return@LaunchedEffect

            // 줌 레벨 13 미만일 때 클러스터링 활성화
            val shouldCluster = currentZoomLevel < 13.0

            if (shouldCluster) {
                // 클러스터링 로직
                val clusters = clusterMarkers(places, selectedHospitalId)
                clusters.forEach { cluster ->
                    if (cluster.count == 1) {
                        // 단일 마커
                        val place = cluster.markers.first()
                        val isSelected = place.hospitalId == selectedHospitalId
                        val iconRes = if (isSelected) {
                            if (place.isOpened) R.drawable.marker_selected
                            else R.drawable.marker_selected_closed
                        } else {
                            if (place.isOpened) R.drawable.marker_open
                            else R.drawable.marker_closed
                        }

                        val newMarker = Marker().apply {
                            position = LatLng(place.latitude, place.longitude)
                            icon = OverlayImage.fromResource(iconRes)
                            this.map = map
                            onClickListener = Overlay.OnClickListener {
                                onMarkerClicked(place.hospitalId)
                                true
                            }
                        }
                        markers.add(newMarker)
                    } else {
                        // 클러스터 마커
                        val clusterMarker = Marker().apply {
                            position = LatLng(cluster.centerLat, cluster.centerLng)
                            // TODO: 클러스터 전용 아이콘 추가 필요 (현재는 기본 마커 사용)
                            icon = OverlayImage.fromResource(R.drawable.marker_open)
                            captionText = "${cluster.count}"
                            captionTextSize = 14f
                            captionColor = android.graphics.Color.WHITE
                            this.map = map
                            // 클러스터 클릭 시 줌인
                            onClickListener = Overlay.OnClickListener {
                                val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                    LatLng(cluster.centerLat, cluster.centerLng),
                                    currentZoomLevel + 2.0
                                ).animate(CameraAnimation.Easing)
                                map.moveCamera(cameraUpdate)
                                true
                            }
                        }
                        markers.add(clusterMarker)
                    }
                }
            } else {
                // 개별 마커 표시
                places.forEach { place ->
                    val isSelected = place.hospitalId == selectedHospitalId
                    val iconRes = if (isSelected) {
                        if (place.isOpened) R.drawable.marker_selected
                        else R.drawable.marker_selected_closed
                    } else {
                        if (place.isOpened) R.drawable.marker_open
                        else R.drawable.marker_closed
                    }

                    val newMarker = Marker().apply {
                        position = LatLng(place.latitude, place.longitude)
                        icon = OverlayImage.fromResource(iconRes)
                        this.map = map
                        onClickListener = Overlay.OnClickListener {
                            onMarkerClicked(place.hospitalId)
                            true
                        }
                    }
                    markers.add(newMarker)
                }
            }
        }
    }
}

data class Cluster(
    val centerLat: Double,
    val centerLng: Double,
    val count: Int,
    val markers: List<HospitalMarker>
)

fun clusterMarkers(places: List<HospitalMarker>, selectedHospitalId: Long?): List<Cluster> {
    val clusters = mutableListOf<Cluster>()
    val grid = Grid(places, selectedHospitalId)

    for (cell in grid.cells) {
        if (cell.markers.isEmpty()) continue

        val centerLat = cell.centerLat
        val centerLng = cell.centerLng
        val count = cell.markers.size
        val markers = cell.markers

        clusters.add(Cluster(centerLat, centerLng, count, markers))
    }

    return clusters
}

class Grid(
    private val places: List<HospitalMarker>,
    private val selectedHospitalId: Long?
) {
    val cells = mutableListOf<Cell>()

    init {
        for (place in places) {
            // 선택된 병원은 항상 개별 마커로 표시
            if (place.hospitalId == selectedHospitalId) {
                cells.add(Cell(place, isSelected = true))
                continue
            }
            
            val cell = getCell(place)
            if (cell == null) {
                val newCell = Cell(place, isSelected = false)
                cells.add(newCell)
            } else {
                if (!cell.isSelected) {
                    cell.markers.add(place)
                }
            }
        }
    }

    private fun getCell(place: HospitalMarker): Cell? {
        for (cell in cells) {
            if (!cell.isSelected && cell.contains(place)) return cell
        }
        return null
    }
}

class Cell(private val center: HospitalMarker, val isSelected: Boolean = false) {
    val centerLat = center.latitude
    val centerLng = center.longitude
    val markers = mutableListOf<HospitalMarker>()

    init {
        markers.add(center)
    }

    fun contains(place: HospitalMarker): Boolean {
        val distance = calculateDistanceKm(centerLat, centerLng, place.latitude, place.longitude)
        return distance < 5.0 // 5km 이내
    }

    private fun calculateDistanceKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val dLat = (lat2 - lat1) * 0.017453292519943295
        val dLng = (lng2 - lng1) * 0.017453292519943295
        val a = sin(dLat / 2) * sin(dLat / 2) + cos(lat1 * 0.017453292519943295) * cos(lat2 * 0.017453292519943295) * sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return 6371 * c
    }
}