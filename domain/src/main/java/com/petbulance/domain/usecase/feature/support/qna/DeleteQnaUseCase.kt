package com.petbulance.domain.usecase.feature.support.qna

import com.petbulance.domain.model.feature.support.qna.DeleteQnaResult
import com.petbulance.domain.repository.feature.support.QnaRepository
import javax.inject.Inject

class DeleteQnaUseCase @Inject constructor(
    private val qnaRepository: QnaRepository
) {
    suspend operator fun invoke(qnaId: Long): Result<DeleteQnaResult> {
        return qnaRepository.deleteQna(qnaId)
    }
}
