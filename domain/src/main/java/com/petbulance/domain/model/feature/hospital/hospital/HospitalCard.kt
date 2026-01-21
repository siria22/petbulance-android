package com.petbulance.domain.model.feature.hospital.hospital

data class HospitalCard(
    val hospitalId: Long,
    val name: String,
    val lat: Double,
    val lng: Double,
    val distanceMeters: Double,
    val phone: String,
    val types: List<String>,
    val isOpenNow: Boolean,
    val nextOpenHours: String,
    val thumbnailUrl: String,
    val rating: Double,
    val reviewCount: Long
) {
    fun toHospital() = Hospital(
        hospitalId = hospitalId,
        name = name,
        lat = lat,
        lng = lng,
        distanceMeters = distanceMeters,
        phone = phone,
        types = types,
        isOpenNow = isOpenNow,
        openHours = nextOpenHours,
        thumbnailUrl = thumbnailUrl,
        rating = rating,
        reviewCount = reviewCount.toInt()
    )
}