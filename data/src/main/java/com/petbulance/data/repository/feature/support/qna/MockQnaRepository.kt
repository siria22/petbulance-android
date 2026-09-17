package com.petbulance.data.repository.feature.support.qna

import com.petbulance.data.repository.MockFixtures
import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.model.feature.support.qna.QnaAnswer
import com.petbulance.domain.model.feature.support.qna.QnaListResult
import com.petbulance.domain.model.feature.support.qna.QnaParam
import com.petbulance.domain.model.feature.support.qna.QnaStatus
import com.petbulance.domain.repository.feature.support.QnaRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockQnaRepository @Inject constructor() : QnaRepository {

    private val qnaList = listOf(
        Qna(
            id = 2L,
            title = "영수증 인증이 계속 실패해요",
            content = "병원 영수증을 촬영했는데 병원 이름을 찾지 못했다고 나옵니다. 영수증이 조금 구겨져 있는데 다시 찍으면 될까요?",
            date = MockFixtures.dateLabel(1),
            status = QnaStatus.ANSWER_WAITING
        ),
        Qna(
            id = 1L,
            title = "병원 진료 시간이 실제와 달라요",
            content = "솜털 토끼 클리닉 토요일 진료 시간이 오후 2시까지로 바뀌었는데 앱에는 예전 시간이 나옵니다. 확인 부탁드려요.",
            date = MockFixtures.dateLabel(6),
            status = QnaStatus.ANSWER_COMPLETED,
            answer = QnaAnswer(
                content = "안녕하세요, 펫뷸런스입니다. 알려주신 내용을 병원에 확인해 진료 시간을 수정했습니다. 소중한 제보 감사합니다.",
                answeredAt = MockFixtures.dateLabel(5)
            )
        )
    )

    override suspend fun getQnaList(lastQnaId: Long?, pageSize: Int): Result<QnaListResult> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val startIndex = lastQnaId?.let { id -> qnaList.indexOfFirst { it.id == id } + 1 } ?: 0
        val page = qnaList.drop(startIndex).take(pageSize)
        return Result.success(QnaListResult(qnaList = page, hasNext = startIndex + page.size < qnaList.size))
    }

    override suspend fun getQnaById(qnaId: Long): Result<Qna> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val qna = qnaList.find { it.id == qnaId }
            ?: return Result.failure(NoSuchElementException("Qna not found: $qnaId"))
        return Result.success(qna)
    }

    override suspend fun createQna(param: QnaParam): Result<Qna> =
        Result.success(
            Qna(
                id = qnaList.maxOf { it.id } + 1,
                title = param.title,
                content = param.content,
                date = MockFixtures.dateLabel(0),
                status = QnaStatus.ANSWER_WAITING
            )
        )

    override suspend fun updateQna(qnaId: Long, param: QnaParam): Result<Qna> =
        Result.success(
            Qna(
                id = qnaId,
                title = param.title,
                content = param.content,
                date = MockFixtures.dateLabel(0),
                status = QnaStatus.ANSWER_WAITING
            )
        )

    override suspend fun deleteQna(qnaId: Long): Result<DeleteQnaResult> =
        Result.success(DeleteQnaResult(id = qnaId, message = "deleted"))
}
