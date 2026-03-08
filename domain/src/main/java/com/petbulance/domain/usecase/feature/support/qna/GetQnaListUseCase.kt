package com.petbulance.domain.usecase.feature.support.qna

import com.petbulance.domain.model.feature.support.qna.QnaListResult
import com.petbulance.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class GetQnaListUseCase @Inject constructor(
    private val qnaRepository: QnaRepository
) {
    suspend operator fun invoke(lastQnaId: Long? = null, pageSize: Int = 10): Result<QnaListResult> {
        return qnaRepository.getQnaList(lastQnaId, pageSize)
    }
}
