package com.petbulance.domain.model.feature.hospital.hospital

/**
 * From HospitalDetailResDto
 */
data class HospitalDetail(
    val hospitalId: Long,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val phone: String,
    val acceptedAnimals: List<String>,
    val openHours: List<OpenHour>,
    val notes: String,
    val openNow: Boolean,
    val description: String,
    val rating: Double,
    val reviewCount: Int,
    val thumbnailUrl: String?
) {
    fun toMarker() = HospitalMarker(
        hospitalId = hospitalId,
        longitude = lng,
        latitude = lat,
        isOpened = openNow,
        isSelected = false
    )
}

data class OpenHour(
    val day: String,
    val hours: String
)