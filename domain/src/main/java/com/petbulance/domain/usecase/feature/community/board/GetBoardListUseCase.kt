package com.petbulance.domain.usecase.feature.community.board

import com.petbulance.domain.model.feature.community.board.Board
import com.petbulance.domain.repository.feature.community.BoardRepository
import javax.inject.Inject

class GetBoardListUseCase @Inject constructor(
    private val repository: BoardRepository
) {
    suspend operator fun invoke(): List<Board> {
        return repository.getBoardList().getOrThrow()
    }
}