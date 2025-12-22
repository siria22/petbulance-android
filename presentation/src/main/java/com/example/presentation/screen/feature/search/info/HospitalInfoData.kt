package com.example.presentation.screen.feature.search.info

import com.example.domain.model.feature.hospital.hospital.Hospital
import com.example.domain.model.feature.hospital.hospital.HospitalDetail
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.type.ReviewSortType

data class HospitalInfoData(
    val hospitalUiData: HospitalUiData = HospitalUiData(),
    val reviewUiData: ReviewUiData = ReviewUiData(),
) {
    companion object {
        val init = HospitalInfoData()

        fun stub() = HospitalInfoData(
            hospitalUiData = HospitalUiData(
                hospital = Hospital(
                    hospitalId = 1,
                    name = "화타동물병원",
                    lat = 37.5509,
                    lng = 126.9410,
                    distanceMeters = 1200.0,
                    phone = "02-1234-5678",
                    types = listOf("파충류", "양서류", "어류"),
                    isOpenNow = true,
                    thumbnailUrl = "",
                    rating = 4.8,
                    reviewCount = 25,
                    openHours = "20:00에 영업 종료"
                ),
                hospitalDetail = HospitalDetail(
                    hospitalId = 1,
                    name = "화타동물병원",
                    address = "서울 oo구 oo로 00, 0층 병원명",
                    lat = 37.5509,
                    lng = 126.9410,
                    phone = "02-1234-5678",
                    acceptedAnimals = listOf("개", "고양이"),
                    openHours = emptyList(), // 필요 시 채움
                    notes = "주차 가능",
                    openNow = true,
                    description = "상세 설명"
                )
            ),
            reviewUiData = ReviewUiData(
                reviews = listOf(
                    HospitalReview.stub
                )
            )
        )
    }
}

data class HospitalUiData(
    val hospital: Hospital? = null,
    val hospitalDetail: HospitalDetail? = null
)

data class ReviewUiData(
    val reviews: List<HospitalReview> = emptyList(),
    val sortBy: ReviewSortType = ReviewSortType.RECENT,
    val onlyImage: Boolean = false
)