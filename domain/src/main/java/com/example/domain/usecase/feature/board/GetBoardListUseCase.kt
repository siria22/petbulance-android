package com.example.domain.usecase.feature.board

import com.example.domain.model.feature.community.board.Board
import com.example.domain.repository.feature.community.BoardRepository
import javax.inject.Inject

class GetBoardListUseCase @Inject constructor(
    private val repository: BoardRepository
) {
    suspend operator fun invoke(): List<Board> {
        return repository.getBoardList().getOrThrow()
    }
}