package com.petbulance.domain.usecase.feature.support.qna

import com.petbulance.domain.model.feature.support.qna.Qna
import com.petbulance.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class GetQnaByIdUseCase @Inject constructor(
    private val qnaRepository: QnaRepository
) {
    suspend operator fun invoke(qnaId: Long): Result<Qna> {
        return qnaRepository.getQnaById(qnaId)
    }
}
