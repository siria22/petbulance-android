package com.petbulance.domain.model.feature.hospital.hospital

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
    companion object {
        val stub = Hospital(
            hospitalId = 1,
            name = "화타동물병원",
            lat = 37.0,
            lng = 127.0,
            distanceMeters = 1200.0,
            phone = "02-1234-5678",
            types = listOf("REPTILE", "dd"),
            isOpenNow = true,
            openHours = "20:00에 영업 종료",
            thumbnailUrl = null,
            rating = 4.8,
            reviewCount = 25
        )
    }
    fun toMarker() = HospitalMarker(
        hospitalId = hospitalId,
        longitude = lng,
        latitude = lat,
        isOpened = isOpenNow,
        isSelected = false
    )
}
