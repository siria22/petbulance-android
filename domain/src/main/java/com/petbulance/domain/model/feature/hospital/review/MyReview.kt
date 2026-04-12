package com.petbulance.domain.model.feature.hospital.review

data class MyReview(
    val id: Long,
    val hospitalName: String,
    val content: String,
    val date: String,
    val representativeImage: String?,
    val likeCount: Int,
    val isReceiptVerified: Boolean,
    val status: ReviewStatus
) {
    companion object {
        fun stub() = MyReview(
            id = 1L,
            hospitalName = "행복 동물병원",
            content = "휴! 행복해지네요 ㅎ",
            date = "2026-02-05",
            representativeImage = null,
            likeCount = 10,
            isReceiptVerified = true,
            status = ReviewStatus.REGISTERED
        )

        fun stubs() = listOf(
            MyReview(
                1L,
                "행복 동물병원",
                "선생님이 정말 친절하셔서 마음이 놓였어요.",
                "2026-02-01",
                null,
                10,
                true,
                ReviewStatus.REGISTERED
            ),
            MyReview(
                2L,
                "튼튼 아프리카 동물병원",
                "시설이 깔끔하고 대기 시간이 짧아서 좋았습니다.",
                "2026-02-02",
                null,
                10,
                true,
                ReviewStatus.UNDER_REVIEW
            ),
            MyReview(
                3L,
                "튼튼 아프리카 동물병원",
                "시설이 깔끔하고 대기 시간이 짧아서 좋았습니다.",
                "2026-02-02",
                null,
                10,
                true,
                ReviewStatus.UNDER_REVIEW
            ),
        )
    }
}
