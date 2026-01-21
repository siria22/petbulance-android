package com.petbulance.domain.model.feature.home

data class HomeScreenReview(
    val id: Long,
    val hospitalName: String,
    val rating: Double,
    val reviewCount: Int,
    val image: String?,
    val content: String
) {
    companion object {
        fun stub(): HomeScreenReview = HomeScreenReview(
            id = 1,
            hospitalName = "동물병원",
            rating = 4.7,
            reviewCount = 123,
            image = "",
            content = "리뷰 내용이 너무 길어지면 이걸 어떻게 처리할지 고민했는데, " +
                    "그냥 내용 즉당히 자르는 것 보다 알아서 래핑되다가 ...으로 처리되는게 더 좋아보여서 그걸로 했음"
        )
    }
}