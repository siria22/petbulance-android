package com.example.data.mapper.feature.hospital

import com.example.data.datasource.remote.network.feature.hospital.review.dto.FilterResDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.MyReviewGetDao
import com.example.data.datasource.remote.network.feature.hospital.review.dto.ReceiptAnalysisResDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.ReceiptItemDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.ReviewImageDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.ReviewSaveReqDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.SearchResDto
import com.example.data.datasource.remote.network.feature.hospital.review.dto.UserReviewSearchDto
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.MyReview
import com.example.domain.model.feature.hospital.review.ReviewSearchItem
import com.example.domain.model.feature.hospital.review.SaveReviewParam

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
    isReceiptVerified = receiptCheck,
    treatment = treatmentService,
    animalType = animalType,
    detailAnimalType = detailAnimalType,
    content = reviewContent,
    rating = totalRating,
    date = reviewDate,
    likeCount = likeCount,
    isLiked = liked,
    imageUrls = images,
    author = author,
    price = totalPrice
)

fun MyReviewGetDao.toDomain() = MyReview(
    id = id,
    hospitalName = hospitalName,
    content = content,
    date = createdAt,
    rating = rating,
    representativeImage = images.firstOrNull()
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
        ReceiptItemDto(name = it.name, price = it.price) // 매핑 수정
    },
    visitDate = visitDate,
    reviewComment = comment,
    title = title,
    images = images?.map {
        ReviewImageDto(filename = it.filename, contentType = it.contentType)
    }
)

fun ReceiptAnalysisResDto.toDomain() = ReceiptAnalysisResult(
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    visitDate = visitDateTime,
    totalPrice = price,
    items = items.map { ReceiptItem(it.name, it.price) }
)