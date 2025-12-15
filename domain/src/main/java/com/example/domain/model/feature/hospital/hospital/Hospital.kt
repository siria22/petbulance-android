package com.example.domain.model.feature.hospital.hospital

/**
 * From HospitalResDto
 */
data class Hospital(
    val hospitalId: Long,
    val name: String,
    val lat: Double,
    val lng: Double,
    val distanceMeters: Double?,
    val phone: String?,
    val types: List<String>,
    val isOpenNow: Boolean,
    val openHours: String?,
    val thumbnailUrl: String?,
    val rating: Double?,
    val reviewCount: Int?
) {
    fun toMarker() = HospitalMarker(
        hospitalId = hospitalId,
        longitude = lng,
        latitude = lat,
        isOpened = isOpenNow,
        isSelected = false
    )
}
