package com.example.data.repository.feature.community.board

import com.example.domain.model.feature.community.board.Board
import com.example.domain.repository.feature.community.BoardRepository
import jakarta.inject.Inject

class MockBoardRepository @Inject constructor() : BoardRepository {
    override suspend fun getBoardList(): Result<List<Board>> {
        return Result.success(mockBoardList)
    }
}

private val mockBoardList = listOf(
    Board(
        boardId = 1L,
        nameKr = "자유게시판",
        nameEn = "Free Board",
        description = "반려동물과 함께하는 일상을 자유롭게 이야기하는 공간입니다."
    ),
    Board(
        boardId = 2L,
        nameKr = "질문과 답변",
        nameEn = "Q&A",
        description = "궁금한 점이 있다면 언제든 물어보세요. 전문가와 이웃이 답변해드립니다."
    ),
    Board(
        boardId = 3L,
        nameKr = "병원 방문 후기",
        nameEn = "Hospital Reviews",
        description = "실제 다녀온 동물병원의 솔직한 후기와 영수증 인증 정보를 공유합니다."
    ),
    Board(
        boardId = 4L,
        nameKr = "건강/질병 정보",
        nameEn = "Health Info",
        description = "아이들의 건강 관리를 위한 필수 상식과 질병 정보를 모았습니다."
    ),
    Board(
        boardId = 5L,
        nameKr = "실종/제보",
        nameEn = "Lost & Found",
        description = "잃어버린 아이를 찾거나, 유기동물을 발견했을 때 제보해주세요."
    ),
    Board(
        boardId = 6L,
        nameKr = "중고장터",
        nameEn = "Marketplace",
        description = "사용하지 않는 용품을 이웃과 나누거나 저렴하게 거래해보세요."
    )
)