package com.petbulance.presentation.screen.feature.community

import com.petbulance.domain.model.feature.community.post.NoticeBanner
import com.petbulance.domain.model.feature.community.post.PostSummary

data class CommunityData(
    val noticeBanner: NoticeBanner?,
    val posts: List<PostSummary>,
    val hasNext: Boolean,
    val currentType: String?,
    val currentTopic: String?,
    val currentSort: String
) {
    companion object {
        val empty = CommunityData(
            noticeBanner = null,
            posts = emptyList(),
            hasNext = false,
            currentType = null,
            currentTopic = null,
            currentSort = "latest"
        )

        fun stub() = CommunityData(
            noticeBanner = NoticeBanner(
                noticeId = 1,
                noticeStatus = "ACTIVE",
                title = "(공지사항) 12/16 02:00~08:00 서비스 점검으로 인한 앱 사용 중단",
                content = "안녕하세요. 펫뷸런스입니다."
            ),
            posts = listOf(
                PostSummary(
                    id = 1,
                    type = "SMALLMAMMALS",
                    topic = "HEALTH",
                    title = "게코 도마뱀 키우시는 분 있나요?",
                    content = "케이지 추천해주세요!",
                    thumbnailUrl = null,
                    imageCount = 5,
                    viewCount = 132,
                    commentCount = 8,
                    likeCount = 12,
                    createdAt = "19시간 전",
                    isLiked = false
                ),
                PostSummary(
                    id = 2,
                    type = "AVIAN",
                    topic = "DAILY",
                    title = "햄스터 다리 왜일까요??",
                    content = "아제부터 약간 저는것같은데 병원을 가봐야하지 이렇게 할지 모르겠어요. 케이지 안에 미끄럼틀 타다가 그랬겠죠? 운동을 시키고 싶은데, 어떤 놀이가 좋을까요?",
                    thumbnailUrl = "https://example.com/image.jpg",
                    imageCount = 3,
                    viewCount = 54,
                    commentCount = 4,
                    likeCount = 3,
                    createdAt = "3시간 전",
                    isLiked = false
                )
            ),
            hasNext = true,
            currentType = null,
            currentTopic = null,
            currentSort = "latest"
        )
    }
}