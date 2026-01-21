package com.petbulance.data.repository.feature.hospital.hospital

import com.petbulance.domain.model.common.PagingResult
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalCard
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.model.feature.hospital.hospital.OpenHour
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockHospitalRepository @Inject constructor() : HospitalRepository {
    private val mockHospitals = List(20) { i ->
        Hospital(
            hospitalId = (i + 1L),
            name = "행복 동물병원 ${i + 1}호점 (Mock)",
            lat = 37.5665 + (i * 0.001),
            lng = 126.9780 + (i * 0.001),
            distanceMeters = 1234.5 + (i * 100),
            phone = "02-1234-5678",
            types = if (i % 2 == 0) listOf("DOG", "CAT") else listOf("DOG"),
            isOpenNow = i % 3 != 0,
            openHours = "09:00 - 18:00",
            thumbnailUrl = "https://picsum.photos/seed/${i + 1}/200/300",
            rating = 4.5 - (i * 0.1),
            reviewCount = 120 - (i * 5)
        )
    }

    override suspend fun searchHospitals(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
        bounds: String?,
        animal: String?,
        openNow: Boolean?,
        page: Int,
        size: Int
    ): Result<PagingResult<Hospital>> {
        delay(500)
        val start = page * size
        val end = (start + size).coerceAtMost(mockHospitals.size)

        val content =
            if (start >= mockHospitals.size) emptyList() else mockHospitals.subList(start, end)

        return Result.success(
            PagingResult(
                content = content,
                isLast = end >= mockHospitals.size,
                pageNumber = page
            )
        )
    }

    override suspend fun getHospitalDetail(hospitalId: Long): Result<HospitalDetail> {
        delay(500)
        val hospital = mockHospitals.find { it.hospitalId == hospitalId }

        return if (hospital != null) {
            Result.success(
                HospitalDetail(
                    hospitalId = hospitalId,
                    name = hospital.name.replace("(Mock)", "상세 (Mock)"),
                    address = "서울시 어딘가 ${hospital.hospitalId}번지",
                    lat = hospital.lat,
                    lng = hospital.lng,
                    phone = hospital.phone ?: "02-0000-0000",
                    acceptedAnimals = listOf("DOG", "CAT", "HAMSTER", "BIRD"),
                    openHours = listOf(
                        OpenHour("월-금", "09:00-19:00"),
                        OpenHour("토", "10:00-16:00"),
                        OpenHour("일", "휴무")
                    ),
                    notes = "주차 가능, 예약 시 10% 할인",
                    openNow = hospital.isOpenNow,
                    description = "이곳은 ${hospital.name}의 상세 설명입니다. 최신 장비와 최고의 의료진이 함께합니다.",
                    rating = hospital.rating ?: 0.0,
                    reviewCount = hospital.reviewCount ?: 0,
                    thumbnailUrl = hospital.thumbnailUrl
                )
            )
        } else {
            Result.failure(Exception("Hospital not found"))
        }
    }

    override suspend fun getHospitalCard(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): Result<HospitalCard> {
        delay(300)
        val hospital = mockHospitals.find { it.hospitalId == hospitalId }

        return if (hospital != null) {
            Result.success(
                HospitalCard(
                    hospitalId = hospitalId,
                    name = hospital.name.replace("(Mock)", "카드 (Mock)"),
                    lat = hospital.lat,
                    lng = hospital.lng,
                    distanceMeters = 550.0,
                    phone = hospital.phone ?: "02-0000-0000",
                    types = hospital.types,
                    isOpenNow = hospital.isOpenNow,
                    nextOpenHours = if (!hospital.isOpenNow) "내일 오전 9시" else "오후 6시까지",
                    thumbnailUrl = hospital.thumbnailUrl ?: "",
                    rating = hospital.rating ?: 0.0,
                    reviewCount = (hospital.reviewCount ?: 0).toLong()
                )
            )
        } else {
            Result.failure(Exception("Hospital not found"))
        }
    }
}