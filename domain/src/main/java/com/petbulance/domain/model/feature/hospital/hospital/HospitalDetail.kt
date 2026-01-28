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
    companion object {
        fun stub() = HospitalDetail(
            hospitalId = 1,
            name = "화타동물병원",
            address = "서울 oo구 oo로 00, 0층 병원명",
            lat = 37.5509,
            lng = 126.9410,
            phone = "02-1234-5678",
            acceptedAnimals = listOf("개", "고양이"),
            openHours = emptyList(),
            notes = "주차 가능",
            openNow = true,
            description = "상세 설명",
            rating = 4.3,
            reviewCount = 10,
            thumbnailUrl = ""
        )
    }
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