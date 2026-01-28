package com.petbulance.presentation.screen.feature.search.info

import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.type.ReviewSortType

data class HospitalInfoData(
    val hospitalUiData: HospitalUiData = HospitalUiData(),
    val reviewUiData: ReviewUiData = ReviewUiData(),
) {
    companion object {
        val init = HospitalInfoData()

        fun stub() = HospitalInfoData(
            hospitalUiData = HospitalUiData(
                hospital = Hospital.stub(),
                hospitalDetail = HospitalDetail.stub()
            ),
            reviewUiData = ReviewUiData(
                reviews = HospitalReview.stubs()
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
    val sortBy: ReviewSortType = ReviewSortType.LATEST,
    val onlyImage: Boolean = false
)