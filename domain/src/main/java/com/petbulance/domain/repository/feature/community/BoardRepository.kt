package com.petbulance.domain.repository.feature.community

import com.petbulance.domain.model.feature.community.board.Board

interface BoardRepository {
    suspend fun getBoardList(): Result<List<Board>>
}