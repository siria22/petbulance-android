package com.petbulance.data.mapper.feature.hospital

import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.FilterResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.MyReviewGetDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptAnalysisResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptItemDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewSaveReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.SearchResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.UserReviewSearchDto
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies

fun UserReviewSearchDto.toDomain() = ReviewSearchItem(
    userNickname = userNickname,
    receiptCheck = receiptCheck,
    id = id,
    hospitalImage = hospitalImage,
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    treatmentService = treatmentService,
    animalType = AnimalCategory.fromString(animalType),
    detailAnimalType = AnimalSpecies.fromString(detailAnimalType),
    reviewContent = reviewContent,
    totalRating = totalRating,
    createDate = createDate,
    totalPrice = totalPrice,
    likeCount = likeCount,
    liked = liked,
    images = images
)

fun FilterResDto.toDomain() = ReviewSearchItem(
    userNickname = userNickname,
    receiptCheck = receiptCheck,
    id = id,
    hospitalImage = hospitalImage,
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    treatmentService = treatmentService,
    animalType = AnimalCategory.fromString(animalType),
    detailAnimalType = AnimalSpecies.fromString(detailAnimalType),
    reviewContent = reviewContent,
    totalRating = totalRating,
    createDate = createDate,
    totalPrice = totalPrice,
    likeCount = likeCount,
    liked = liked,
    images = images
)

fun SearchResDto.toDomain() = HospitalReview(
    id = id,
    hospitalName = hospitalName,
    isReceiptVerified = isReceiptVerified,
    treatment = treatment,
    animalType = AnimalCategory.fromString(animalType),
    detailAnimalType = AnimalSpecies.fromString(detailAnimalType),
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
    reviewComment = comment
)

fun ReceiptAnalysisResDto.toDomain() = ReceiptAnalysisResult(
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    visitDate = visitDateTime,
    totalPrice = price,
    items = items.map { ReceiptItem(it.name, it.price) }
)