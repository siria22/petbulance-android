package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.qna.list

import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaStatus

data class QnaListData(
    val qnaList: List<Qna>,
    val successMessage: SuccessMessageType?
) {
    companion object {
        val empty = QnaListData(
            qnaList = emptyList(),
            successMessage = null
        )

        fun stub() = QnaListData(
            qnaList = listOf(
                Qna(
                    id = 1L,
                    title = "닉네임 변경 어떻게 하나요?",
                    content = "안녕하세요!\n닉네임 좀 바꾸고 싶은데 어디서 하는지 못 찾겠어요😭",
                    date = "2025-11-22",
                    status = QnaStatus.ANSWER_WAITING,
                    answer = null
                ),
                Qna(
                    id = 2L,
                    title = "문의",
                    content = "답변완료",
                    date = "2025-11-22",
                    status = QnaStatus.ANSWER_COMPLETED,
                    answer = null
                )
            ),
            successMessage = null
        )
    }
}

sealed class SuccessMessageType(val message: String, val hasAction: Boolean) {
    data class Created(val qnaId: Long) : SuccessMessageType("문의를 추가했어요", true)
    data class Updated(val qnaId: Long) : SuccessMessageType("문의를 수정했어요", true)
    data object Deleted : SuccessMessageType("문의를 삭제했어요", false)
}
