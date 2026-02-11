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
        Hospital.stub()
    }

    override suspend fun searchHospitals(
        q: String?,
        region: String?,
        bounds: String?,
        animal: String?,
        openNow: Boolean?,
        sortBy: String?,
        size: Int,
        cursorId: Long?,
        cursorDistance: Double?,
        cursorRating: Double?,
        cursorReviewCount: Long?
    ): Result<PagingResult<Hospital>> {
        delay(500)
        return Result.success(
            PagingResult(
                content = mockHospitals.take(size),
                hasNext = mockHospitals.size > size,
                cursorId = mockHospitals.getOrNull(size - 1)?.hospitalId,
                cursorDistance = null,
                cursorRating = null,
                cursorReviewCount = null
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
                    acceptedAnimals = listOf("DOG", "CAT", "HAMSTER", "AVIAN"),
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

    override suspend fun getHospitalCard(hospitalId: Long): Result<HospitalCard> {
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
                    reviewCount = (hospital.reviewCount ?: 0).toLong(),
                    image = ""
                )
            )
        } else {
            Result.failure(Exception("Hospital not found"))
        }
    }
}