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
    val reviewCount: Int?,
    val image: String?,
    val tags: List<HospitalTag>?
) {
    companion object {
        fun stub() = Hospital(
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
            reviewCount = 25,
            image = null,
            tags = listOf(
                HospitalTag(type = "WORKTYPE", value = "응급가능"),
                HospitalTag(type = "WORKTYPE", value = "야간진료"),
                HospitalTag(type = "ANIMALTYPE", value = "소형포유류"),
                HospitalTag(type = "LOCATIONTYPE", value = "보문역"),
                HospitalTag(type = "LOCATIONTYPE", value = "주차가능")
            )
        )

        fun stubs() = listOf(
            Hospital(
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
                reviewCount = 25,
                image = null,
                tags = listOf(
                    HospitalTag(type = "WORKTYPE", value = "응급가능"),
                    HospitalTag(type = "WORKTYPE", value = "야간진료"),
                    HospitalTag(type = "ANIMALTYPE", value = "소형포유류"),
                    HospitalTag(type = "LOCATIONTYPE", value = "보문역"),
                    HospitalTag(type = "LOCATIONTYPE", value = "주차가능")
                )
            ),
            Hospital(
                hospitalId = 2,
                name = "행복 동물병원",
                lat = 36.9,
                lng = 126.0,
                distanceMeters = 1200.0,
                phone = "02-9876-5432",
                types = listOf("REPTILE", "dd"),
                isOpenNow = true,
                openHours = "20:00에 영업 종료",
                thumbnailUrl = null,
                rating = 4.8,
                reviewCount = 25,
                image = null,
                tags = listOf(
                    HospitalTag(type = "WORKTYPE", value = "응급가능"),
                    HospitalTag(type = "WORKTYPE", value = "야간진료"),
                    HospitalTag(type = "ANIMALTYPE", value = "소형포유류")
                )
            )
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

data class HospitalTag(
    val type: String,
    val value: String
)
