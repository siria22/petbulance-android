package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.detail

import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaAnswer
import com.petbulance.domain.model.feature.support.qna.QnaStatus

data class QnaDetailData(
    val qnaId: Long,
    val title: String,
    val content: String,
    val date: String,
    val status: QnaStatus,
    val answer: QnaAnswer?,
    val isLoading: Boolean
) {
    companion object {
        val empty = QnaDetailData(
            qnaId = 0L,
            title = "",
            content = "",
            date = "",
            status = QnaStatus.ANSWER_WAITING,
            answer = null,
            isLoading = false
        )

        fun stub() = QnaDetailData(
            qnaId = 1L,
            title = "닉네임 변경 어떻게 하나요?",
            content = "안녕하세요!\n닉네임 좀 바꾸고 싶은데 어디서 하는지 못 찾겠어요😭 처음 가입할 때 자동으로 만들어진 닉네임이 너무 제 스타일이 아니라서요.\n마이페이지도 눌러보고 이것저것 찾아봤는데 수정하는 메뉴가 안 보이네요.\n닉네임 변경 가능한가요? 가능하면 어디서 바꾸는지 알려주세요",
            date = "2025-11-22",
            status = QnaStatus.ANSWER_COMPLETED,
            answer = QnaAnswer(
                content = "안녕하세요. 펫블런스 운영팀입니다 :)\n닉네임은 마이페이지 → 프로필 관리 → 닉네임 변경에서 수정하실 수 있어요!\n혹시 매뉴가 보이지 않으면 앱을 재시작하거나 업데이트 후 다시 시도해 주세요.\n감사합니다!",
                answeredAt = "2025-11-23"
            ),
            isLoading = false
        )
    }
}

data class QnaDetailUiState(
    val qna: Qna? = null,
    val isLoading: Boolean = false
) {
    companion object {
        val empty = QnaDetailUiState(
            qna = null,
            isLoading = false
        )
    }
}