package com.petbulance.data.mapper.feature.hospital

import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.FilterResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.MyReviewGetDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptAnalysisResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptItemDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewImageDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewSaveReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.SearchResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.UserReviewSearchDto
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam

// TODO : 명세 확인
fun UserReviewSearchDto.toDomain() = ReviewSearchItem(
    id = id,
    hospitalName = hospitalName,
    content = reviewContent,
    rating = overallRating,
    treatment = treatmentService,
    isReceiptVerified = receiptCheck,
    animalType = detailAnimalType,
    totalReviewCount = 0 // TODO : 이거 안쓰는 값인지 확인
)

fun FilterResDto.toDomain() = ReviewSearchItem(
    id = id,
    hospitalName = hospitalName,
    content = reviewContent,
    rating = totalRating,
    treatment = treatmentService,
    isReceiptVerified = receiptCheck,
    animalType = detailAnimalType,
    totalReviewCount = totalReviewCount
)

fun SearchResDto.toDomain() = HospitalReview(
    id = id,
    hospitalName = hospitalName,
    isReceiptVerified = isReceiptVerified,
    treatment = treatment,
    animalType = animalType,
    detailAnimalType = detailAnimalType,
    content = content,
    rating = rating,
    date = date,
    likeCount = likeCount,
    isLiked = isLiked,
    imageUrls = imageUrls,
    author = author,
    price = price,
)

fun MyReviewGetDto.toDomain() = MyReview(
    id = id,
    hospitalName = hospitalName,
    content = comment,
    date = reviewDate,
    rating = 0.0,
    representativeImage = hospitalImageUrl
)

fun SaveReviewParam.toDto() = ReviewSaveReqDto(
    receiptChecked = isReceipt,
    hospitalId = hospitalId,
    expertiseRating = rating.expertise,
    kindnessRating = rating.kindness,
    facilityRating = rating.facility,
    totalPrice = price,
    animalType = animalType,
    detailAnimalType = detailAnimalType,
    receiptItems = receiptItems.map {
        ReceiptItemDto(name = it.name, price = it.price)
    },
    visitDate = visitDate,
    reviewComment = comment,
    images = images?.map {
        ReviewImageDto(
            filename = it.filename,
            contentType = it.contentType,
            content = it.content,
            receipt = it.isReceipt
        )
    }
)

fun ReceiptAnalysisResDto.toDomain() = ReceiptAnalysisResult(
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    visitDate = visitDateTime,
    totalPrice = price,
    items = items.map { ReceiptItem(it.name, it.price) }
)