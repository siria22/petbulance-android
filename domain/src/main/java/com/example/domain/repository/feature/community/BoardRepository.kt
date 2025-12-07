package com.example.domain.repository.feature.community

import com.example.domain.model.feature.community.board.Board

interface BoardRepository {
    suspend fun getBoardList(): Result<List<Board>>
}