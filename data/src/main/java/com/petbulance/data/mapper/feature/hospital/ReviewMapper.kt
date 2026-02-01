package com.petbulance.data.mapper.feature.hospital

import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.FilterResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.MyReviewGetDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptAnalysisResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptItemDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewDetailResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewImageDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewModifyReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewSaveReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.SearchResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.UserReviewSearchDto
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ModifyReviewParam
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReceiptItem
import com.petbulance.domain.model.feature.hospital.review.ReviewDetail
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
    animalType = animalType.name,
    detailAnimalType = detailAnimalType,
    receiptItems = receiptItems.map {
        ReceiptItemDto(name = it.name, price = it.price)
    },
    visitDate = visitDate?.ifBlank { null },
    reviewComment = comment
)

fun ReceiptAnalysisResDto.toDomain() = ReceiptAnalysisResult(
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    visitDate = visitDateTime,
    totalPrice = price,
    items = items.map { ReceiptItem(it.name, it.price) }
)

fun ReviewSearchItem.toHospitalReview() = HospitalReview(
    id = this.id,
    isReceiptVerified = this.receiptCheck,
    treatment = this.treatmentService,
    animalType = this.animalType,
    detailAnimalType = this.detailAnimalType,
    content = this.reviewContent,
    rating = this.totalRating,
    date = this.createDate,
    likeCount = this.likeCount,
    isLiked = this.liked,
    imageUrls = this.images,
    author = this.userNickname,
    price = this.totalPrice,
    hospitalName = this.hospitalName
)

fun ModifyReviewParam.toDto() = ReviewModifyReqDto(
    reviewId = reviewId,
    title = title,
    receiptChecked = isReceipt,
    hospitalId = hospitalId,
    expertiseRating = rating.expertise,
    kindnessRating = rating.kindness,
    facilityRating = rating.facility,
    totalPrice = price,
    animalType = animalType.name,
    detailAnimalType = detailAnimalType,
    receiptItems = receiptItems.map { ReceiptItemDto(it.name, it.price) },
    visitDate = visitDate,
    reviewComment = comment,
    images = images.map {
        ReviewImageDto(
            filename = it.filename,
            contentType = it.contentType,
        )
    }
)

fun ReviewDetailResDto.toDomain(currentUserName: String) = ReviewDetail(
    userNickname = userNickname ?: "알 수 없음",
    receiptCheck = receiptCheck,
    id = id,
    hospitalImage = hospitalImage,
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    treatmentService = treatmentService,
    animalType = AnimalCategory.fromString(animalType),
    detailAnimalType = AnimalSpecies.fromString(detailAnimalType ?: "PARROT"),
    reviewContent = reviewContent,
    facilityRating = facilityRating,
    expertiseRating = expertiseRating,
    kindnessRating = kindnessRating,
    createDate = createDate,
    totalPrice = totalPrice,
    likeCount = likeCount,
    liked = liked,
    visitDate = visitDate ?: "",
    images = images ?: emptyList(),
    userProfileImage = userProfileImage,
    viewCount = viewCount,
    isAuthor = (currentUserName == userNickname)
)