package com.petbulance.domain.model.feature.hospital.review

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.AnimalSpecies

data class ReviewSearchItem(
    val userNickname: String,
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String? = null,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val animalType: AnimalCategory,
    val detailAnimalType: AnimalSpecies,
    val reviewContent: String,
    val totalRating: Double,
    val createDate: String,
    val totalPrice: Int,
    val likeCount: Int,
    val liked: Boolean,
    val images: List<String>
) {
    companion object {
        fun stub() = ReviewSearchItem(
            userNickname = "새벽숲",
            receiptCheck = false,
            id = 3L,
            hospitalImage = null,
            hospitalId = 3L,
            hospitalName = "고려종합동물병원",
            treatmentService = "진료비(9900), 방사선 (X-ray) 검사(복부)(45000), 5kg 이하(129000), 검사(초음파-복부)(55000), 혈구 (NMB)(96000), *병리검사(혈액-CRP)(33000), 검사(흉수, 복수-CBC)(33000), *[KVL]Cytology(FNA)-1 site(100000), 처치-복수천자(복수 제거 목적)(77000), 비타민K(vit K)-ample(5500)",
            animalType = AnimalCategory.fromString("BIRD"),
            detailAnimalType = AnimalSpecies.fromString("PARROT"),
            reviewContent = "우리집 앵무새가 감기에 걸려서 병원에 찾아갔는 데 ~~",
            totalRating = 2.83,
            createDate = "2026-01-26T08:05:01",
            totalPrice = 100000,
            likeCount = 0,
            liked = false,
            images = emptyList()
        )
    }
}
