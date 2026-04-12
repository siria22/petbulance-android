package com.petbulance.domain.repository.nonfeature.device

import kotlinx.coroutines.flow.Flow

/**
 * 위치 정보를 제공하는 인터페이스.
 * ViewModel에서 FusedLocationProviderClient 직접 의존을 제거하기 위해 사용.
 */
interface LocationProvider {
    fun getLocationUpdates(): Flow<DeviceLocation>
}

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double
)
